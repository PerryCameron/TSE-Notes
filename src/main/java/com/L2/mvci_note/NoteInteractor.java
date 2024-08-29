package com.L2.mvci_note;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.PartDTO;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ClipboardUtils;
import com.L2.static_tools.FakeData;
import com.L2.static_tools.TableFormatter;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.L2.static_tools.ApplicationPaths.entitlementsFile;
import static com.L2.static_tools.ApplicationPaths.settingsDir;

public class NoteInteractor {

    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(NoteInteractor.class);

    public NoteInteractor(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public void loadEntitlements() {
        try {
            // Ensure the directory and file exist
            AppFileTools.createFileIfNotExists(settingsDir);
            // Load the entitlements
            ObservableList<EntitlementDTO> entitlements = AppFileTools.getEntitlements(entitlementsFile);
            if (entitlements != null) {
                noteModel.setEntitlements(entitlements);
                logger.info("Loaded entitlements: " + entitlements.size());
            } else {
                // arrayList is already initialized so really we do nothing but warn
                logger.warn("Entitlements file is empty or could not be read. Initializing with an empty list.");
            }
        } catch (IOException e) {
            logger.error("Failed to load entitlements: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFakeTestData() {
        CaseDTO caseDTO = FakeData.createFakeCase();
        noteModel.setCurrentNote(caseDTO);
    }

    public EntitlementDTO setCurrentEntitlement() {
        EntitlementDTO entitlementDTO = noteModel.getEntitlements().stream().filter(DTO -> DTO.getName()
                .equals(noteModel.getCurrentNote().getEntitlement())).findFirst().orElse(null);
        noteModel.setCurrentEntitlement(entitlementDTO);
        return entitlementDTO;
    }

    public String getStatus() {
        return noteModel.statusLabelProperty().get();
    }

    public void reportNumberOfPartOrders() {
        logger.info("Number of part orders changed to: {}", noteModel.getCurrentNote().getPartOrders().size());
    }

    public void copyPartOrder() {
        ClipboardUtils.copyHtmlToClipboard(buildPartOrderToHTML(), buildPartOrderToPlainText());
    }

    private String buildPartOrderToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        // Start the table and add headers
        stringBuilder.append("<table border=\"1\">");
        logger.info("Copying Part Order");
        if (!noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber().isEmpty()) {
            logger.info("Adding order: {}", noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber());
            stringBuilder.append("<tr><th colspan=\"3\" style=\"background-color: lightgrey;\">")
                    .append("Part Order: ")
                    .append(noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber())
                    .append("</th></tr>");
        }
        stringBuilder.append("<tr>")
                .append("<th>Part Number</th>")
                .append("<th>Description</th>")
                .append("<th>Qty</th>")
                .append("</tr>");
        // Loop through each PartDTO to add table rows
        noteModel.getCurrentNote().getSelectedPartOrder().getParts().forEach(partDTO -> {
            stringBuilder.append("<tr>")
                    .append("<td>").append(partDTO.getPartNumber()).append("</td>")
                    .append("<td>").append(partDTO.getPartDescription()).append("</td>")
                    .append("<td>").append(partDTO.getPartQuantity()).append("</td>")
                    .append("</tr>");
        });
        // Close the table
        stringBuilder.append("</table>");
        // Convert StringBuilder to String (if you need to use it as a String)
        return stringBuilder.toString();
    }

    private String buildPartOrderToPlainText() {
        ObservableList<PartDTO> parts = noteModel.getCurrentNote().getSelectedPartOrder().getParts();
        String orderNumber = noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber();
        return TableFormatter.buildPartsTableString(parts, orderNumber);
    }

    public void logPartOrderNumberChange() {
        logger.info("Part Order Number Changed to: {}", noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber());
    }

    public void loadUser() {
        // will eventually pull user off of hard disk
        noteModel.setUser(FakeData.createPerson());
        logger.info("Loading user: {}", noteModel.getUser().getSesa());
    }

    public void copyNameDate() {
        ClipboardUtils.copyHtmlToClipboard(buildNameDateToHTML(), buildNameDateToPlainText());
    }

    private String buildNameDateToPlainText() {
        return noteModel.getUser().getFullName() + "\t\t" + noteModel.formattedDate();
    }

    private String buildNameDateToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<table style=\"width: 100%; font-size: 13px; background-color: #F5F5F5;\" class=\"ql-table-blob\">")
                .append("<tbody><tr><td class=\"slds-cell-edit cellContainer\">")
                .append("<span class=\"slds-grid slds-grid--align-spread\">")
                .append("<a href=\"")
                .append(noteModel.getUser().getProfileLink())
                .append("\" target=\"_blank\" title=\"FirstName LastName\" class=\"slds-truncate outputLookupLink\">")
                .append(noteModel.getUser().getFullName())
                .append("</a>")
                .append("</span></td>")
                .append("<td class=\"slds-cell-edit cellContainer\">")
                .append("<span class=\"slds-grid slds-grid--align-spread\">")
                .append("<span class=\"slds-truncate uiOutputDateTime\">")
                .append(noteModel.formattedDate())
                .append("</span>")
                .append("</span></td></tr></tbody></table>");
        return stringBuilder.toString();
    }
}
