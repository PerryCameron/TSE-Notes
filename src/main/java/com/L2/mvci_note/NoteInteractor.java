package com.L2.mvci_note;

import com.L2.dto.CaseDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
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

    public String copyAllPartOrdersToPlainText() {
        System.out.println("copyAllPartOrders() to plain text");
        if(noteModel.getCurrentNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(PartOrderDTO partOrderDTO : noteModel.getCurrentNote().getPartOrders()) {
                noteModel.getCurrentNote().setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToPlainText());
            }
            return builder.toString();
        } else if (noteModel.getCurrentNote().getPartOrders().size() == 1) {
            return buildPartOrderToPlainText();
        }
        return "";
    }

    public String copyAllPartOrdersToHTML() {
        if(noteModel.getCurrentNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(PartOrderDTO partOrderDTO : noteModel.getCurrentNote().getPartOrders()) {
                noteModel.getCurrentNote().setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToHTML()).append("<br>");
            }
            return builder.toString();
        } else if (noteModel.getCurrentNote().getPartOrders().size() == 1) {
            return buildPartOrderToHTML();
        }
        return "";
    }

    private String buildPartOrderToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        // Start the table and add headers
        logger.info("Copying Part Order");
        if (!noteModel.getCurrentNote().getSelectedPartOrder().getParts().isEmpty()) {
            stringBuilder.append("<b>Parts Needed</b><br>");
            stringBuilder.append("<table border=\"1\">");
            logger.info("Adding order: {}", noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber());
            if(!noteModel.getCurrentNote().getSelectedPartOrder().getOrderNumber().isEmpty()) {
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
        }
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

    public void copyShippingInformation() {
        ClipboardUtils.copyHtmlToClipboard(shippingInformationToHTML(), shippingInformationToPlainText());
    }

    private String shippingInformationToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--- Shipping Contact ---").append("\r\n");
        stringBuilder.append("Name: ").append(noteModel.getCurrentNote().getContactName()).append("\r\n");
        stringBuilder.append("Email: ").append(noteModel.getCurrentNote().getContactEmail()).append("\r\n");
        stringBuilder.append("Phone: ").append(noteModel.getCurrentNote().getContactPhoneNumber())
                .append("\r\n").append("\r\n");
        stringBuilder
                .append("--- Shipping Address ---").append("\r\n");
        if (!noteModel.getCurrentNote().getInstalledAt().isEmpty())
            stringBuilder.append(noteModel.getCurrentNote().getInstalledAt()).append("\r\n");
        stringBuilder.append(noteModel.getCurrentNote().getStreet()).append("\r\n")
                .append(noteModel.getCurrentNote().getCity()).append(" ")
                .append(noteModel.getCurrentNote().getState()).append(" ")
                .append(noteModel.getCurrentNote().getZip()).append("\r\n")
                .append(noteModel.getCurrentNote().getCountry()).append("\r\n");
        return stringBuilder.toString();
    }

    private String shippingInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<b>Shipping Contact</b><br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(noteModel.getCurrentNote().getContactName()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getCurrentNote().getContactEmail()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getCurrentNote().getContactPhoneNumber()).append("<br><br>");
        stringBuilder.append("<b>Shipping Address</b>").append("<br>");
        if (!noteModel.getCurrentNote().getInstalledAt().isEmpty())
            stringBuilder.append(noteModel.getCurrentNote().getInstalledAt()).append("<br>");
        stringBuilder
                .append(noteModel.getCurrentNote().getStreet()).append("<br>")
                .append(noteModel.getCurrentNote().getCity()).append(" ")
                .append(noteModel.getCurrentNote().getState()).append(" ")
                .append(noteModel.getCurrentNote().getZip()).append("<br>")
                .append(noteModel.getCurrentNote().getCountry());
        return stringBuilder.toString();
    }

    public void copyBasicInformation() {
        ClipboardUtils.copyHtmlToClipboard(basicInformationToHTML(), basicInformationToPlainText());
    }

    private String basicInformationToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Case/WO # ");
        if(!noteModel.getCurrentNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getCurrentNote().getCaseNumber());
            if(!noteModel.getCurrentNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getCurrentNote().getWorkOrder()).append("\r\n");
        stringBuilder.append("Model: ").append(noteModel.getCurrentNote().getModelNumber()).append("\r\n");
        stringBuilder.append("S/N: ").append(noteModel.getCurrentNote().getSerialNumber()).append("\r\n").append("\r\n");
        stringBuilder.append("--- Call-in person ---").append("\r\n");
        stringBuilder.append("Name: ").append(noteModel.getCurrentNote().getCallInPerson()).append("\r\n");
        stringBuilder.append("Phone: ").append(noteModel.getCurrentNote().getCallInPhoneNumber()).append("\r\n");
        stringBuilder.append("Email: ").append(noteModel.getCurrentNote().getCallInEmail()).append("\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("Entitlement: ").append(noteModel.getCurrentNote().getEntitlement()).append("\r\n");
        stringBuilder.append("Scheduling Terms: ").append(noteModel.getCurrentNote().getSchedulingTerms()).append("\r\n");
        stringBuilder.append("Service Level: ").append(noteModel.getCurrentNote().getServiceLevel()).append("\r\n");
        stringBuilder.append("Status of the UPS: ").append(noteModel.getCurrentNote().getUpsStatus()).append("\r\n");
        stringBuilder.append("Load Supported: ").append(convertBool(noteModel.getCurrentNote().isLoadSupported())).append("\r\n");
        return stringBuilder.toString();
    }

    private String convertBool(boolean convert) {
        if(convert) return "Yes";
        else return "No";
    }

    private String basicInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Case/WO # </span>");
        if(!noteModel.getCurrentNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getCurrentNote().getCaseNumber());
            if(!noteModel.getCurrentNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getCurrentNote().getWorkOrder()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Model: </span>").append(noteModel.getCurrentNote().getModelNumber()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">S/N: </span>").append(noteModel.getCurrentNote().getSerialNumber()).append("<br><br>");
        stringBuilder.append("<b>Call-in person</b><br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(noteModel.getCurrentNote().getCallInPerson()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getCurrentNote().getCallInPhoneNumber()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getCurrentNote().getCallInEmail()).append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Entitlement: </span>").append(noteModel.getCurrentNote().getEntitlement()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Service Level: </span>").append(noteModel.getCurrentNote().getSchedulingTerms()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Service Level: </span>").append(noteModel.getCurrentNote().getServiceLevel()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Status of the UPS: </span>").append(noteModel.getCurrentNote().getUpsStatus()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Load Supported: </span>").append(convertBool(noteModel.getCurrentNote().isLoadSupported())).append("<br>");
        return stringBuilder.toString();
    }

    public void copyCustomerRequest() {
        ClipboardUtils.copyHtmlToClipboard(customerRequestToHTML(), customerRequestToPlainText());
    }

    private String customerRequestToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        stringBuilder.append(basicInformationToPlainText()).append("\r\n");
        stringBuilder.append(issueToPlainText()).append("\r\n");
        stringBuilder.append("--- Parts Needed ---").append("\r\n");
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        stringBuilder.append(shippingInformationToPlainText()).append("\r\n");
        return stringBuilder.toString();
    }

    private String customerRequestToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>").append("\r\n");
        stringBuilder.append(basicInformationToHTML()).append("<br>").append("\r\n");
        stringBuilder.append(issueToHTML()).append("<br>").append("\r\n");
        stringBuilder.append(copyAllPartOrdersToHTML()).append("<br>");
        stringBuilder.append(shippingInformationToHTML()).append("<br>").append("\r\n");
        return stringBuilder.toString();
    }

    public void copyIssue() {
        ClipboardUtils.copyHtmlToClipboard(issueToHTML(), issueToPlainText());
    }

    private String issueToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--- Issue ---").append("\r\n");
        stringBuilder.append(noteModel.getCurrentNote().getIssue());
        return stringBuilder.toString();
    }

    private String issueToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<strong>Issue</strong>").append("<br>");
        stringBuilder.append(noteModel.getCurrentNote().getIssue()).append("<br>");
        return stringBuilder.toString();
    }

    public void copyCorrectiveAction() {
        logger.info("Copying corrective action");
        ClipboardUtils.copyHtmlToClipboard(correctiveActionToHTML(), correctiveActionToPlainText());
    }

    private String correctiveActionToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        if(!noteModel.getCurrentNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getCurrentNote().getCreatedWorkOrder()).append("\r\n").append("\r\n");
        }
        if(!noteModel.getCurrentNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getCurrentNote().getAdditionalCorrectiveActionText()).append("\r\n").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        if(!noteModel.getCurrentNote().getTex().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getCurrentNote().getTex()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private String correctiveActionToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>");
        if(!noteModel.getCurrentNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getCurrentNote().getCreatedWorkOrder()).append("<br><br>");
        }
        if(!noteModel.getCurrentNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getCurrentNote().getAdditionalCorrectiveActionText()).append("<br>").append("<br>");
        }
        stringBuilder.append(copyAllPartOrdersToHTML()).append("<br>");
        if(!noteModel.getCurrentNote().getTex().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getCurrentNote().getTex()).append("<br>");
        }
        return stringBuilder.toString();
    }

    public void setComplete() {
        logger.info("Note {} has been set to completed", noteModel.getCurrentNote().getId() );
        noteModel.getCurrentNote().setCompleted(true);
    }

    public void createNewNote() {
        int noteNumber = noteModel.getCurrentNote().getId() + 1;
        noteModel.setCurrentEntitlement(noteModel.getEntitlements().getLast());
        noteModel.setCurrentNote(new CaseDTO(noteNumber, false));
        logger.info("Created new note {}", noteNumber);
    }
}
