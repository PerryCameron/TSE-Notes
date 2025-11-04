package com.L2.static_tools.bom;

import com.L2.dto.bom.ComponentXML;
import com.L2.dto.bom.ComponentDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        public Object marshal(String v) {
            throw new UnsupportedOperationException("marshalling not needed");
        }
    }

    /* --------------------------------------------------------------------- *
     * 3. Helper to strip the SOAP envelope and feed the inner payload to JAXB.
     * --------------------------------------------------------------------- */
    private static ComponentXML parseBomXml(String xml) throws Exception {
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
        return wrapper.components.getFirst();
    }

    @XmlRootElement(name = "bomexploder_response")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BomWrapper {
        @XmlElement(name = "component")
        List<ComponentXML> components = new ArrayList<>();
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
     * 6. Build TreeItem<ComponentDTO> from Component hierarchy
     * --------------------------------------------------------------------- */
    private static TreeItem<ComponentDTO> buildTree(ComponentXML root) {
        TreeItem<ComponentDTO> rootItem = new TreeItem<>(new ComponentDTO(root));
        addChildren(rootItem, root.getComponents());
        return rootItem;
    }

    private static void addChildren(TreeItem<ComponentDTO> parentItem, List<ComponentXML> children) {
        for (ComponentXML child : children) {
            TreeItem<ComponentDTO> childItem = new TreeItem<>(new ComponentDTO(child));
            parentItem.getChildren().add(childItem);
            if (!child.getComponents().isEmpty()) {
                addChildren(childItem, child.getComponents());
            }
        }
    }

    /* --------------------------------------------------------------------- *
     * 7. JavaFX start() – TreeTableView setup
     * --------------------------------------------------------------------- */
    @Override
    public void start(Stage primaryStage) throws Exception {
        String xml = readBomXml();
        ComponentXML rootComponentXML = parseBomXml(xml);
        TreeItem<ComponentDTO> root = buildTree(rootComponentXML);
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

        treeTable.getColumns().addAll(Arrays.asList(
                colItem, colItemId, colLevel, colDesc, colRev,
                colUom, colQty, colType, colRef)
        );

        colItem.setCellFactory(tc -> new TreeTableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                // Get the ComponentDTO from the row
                TreeTableRow<ComponentDTO> row = getTableRow();
                if (row == null || row.getItem() == null) {
                    setText(item);
                    setStyle("");
                    return;
                }

                int level = row.getItem().levelProperty().get();
                String color;
                String fontWeight = level == 1 ? "bold" : "normal";

                color = switch (level) {
                    case 1 -> "#1976D2";
                    case 2 -> "#388E3C";
                    case 3 -> "#F57C00";
                    case 4 -> "#7B1FA2";
                    default -> "#000000"; // black for level 5+
                };

                setText(item);
                setStyle("-fx-font-weight: " + fontWeight + "; -fx-text-fill: " + color + ";");
            }
        });

        treeTable.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

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
            // using test XML
            String xml = readBomXml();
            // to parse
            ComponentXML root = parseBomXml(xml);

            System.out.println("=== BOM hierarchy (pretty printed) ===");
            root.prettyPrint(0);

            List<ComponentXML> flat = new ArrayList<>();
            flatten(root, flat);
            System.out.println("\n=== Flattened list size: " + flat.size() + " ===");

            // Launch JavaFX UI
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void flatten(ComponentXML c, List<ComponentXML> out) {
        out.add(c);
        for (ComponentXML child : c.getComponents()) {
            flatten(child, out);
        }
    }
}