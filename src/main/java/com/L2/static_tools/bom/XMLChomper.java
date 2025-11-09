package com.L2.static_tools.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.widgetFx.DialogueFx;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XMLChomper {
    private static final Logger logger = LoggerFactory.getLogger(XMLChomper.class);

    public static Integer[] getStats(TreeItem<ComponentDTO> treeItemRoot) {
        Integer[] stats = new Integer[11]; // Indices 0-9 for levels 1-10; extra slot for safety
        Arrays.fill(stats, 0);
        countLevels(treeItemRoot, stats);
        return stats;
    }

    private static void countLevels(TreeItem<ComponentDTO> item, Integer[] stats) {
        if (item == null || item.getValue() == null) {
            return;
        }

        ComponentDTO dto = item.getValue();
        Integer level = dto.levelProperty().get();
        if (level != null && level >= 1 && level <= 10) {
            stats[level - 1]++;
        }

        for (TreeItem<ComponentDTO> child : item.getChildren()) {
            countLevels(child, stats);
        }
    }

    /* --------------------------------------------------------------------- *
     *    Adapter that turns <refdeslist><refdes>R1</refdes>…</refdeslist>
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

    /**
     * Parses a SOAP-wrapped BOM XML response and returns the root {@link ComponentXML}.
     * <p>
     * The input XML is expected to be a SOAP envelope containing a {@code <bomexploder_response>}
     * element. This method:
     * <ol>
     *   <li>Extracts the inner payload (strips SOAP envelope)</li>
     *   <li>Uses JAXB with StAX to unmarshal the {@code <bomexploder_response>} wrapper</li>
     *   <li>Returns the first (and only) top-level {@code <component>}</li>
     * </ol>
     * </p>
     *
     * @param xml      the full SOAP XML response as a String
     * @param bomModel
     * @return the root {@link ComponentXML} representing the top-level assembly
     * @throws Exception if parsing fails, envelope is missing, or no component is found
     */
    public static ComponentXML parseBomXml(String xml) throws Exception {


        // Step 1: Locate the start of the actual payload inside the SOAP envelope
        // The BOM data is wrapped in <bomexploder_response>...</bomexploder_response>
        int start = xml.indexOf("<bomexploder_response");
        if (start == -1) {
            throw new IllegalArgumentException("No <bomexploder_response> found in XML. Is this a valid BOM response?");
        }

        // Step 2: Find the end of the payload (closing tag)
        int end = xml.lastIndexOf("</bomexploder_response>") + "</bomexploder_response>".length();
        if (end <= start) {
            throw new IllegalArgumentException("Malformed </bomexploder_response> tag");
        }

        // Step 3: Extract just the inner payload (strip SOAP envelope)
        String payload = xml.substring(start, end);
        // Now 'payload' contains: <bomexploder_response><component>...</component></bomexploder_response>

        // Step 4: Create JAXB context for the wrapper class that holds the list of components
        // BomWrapper maps to <bomexploder_response> and contains List<ComponentXML>
        JAXBContext jc = JAXBContext.newInstance(BomWrapper.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        // Step 5: Use StAX (streaming) to parse the payload
        // Required because the XML may contain namespace-qualified attributes or mixed content
        XMLInputFactory xif = XMLInputFactory.newFactory();
        StringReader sr = new StringReader(payload);
        XMLStreamReader xsr = xif.createXMLStreamReader(sr);

        // Step 6: Move the cursor to the root element: <bomexploder_response>
        // nextTag() skips whitespace and moves to the first tag
        xsr.nextTag();

        // Step 7: Unmarshal the wrapper — JAXB reads from current position in stream
        // .getValue() extracts the actual BomWrapper object from JAXBElement wrapper
        BomWrapper wrapper = unmarshaller.unmarshal(xsr, BomWrapper.class).getValue();

        // Step 8: Validate that at least one top-level component exists
        if (wrapper.components.isEmpty()) {
            throw new IllegalStateException("BOM response contains no <component> at top level");
        }

        // Step 9: Return the root component (there should be exactly one at level 1)
        return wrapper.components.getFirst();
    }

    @XmlRootElement(name = "bomexploder_response")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class BomWrapper {
        @XmlElement(name = "component")
        List<ComponentXML> components = new ArrayList<>();
    }

    public static TreeItem<ComponentDTO> buildTree(ComponentXML root) {
        ComponentDTO component = new ComponentDTO(root);
        TreeItem<ComponentDTO> rootItem = new TreeItem<>(component);
        addChildren(rootItem, root.getComponents());
        return rootItem;
    }

    public static void addChildren(TreeItem<ComponentDTO> parentItem, List<ComponentXML> children) {
        for (ComponentXML child : children) {
            TreeItem<ComponentDTO> childItem = new TreeItem<>(new ComponentDTO(child));
            parentItem.getChildren().add(childItem);
            if (!child.getComponents().isEmpty()) {
                addChildren(childItem, child.getComponents());
            }
        }
    }

    public static void saveToBomXml(String content, Path filePath) {
        try {
            Files.writeString(filePath, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Platform.runLater(() -> DialogueFx.errorAlert("Can not write bom.XML", e.getMessage()));
        }
    }

    public static String readXMLFromFile(Path filePath) {
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Can not read bom.XML", e.getMessage());
            //Platform.runLater(() -> DialogueFx.errorAlert("Can not read bom.XML", e.getMessage()));
        }
        return "";
    }

    /**
     * Checks if the given XML string starts with "<env:Envelope" to verify if it's a return packet.
     *
     * @param xml The XML content as a string.
     * @return True if the string starts with "<env:Envelope", false otherwise.
     */
    public static boolean isReturnPacket(String xml) {
        return xml.startsWith("<env:Envelope");
    }

    /**
     * Checks if the substring "<component>" appears at least once in the given XML string.
     *
     * @param xml The XML content as a string.
     * @return True if "<component>" is found, false otherwise.
     */
    public static boolean hasComponent(String xml) {
        return xml.contains("<component>");
    }
}