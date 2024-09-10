package com.L2.mvci_note;

import com.L2.dto.NoteDTO;
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
import java.time.LocalDateTime;

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
        NoteDTO noteDTO = FakeData.createFakeCase();
        noteModel.getNotes().add(noteDTO);
        noteModel.setBoundNote(noteDTO);
    }

    public EntitlementDTO setCurrentEntitlement() {
        EntitlementDTO entitlementDTO = noteModel.getEntitlements().stream().filter(DTO -> DTO.getName()
                .equals(noteModel.getBoundNote().getEntitlement())).findFirst().orElse(null);
        noteModel.setCurrentEntitlement(entitlementDTO);
        return entitlementDTO;
    }

    public String getStatus() {
        return noteModel.statusLabelProperty().get();
    }

    public void reportNumberOfPartOrders() {
        logger.info("Number of part orders changed to: {}", noteModel.getBoundNote().getPartOrders().size());
    }

    public void copyPartOrder() {
            ClipboardUtils.copyHtmlToClipboard(buildPartOrderToHTML(), buildPartOrderToPlainText());
    }

    public String copyAllPartOrdersToPlainText() {
        if(noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
                noteModel.getBoundNote().setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToPlainText());
            }
            return builder.toString();
        } else if (noteModel.getBoundNote().getPartOrders().size() == 1) {
            return buildPartOrderToPlainText();
        }
        return "";
    }

    public String copyAllPartOrdersToHTML() {
        if(noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for(PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
                noteModel.getBoundNote().setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToHTML()).append("<br>");
            }
            return builder.toString();
        } else if (noteModel.getBoundNote().getPartOrders().size() == 1) {
            return buildPartOrderToHTML();
        }
        return "";
    }

    private String buildPartOrderToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        // Start the table and add headers
        logger.info("Copying Part Order");
        if (!noteModel.getBoundNote().getSelectedPartOrder().getParts().isEmpty()) {
            stringBuilder.append("<b>Parts Needed</b><br>");
            stringBuilder.append("<table border=\"1\">");
            logger.info("Adding order: {}", noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber());
            if(!noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber().isEmpty()) {
            stringBuilder.append("<tr><th colspan=\"3\" style=\"background-color: lightgrey;\">")
                    .append("Part Order: ")
                    .append(noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber())
                    .append("</th></tr>");
            }
            stringBuilder.append("<tr>")
                    .append("<th>Part Number</th>")
                    .append("<th>Description</th>")
                    .append("<th>Qty</th>")
                    .append("</tr>");
            // Loop through each PartDTO to add table rows
            noteModel.getBoundNote().getSelectedPartOrder().getParts().forEach(partDTO -> {
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
        ObservableList<PartDTO> parts = noteModel.getBoundNote().getSelectedPartOrder().getParts();
        String orderNumber = noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber();
        return TableFormatter.buildPartsTableString(parts, orderNumber);
    }

    public void logPartOrderNumberChange() {
        logger.info("Part Order Number Changed to: {}", noteModel.getBoundNote().getSelectedPartOrder());
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
        return noteModel.getUser().getFullName() + "\t\t" + noteModel.getBoundNote().formattedDate();
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
                .append(noteModel.getBoundNote().formattedDate())
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
        stringBuilder.append("Name: ").append(noteModel.getBoundNote().getContactName()).append("\r\n");
        stringBuilder.append("Email: ").append(noteModel.getBoundNote().getContactEmail()).append("\r\n");
        stringBuilder.append("Phone: ").append(noteModel.getBoundNote().getContactPhoneNumber())
                .append("\r\n").append("\r\n");
        stringBuilder
                .append("--- Shipping Address ---").append("\r\n");
        if (!noteModel.getBoundNote().getInstalledAt().isEmpty())
            stringBuilder.append(noteModel.getBoundNote().getInstalledAt()).append("\r\n");
        stringBuilder.append(noteModel.getBoundNote().getStreet()).append("\r\n")
                .append(noteModel.getBoundNote().getCity()).append(" ")
                .append(noteModel.getBoundNote().getState()).append(" ")
                .append(noteModel.getBoundNote().getZip()).append("\r\n")
                .append(noteModel.getBoundNote().getCountry()).append("\r\n");
        return stringBuilder.toString();
    }

    private String shippingInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<b>Shipping Contact</b><br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(noteModel.getBoundNote().getContactName()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getBoundNote().getContactEmail()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getBoundNote().getContactPhoneNumber()).append("<br><br>");
        stringBuilder.append("<b>Shipping Address</b>").append("<br>");
        if (!noteModel.getBoundNote().getInstalledAt().isEmpty())
            stringBuilder.append(noteModel.getBoundNote().getInstalledAt()).append("<br>");
        stringBuilder
                .append(noteModel.getBoundNote().getStreet()).append("<br>")
                .append(noteModel.getBoundNote().getCity()).append(" ")
                .append(noteModel.getBoundNote().getState()).append(" ")
                .append(noteModel.getBoundNote().getZip()).append("<br>")
                .append(noteModel.getBoundNote().getCountry());
        return stringBuilder.toString();
    }

    public void copyBasicInformation() {
        ClipboardUtils.copyHtmlToClipboard(basicInformationToHTML(), basicInformationToPlainText());
    }

    private String basicInformationToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Case/WO # ");
        if(!noteModel.getBoundNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getCaseNumber());
            if(!noteModel.getBoundNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getBoundNote().getWorkOrder()).append("\r\n");
        stringBuilder.append("Model: ").append(noteModel.getBoundNote().getModelNumber()).append("\r\n");
        stringBuilder.append("S/N: ").append(noteModel.getBoundNote().getSerialNumber()).append("\r\n").append("\r\n");
        stringBuilder.append("--- Call-in person ---").append("\r\n");
        stringBuilder.append("Name: ").append(noteModel.getBoundNote().getCallInPerson()).append("\r\n");
        stringBuilder.append("Phone: ").append(noteModel.getBoundNote().getCallInPhoneNumber()).append("\r\n");
        stringBuilder.append("Email: ").append(noteModel.getBoundNote().getCallInEmail()).append("\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append("Entitlement: ").append(noteModel.getBoundNote().getEntitlement()).append("\r\n");
        stringBuilder.append("Scheduling Terms: ").append(noteModel.getBoundNote().getSchedulingTerms()).append("\r\n");
        stringBuilder.append("Service Level: ").append(noteModel.getBoundNote().getServiceLevel()).append("\r\n");
        stringBuilder.append("Status of the UPS: ").append(noteModel.getBoundNote().getUpsStatus()).append("\r\n");
        stringBuilder.append("Load Supported: ").append(convertBool(noteModel.getBoundNote().isLoadSupported())).append("\r\n");
        return stringBuilder.toString();
    }

    private String convertBool(boolean convert) {
        if(convert) return "Yes";
        else return "No";
    }

    private String basicInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Case/WO # </span>");
        if(!noteModel.getBoundNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getCaseNumber());
            if(!noteModel.getBoundNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getBoundNote().getWorkOrder()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Model: </span>").append(noteModel.getBoundNote().getModelNumber()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">S/N: </span>").append(noteModel.getBoundNote().getSerialNumber()).append("<br><br>");
        stringBuilder.append("<b>Call-in person</b><br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(noteModel.getBoundNote().getCallInPerson()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getBoundNote().getCallInPhoneNumber()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getBoundNote().getCallInEmail()).append("<br>");
        stringBuilder.append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Entitlement: </span>").append(noteModel.getBoundNote().getEntitlement()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Service Level: </span>").append(noteModel.getBoundNote().getSchedulingTerms()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Service Level: </span>").append(noteModel.getBoundNote().getServiceLevel()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Status of the UPS: </span>").append(noteModel.getBoundNote().getUpsStatus()).append("<br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Load Supported: </span>").append(convertBool(noteModel.getBoundNote().isLoadSupported())).append("<br>");
        return stringBuilder.toString();
    }

    public void copyCustomerRequest() {
        logger.info("Copying customer request, flashing Group A (Name/Date, Basic Information, Issue, Part Orders and Shipping Information.");
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
        stringBuilder.append(noteModel.getBoundNote().getIssue());
        return stringBuilder.toString();
    }

    private String issueToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<strong>Issue</strong>").append("<br>");
        stringBuilder.append(noteModel.getBoundNote().getIssue()).append("<br>");
        return stringBuilder.toString();
    }

    public void copyCorrectiveAction() {
        logger.info("Copying corrective action, flashing group B (FinalBox, Related, Part Orders, Name/Date");
        ClipboardUtils.copyHtmlToClipboard(correctiveActionToHTML(), correctiveActionToPlainText());
    }

    private String correctiveActionToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        if(!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getBoundNote().getCreatedWorkOrder()).append("\r\n").append("\r\n");
        }
        if(!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getAdditionalCorrectiveActionText()).append("\r\n").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        if(!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getBoundNote().getTex()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private String correctiveActionToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>");
        if(!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getBoundNote().getCreatedWorkOrder()).append("<br><br>");
        }
        if(!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getAdditionalCorrectiveActionText()).append("<br>").append("<br>");
        }
        stringBuilder.append(copyAllPartOrdersToHTML()).append("<br>");
        if(!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ");
            stringBuilder.append(noteModel.getBoundNote().getTex()).append("<br>");
        }
        return stringBuilder.toString();
    }

    public void setComplete() {
        logger.info("Note {} has been set to completed", noteModel.getBoundNote().getId() );
        noteModel.getBoundNote().setCompleted(true);
    }

    public void createNewNote() {
        saveBoundNote();
        noteModel.getBoundNote().getPartOrders().clear();
        noteModel.getBoundNote().setSelectedPartOrder(null);
        noteModel.getBoundNote().setTimestamp(LocalDateTime.now());
        noteModel.getBoundNote().clear();
        noteModel.setClearCalled(true);
        noteModel.setClearCalled(false);
        int noteNumber = noteModel.getNotes().size() + 1;  // temp way to make an ID
        noteModel.getBoundNote().setId(noteNumber);
        noteModel.getNotes().add(noteModel.getBoundNote().cloneCase(noteNumber));
        logger.info("Created a new note with id: {}", noteModel.getBoundNote().getId());
        logger.info("There are now {} notes in memory", noteModel.getNotes().size() );
    }

    private void saveBoundNote() {
        int noteNumber = noteModel.getNotes().size() + 1;  // temp way to make an ID
        // get note if it exists
        NoteDTO noteDTO = noteModel.getNotes()
                .stream()
                .filter(note -> note.getId() == noteModel.getBoundNote().getId())  // Filter notes
                .findFirst()
                .orElse(null);  // Return null if no matching element is found
        // if it does exist update it
        if(noteDTO != null) {
            noteModel.getBoundNote().updateCase(noteDTO);
            logger.info("Updated a note currently in memory");
        // if it doesn't exist create it
        } else {
            logger.info("Note in memory does not exist creating a new one");
            NoteDTO clonedCase = noteModel.getBoundNote().cloneCase(noteNumber);
            noteModel.setCurrentEntitlement(noteModel.getEntitlements().getLast());
            noteModel.getNotes().add(clonedCase);
            logger.info("Created new note {}", noteNumber);
        }
    }

    public void logCurrentEntitlement() {
        logger.info("Current entitlement set to: {}", noteModel.getCurrentEntitlement());
    }
}
