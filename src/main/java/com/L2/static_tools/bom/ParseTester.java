package com.L2.static_tools.bom;

import com.L2.dto.bom.ComponentDTO;
import com.sun.jna.platform.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ParseTester {
    public static void main(String[] args) {
        List<ComponentDTO> roots;
        try {
            roots = BomParser.parse(readBomXml());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("=== FULL BOM HIERARCHY ===");
        System.out.println("Found " + roots.size() + " root components");

        for (int i = 0; i < roots.size(); i++) {
            System.out.println("\n--- ROOT " + (i + 1) + " ---");
            printComponent(roots.get(i), "");
        }
    }

    public static String readBomXml() throws IOException {
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream("bom.xml");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                throw new IOException("File not found in resources: bom.xml");
            }

            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private static void printComponent(ComponentDTO c, String indent) {
        String ref = c.getReferenceList();
        if (ref == null || ref.isEmpty()) ref = "No reference found";
        System.out.println(indent + c.getItem() + " [L" + c.getLevel() + "] " + c.getDescription() + " → " + ref);
        if (c.getChildren() != null) {
            for (ComponentDTO child : c.getChildren()) {
                printComponent(child, indent + "  ");
            }
        }
    }

}