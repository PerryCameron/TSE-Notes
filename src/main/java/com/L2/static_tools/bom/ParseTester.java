package com.L2.static_tools.bom;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import org.w3c.dom.Node;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ParseTester – test harness for the BOM XML parser + JavaFX TreeTableView
 */
public class ParseTester extends Application {

    /* --------------------------------------------------------------------- *
     * 1. POJO model – one class for every <component> node (original JAXB)
     * --------------------------------------------------------------------- */
    @XmlRootElement(name = "component")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Component {

        private String item;
        private long   itemid;
        private int    level;
        private String desc;
        private String rev;
        private String uom;
        private double quantity;
        @XmlElement(name = "item_type")
        private String itemType;

        // Nested components (0…n)
        @XmlElement(name = "component")
        private List<Component> components = new ArrayList<>();

        // Reference designators – will be turned into a CSV string
        @XmlJavaTypeAdapter(RefDesAdapter.class)
        private String refdesCsv;               // <-- final result

        // -----------------------------------------------------------------
        // Helper for debugging / pretty-print
        // -----------------------------------------------------------------
        @Override
        public String toString() {
            return String.format(
                    "%s (lvl=%d, qty=%.3f, type=%s) %s",
                    item, level, quantity, itemType,
                    refdesCsv != null ? "refs=" + refdesCsv : ""
            );
        }

        public void prettyPrint(int indent) {
            String pad = "  ".repeat(indent);
            System.out.printf("%s%s%n", pad, this);
            for (Component c : components) {
                c.prettyPrint(indent + 1);
            }
        }
    }

    /* --------------------------------------------------------------------- *
     * 2. Adapter that turns <refdeslist><refdes>R1</refdes>…</refdeslist>
     *     into a single CSV string.
     * --------------------------------------------------------------------- */
    public static class RefDesAdapter extends XmlAdapter<Object, String> {
        @Override
        public String unmarshal(Object v) throws Exception {
            if (v == null) return null;
            Node listNode = (Node) v;
            List<String> refs = new ArrayList<>();
            Node child = listNode.getFirstChild();
            while (child != null) {
                if ("refdes".equals(child.getLocalName())) {
                    String text = child.getTextContent();
                    if (text != null && !text.isBlank()) {
                        refs.add(text.trim());
                    }
                }
                child = child.getNextSibling();
            }
            return refs.isEmpty() ? null : String.join(",", refs);
        }

        @Override
        public Object marshal(String v) throws Exception {
            throw new UnsupportedOperationException("marshalling not needed");
        }
    }

