package com.L2.mvci.bom;

import com.L2.dto.bom.ComponentDTO;
import com.L2.dto.bom.ComponentXML;
import com.L2.static_tools.ApplicationPaths;
import com.L2.static_tools.bom.BOMExploderClient;
import com.L2.static_tools.bom.XMLChomper;
import com.L2.widgetFx.DialogueFx;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BomInteractor {
    private static final Logger logger = LoggerFactory.getLogger(BomInteractor.class);
    private final BomModel bomModel;

    public BomInteractor(BomModel bomModel) {
        this.bomModel = bomModel;
    }

    public void setRoot(TreeItem<ComponentDTO> treeItemRoot) {
        bomModel.getTreeTable().setRoot(treeItemRoot);
    }

    // we don't need a parameter because when this is called it pulls the terms strait from the model
    public void searchTree() {
        System.out.println("Searching Tree...");

        if (bomModel.getSearchInBom() == null || bomModel.getSearchInBom().trim().isEmpty()) {
            System.out.println("No search terms provided.");
            return;
        }

        if (bomModel.getRoot() == null) {
            System.out.println("Root is null.");
            return;
        }

        String[] terms = bomModel.getSearchInBom().split("\\s+");
        System.out.println("Terms: ");
        Arrays.stream(terms).forEach(System.out::println);

        List<Pair<ComponentDTO, Integer>> matches = new ArrayList<>();
        int nodeCount = collectMatches(bomModel.getRoot(), terms, matches);

        System.out.println("Visited " + nodeCount + " nodes.");

        // Sort by hits descending
        matches.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Extract results
        List<ComponentDTO> results = new ArrayList<>();
        for (Pair<ComponentDTO, Integer> pair : matches) {
            results.add(pair.getKey());
        }

        // Print to console
        for (ComponentDTO dto : results) {
            System.out.println("Item: " + dto.itemProperty().get() + ", Description: "
                    + dto.descriptionProperty().get() + ", RefDes: " + dto.refDesProperty().get());
        }
        System.out.println("Found " + results.size() + " matches.");
    }

    private int collectMatches(TreeItem<ComponentDTO> node, String[] terms, List<Pair<ComponentDTO, Integer>> matches) {
        if (node == null) {
            return 0;
        }

        int count = 0;
        ComponentDTO dto = node.getValue();
        if (dto != null) {
            count++;
            String itemStr = (dto.itemProperty().get() != null ? dto.itemProperty().get().toLowerCase() : "");
            String descStr = (dto.descriptionProperty().get() != null ? dto.descriptionProperty().get().toLowerCase() : "");
            String refStr = (dto.refDesProperty().get() != null ? dto.refDesProperty().get().toLowerCase() : "");

            // Debug print for each DTO
            System.out.println("Checking DTO - Item: " + itemStr + ", Desc: " + descStr + ", Ref: " + refStr);

            int hits = 0;
            for (String term : terms) {
                String t = term.toLowerCase();
                if (itemStr.contains(t) || descStr.contains(t) || refStr.contains(t)) {
                    hits++;
                }
            }

            if (hits > 0) {
                matches.add(new Pair<>(dto, hits));
            }
        }

        for (TreeItem<ComponentDTO> child : node.getChildren()) {
            count += collectMatches(child, terms, matches);
        }
        return count;
    }

    public TreeItem<ComponentDTO> buildTreeRoot(boolean firstStart) {
        logger.info("Searching bom for {}", bomModel.searchComponentProperty().get());
        String output;
        try {
            if (firstStart) {
                output = XMLChomper.readXMLFromFile(ApplicationPaths.secondaryDbDirectory.resolve("bom.XML"));
                if (output.isEmpty()) return null;
            } else {
                // search for component typed in text field
                output = BOMExploderClient.getBOMExplosionAsString(bomModel.searchComponentProperty().get(), "BIL", "");
            }
            // check to make sure we have received valid XML
            if(!XMLChomper.isReturnPacket(output)) {
                Platform.runLater(() -> DialogueFx.errorAlert("Unable to communicate with server", "Are you sure you are on the VPN?"));
                return null;
            }
            // check to make sure our packet has components
            if(!XMLChomper.hasComponent(output)) {
                Platform.runLater(() -> DialogueFx.errorAlert("Unable to retrieve part", "The part number can not be found, are you sure you typed it correctly?"));
                return null;
            }
            // save XML if we get this far
            if(!firstStart) {
                // save XML in text file for persistence
                XMLChomper.saveToBomXml(output, ApplicationPaths.secondaryDbDirectory.resolve("bom.XML"));
            }
            // create an XML component, with nested Lists
            ComponentXML xmlRoot = XMLChomper.parseBomXml(output);
            // create the root tree Item from the XML Component object
            TreeItem<ComponentDTO> treeItemRoot = XMLChomper.buildTree(xmlRoot);
            // attach root to model for use in search
            bomModel.setRoot(treeItemRoot);
            // Get Statistics
            Integer[] levels = XMLChomper.getStats(treeItemRoot);
            // this changes the UI after successfully putting ip into a treeView
            Platform.runLater(() -> bomModel.setLevels(levels));
            // set expanded
            treeItemRoot.setExpanded(true);
            return treeItemRoot;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> DialogueFx.errorAlert("Unable to get BOM", e.getMessage()));
        }
        return null;
    }
}
