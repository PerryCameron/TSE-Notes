package com.L2.static_tools.bom;


import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3c.dom.Node;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ParseTester – test harness for the BOM XML parser.
 */
public class ParseTester {

    /* --------------------------------------------------------------------- *
     * 1. POJO model – one class for every <component> node.
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
            // v is the DOM node that represents <refdeslist>
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
        // 1. Locate the <bomexploder_response> start tag
        int start = xml.indexOf("<bomexploder_response");
        if (start == -1) throw new IllegalArgumentException("No <bomexploder_response> found");
        int end = xml.lastIndexOf("</bomexploder_response>") + "</bomexploder_response>".length();
        String payload = xml.substring(start, end);

        // 2. JAXB context for the inner payload
        JAXBContext jc = JAXBContext.newInstance(BomWrapper.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        // 3. StAX reader – needed because the payload may contain default-ns attributes
        XMLInputFactory xif = XMLInputFactory.newFactory();
        StringReader sr = new StringReader(payload);
        XMLStreamReader xsr = xif.createXMLStreamReader(sr);

        // Move to the root element <bomexploder_response>
        xsr.nextTag();

        // 4. Unmarshal the first <component> (the level-1 root)
        //    The wrapper simply holds the list of top-level components.
        BomWrapper wrapper = unmarshaller.unmarshal(xsr, BomWrapper.class).getValue();
        if (wrapper.components.isEmpty()) {
            throw new IllegalStateException("No top-level component found");
        }
        return wrapper.components.get(0); // there is exactly one level-1 component
    }

    @XmlRootElement(name = "bomexploder_response")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BomWrapper {
        @XmlElement(name = "component")
        List<Component> components = new ArrayList<>();
    }

    /* --------------------------------------------------------------------- *
     * 4. The method you already use to read the file.
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
     * 5. Test entry point
     * --------------------------------------------------------------------- */
    public static void main(String[] args) throws Exception {
        String xml = readBomXml();
        Component root = parseBomXml(xml);

        System.out.println("=== BOM hierarchy (pretty printed) ===");
        root.prettyPrint(0);

        // If you need the whole tree as a List<Object> (flattened):
        List<Component> flat = new ArrayList<>();
        flatten(root, flat);
        System.out.println("\n=== Flattened list size: " + flat.size() + " ===");
    }

    private static void flatten(Component c, List<Component> out) {
        out.add(c);
        for (Component child : c.components) {
            flatten(child, out);
        }
    }
}

//
//import com.L2.dto.bom.ComponentDTO;
//import com.sun.jna.platform.FileUtils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ParseTester {
//    public static void main(String[] args) {
//        List<ComponentDTO> roots;
//        try {
//            roots = BomParser.parse(readBomXml());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        System.out.println("=== FULL BOM HIERARCHY ===");
//        System.out.println("Found " + roots.size() + " root components");
//
//        for (int i = 0; i < roots.size(); i++) {
//            System.out.println("\n--- ROOT " + (i + 1) + " ---");
//            printComponent(roots.get(i), "");
//        }
//    }
//
//    public static String readBomXml() throws IOException {
//        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream("bom.xml");
//             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
//
//            if (is == null) {
//                throw new IOException("File not found in resources: bom.xml");
//            }
//
//            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
//        }
//    }
//
//    private static void printComponent(ComponentDTO c, String indent) {
//        String ref = c.getReferenceList();
//        if (ref == null || ref.isEmpty()) ref = "No reference found";
//        System.out.println(indent + c.getItem() + " [L" + c.getLevel() + "] " + c.getDescription() + " → " + ref);
//        if (c.getChildren() != null) {
//            for (ComponentDTO child : c.getChildren()) {
//                printComponent(child, indent + "  ");
//            }
//        }
//    }
//
//}