    /* --------------------------------------------------------------------- *
     * 3. Helper to strip the SOAP envelope and feed the inner payload to JAXB.
     * --------------------------------------------------------------------- */
    private static Component parseBomXml(String xml) throws Exception {
        int start = xml.indexOf("<bomexploder_response");
        if (start == -1) throw new IllegalArgumentException("No <bomexploder_response> found");
        int end = xml.lastIndexOf("</bomexploder_response>") + "</bomexploder_response>".length();
        String payload = xml.substring(start, end);

        JAXBContext jc = JAXBContext.newInstance(BomWrapper.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        XMLInputFactory xif = XMLInputFactory.newFactory();
        StringReader sr = new StringReader(payload);
        XMLStreamReader xsr = xif.createXMLStreamReader(sr);
        xsr.nextTag();

        BomWrapper wrapper = unmarshaller.unmarshal(xsr, BomWrapper.class).getValue();
        if (wrapper.components.isEmpty()) {
            throw new IllegalStateException("No top-level component found");
        }
        return wrapper.components.get(0);
    }

    @XmlRootElement(name = "bomexploder_response")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BomWrapper {
        @XmlElement(name = "component")
        List<Component> components = new ArrayList<>();
    }

    /* --------------------------------------------------------------------- *
     * 4. Read BOM XML from resources
     * --------------------------------------------------------------------- */
    public static String readBomXml() throws IOException {
        try (InputStream is = ParseTester.class.getClassLoader()
                .getResourceAsStream("bom.xml");
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /* --------------------------------------------------------------------- *
     * 5. JavaFX DTO – renamed to avoid conflict
     * --------------------------------------------------------------------- */
    public static class ComponentDTO {
        private final StringProperty item = new SimpleStringProperty();
        private final LongProperty itemId = new SimpleLongProperty();
        private final IntegerProperty level = new SimpleIntegerProperty();
        private final StringProperty description = new SimpleStringProperty();
        private final StringProperty revision = new SimpleStringProperty();
        private final StringProperty uom = new SimpleStringProperty();
        private final DoubleProperty quantity = new SimpleDoubleProperty();
        private final StringProperty itemType = new SimpleStringProperty();
        private final StringProperty refDes = new SimpleStringProperty();

        public ComponentDTO(Component comp) {
            item.set(comp.item);
            itemId.set(comp.itemid);
            level.set(comp.level);
            description.set(comp.desc);
            revision.set(comp.rev);
            uom.set(comp.uom);
            quantity.set(comp.quantity);
            itemType.set(comp.itemType);
            refDes.set(comp.refdesCsv);
        }

        // Getters for properties
        public StringProperty itemProperty() { return item; }
        public LongProperty itemIdProperty() { return itemId; }
        public IntegerProperty levelProperty() { return level; }
        public StringProperty descriptionProperty() { return description; }
        public StringProperty revisionProperty() { return revision; }
        public StringProperty uomProperty() { return uom; }
        public DoubleProperty quantityProperty() { return quantity; }
        public StringProperty itemTypeProperty() { return itemType; }
        public StringProperty refDesProperty() { return refDes; }
    }

    /* --------------------------------------------------------------------- *
     * 6. Build TreeItem<ComponentDTO> from Component hierarchy
     * --------------------------------------------------------------------- */
    private static TreeItem<ComponentDTO> buildTree(Component root) {
        TreeItem<ComponentDTO> rootItem = new TreeItem<>(new ComponentDTO(root));
        addChildren(rootItem, root.components);
        return rootItem;
    }

    private static void addChildren(TreeItem<ComponentDTO> parentItem, List<Component> children) {
        for (Component child : children) {
            TreeItem<ComponentDTO> childItem = new TreeItem<>(new ComponentDTO(child));
            parentItem.getChildren().add(childItem);
            if (!child.components.isEmpty()) {
                addChildren(childItem, child.components);
            }
        }
    }

    /* --------------------------------------------------------------------- *
     * 7. JavaFX start() – TreeTableView setup
     * --------------------------------------------------------------------- */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String xml = readBomXml();
        Component rootComponent = parseBomXml(xml);
        TreeItem<ComponentDTO> root = buildTree(rootComponent);
        root.setExpanded(true); // Expand top level by default

        TreeTableView<ComponentDTO> treeTable = new TreeTableView<>(root);
        treeTable.setShowRoot(true);

        // Columns
        TreeTableColumn<ComponentDTO, String> colItem = new TreeTableColumn<>("Item");
        colItem.setCellValueFactory(p -> p.getValue().getValue().itemProperty());
        colItem.setPrefWidth(120);

        TreeTableColumn<ComponentDTO, Number> colItemId = new TreeTableColumn<>("Item ID");
        colItemId.setCellValueFactory(p -> p.getValue().getValue().itemIdProperty());
        colItemId.setPrefWidth(80);

        TreeTableColumn<ComponentDTO, Number> colLevel = new TreeTableColumn<>("Level");
        colLevel.setCellValueFactory(p -> p.getValue().getValue().levelProperty());
        colLevel.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, String> colDesc = new TreeTableColumn<>("Description");
        colDesc.setCellValueFactory(p -> p.getValue().getValue().descriptionProperty());
        colDesc.setPrefWidth(200);

        TreeTableColumn<ComponentDTO, String> colRev = new TreeTableColumn<>("Rev");
        colRev.setCellValueFactory(p -> p.getValue().getValue().revisionProperty());
        colRev.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, String> colUom = new TreeTableColumn<>("UOM");
        colUom.setCellValueFactory(p -> p.getValue().getValue().uomProperty());
        colUom.setPrefWidth(60);

        TreeTableColumn<ComponentDTO, Number> colQty = new TreeTableColumn<>("Quantity");
        colQty.setCellValueFactory(p -> p.getValue().getValue().quantityProperty());
        colQty.setPrefWidth(80);
        colQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        TreeTableColumn<ComponentDTO, String> colType = new TreeTableColumn<>("Type");
        colType.setCellValueFactory(p -> p.getValue().getValue().itemTypeProperty());
        colType.setPrefWidth(70);

        TreeTableColumn<ComponentDTO, String> colRef = new TreeTableColumn<>("Ref Des");
        colRef.setCellValueFactory(p -> p.getValue().getValue().refDesProperty());
        colRef.setPrefWidth(150);

        treeTable.getColumns().addAll(
                colItem, colItemId, colLevel, colDesc, colRev,
                colUom, colQty, colType, colRef
        );

        treeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        Scene scene = new Scene(treeTable, 1000, 700);
        primaryStage.setTitle("BOM TreeTableView");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* --------------------------------------------------------------------- *
     * 8. Main entry point – launch JavaFX
     * --------------------------------------------------------------------- */
    public static void main(String[] args) {
        // For testing console output only:
        try {
            String xml = readBomXml();
            Component root = parseBomXml(xml);

            System.out.println("=== BOM hierarchy (pretty printed) ===");
            root.prettyPrint(0);

            List<Component> flat = new ArrayList<>();
            flatten(root, flat);
            System.out.println("\n=== Flattened list size: " + flat.size() + " ===");

            // Launch JavaFX UI
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void flatten(Component c, List<Component> out) {
        out.add(c);
        for (Component child : c.components) {
            flatten(child, out);
        }
    }
}