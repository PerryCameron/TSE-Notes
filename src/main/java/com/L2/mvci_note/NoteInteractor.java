package com.L2.mvci_note;

import com.L2.dto.*;
import com.L2.repository.implementations.EntitlementsRepositoryImpl;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.repository.implementations.PartOrderRepositoryImpl;
import com.L2.repository.implementations.UserRepositoryImpl;
import com.L2.static_tools.*;
import com.L2.widgetFx.DialogueFx;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Iterator;

public class NoteInteractor {

    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(NoteInteractor.class);
    private final NoteRepositoryImpl noteRepo;
    private final PartOrderRepositoryImpl partOrderRepo;
    private final UserRepositoryImpl userRepo;
    private final EntitlementsRepositoryImpl entitlementsRepo;

    public NoteInteractor(NoteModel noteModel) {

        this.noteModel = noteModel;
        this.noteRepo = new NoteRepositoryImpl();
        this.partOrderRepo = new PartOrderRepositoryImpl();
        this.userRepo = new UserRepositoryImpl();
        this.entitlementsRepo = new EntitlementsRepositoryImpl();
    }

    public void loadEntitlements() {
        // Load the entitlements
        ObservableList<EntitlementDTO> entitlements = FXCollections.observableArrayList(entitlementsRepo.getAllEntitlements());
        noteModel.setEntitlements(entitlements);
        logger.info("Loaded entitlements: {}", entitlements.size());
    }

    // loads notes on start-up
    public void loadNotes() {
        // create and set our bound note
        NoteDTO boundNote = new NoteDTO();
        noteModel.setBoundNote(boundNote);
        // get all the notes and load them to memory
        noteModel.getNotes().addAll(noteRepo.getPaginatedNotes(noteModel.getPageSize(), noteModel.getOffset()));
        // set notes to the direction we like
        noteModel.getNotes().sort(Comparator.comparing(NoteDTO::getTimestamp).reversed());
        // if starting up for first time create first empty note
        if (noteModel.getNotes().isEmpty()) {
            NoteDTO noteDTO = new NoteDTO(1, false);
            noteModel.getNotes().add(new NoteDTO(1, false));
            noteRepo.insertNote(noteDTO);
        }
        // set bound note to copy information from latest note
        boundNote.copyFrom(noteModel.getNotes().getFirst());
        // add part orders as needed
        checkAndLoadPartOrdersIfNeeded();
    }

    public void setActiveServiceContract() {
        EntitlementDTO entitlementDTO = noteModel.getEntitlements().stream().filter(DTO -> DTO.getName()
                .equals(noteModel.getBoundNote().getActiveServiceContract())).findFirst().orElse(null);
        noteModel.setCurrentEntitlement(entitlementDTO);
    }

    public String getStatus() {
        return noteModel.statusLabelProperty().get();
    }

    public void reportNumberOfPartOrders() {
        logger.info("Number of part orders changed to: {}", noteModel.getBoundNote().getPartOrders().size());
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    public void copyPartOrder() {
        ClipboardUtils.copyHtmlToClipboard(buildPartOrderToHTML(true), buildPartOrderToPlainText());
    }

    public void copyNameDate() {
        ClipboardUtils.copyHtmlToClipboard(buildNameDateToHTML(), buildNameDateToPlainText());
    }

    public void copyShippingInformation() {
        ClipboardUtils.copyHtmlToClipboard(shippingInformationToHTML(), shippingInformationToPlainText());
    }

    public void copyBasicInformation() {
        ClipboardUtils.copyHtmlToClipboard(basicInformationToHTML(), basicInformationToPlainText());
    }

    public void copySubject() {
        ClipboardUtils.copyHtmlToClipboard(subjectToPlainText(), subjectToPlainText());
    }

    public void copyAnswerToCustomer() {
        logger.info("Copying corrective action, flashing group B (FinalBox, Related, Part Orders, Name/Date");
        ClipboardUtils.copyHtmlToClipboard(answerToCustomerToHTML(), answerToCustomerToPlainText());
    }

    public void copyCustomerRequest() {
        logger.info("Copying customer request, flashing Group A (Name/Date, Basic Information, Issue, Part Orders and Shipping Information.");
        ClipboardUtils.copyHtmlToClipboard(customerRequestToHTML(), customerRequestToPlainText());
    }

    public void copyLoggedCall() {
        ClipboardUtils.copyHtmlToClipboard(customerRequestToHTML(), loggedCallToPlainText());
    }

    public String copyAllPartOrdersToPlainText() {
        if (noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
                noteModel.setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToPlainText());
            }
            return builder.toString();
        } else if (noteModel.getBoundNote().getPartOrders().size() == 1) {
            return buildPartOrderToPlainText();
        }
        return "";
    }

    public String copyAllPartOrdersToHTML(boolean includePOHeader) {
        if (noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
                noteModel.setSelectedPartOrder(partOrderDTO);
                builder.append(buildPartOrderToHTML(includePOHeader)).append("<br>");
            }
            return builder.toString();
        } else if (noteModel.getBoundNote().getPartOrders().size() == 1) {
            return buildPartOrderToHTML(includePOHeader);
        }
        return "";
    }

    private String buildPartOrderToHTML(boolean includePOHeader) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!noteModel.getSelectedPartOrder().getParts().isEmpty()) {
            if (includePOHeader) {
                stringBuilder.append("<b>Parts Ordered</b><br>");
            } else {
                stringBuilder.append("<b>Parts Needed</b><br>");
            }
            stringBuilder.append("<table border=\"1\">");
            logger.info("Adding order: {}", noteModel.getSelectedPartOrder().getOrderNumber());
            if (includePOHeader) {
                if (!noteModel.getSelectedPartOrder().getOrderNumber().isEmpty()) {
                    // Adjust colspan based on showType()
                    int colspan = noteModel.getSelectedPartOrder().showType() ? 4 : 3;
                    stringBuilder.append("<tr><th colspan=\"").append(colspan).append("\" style=\"background-color: lightgrey;\">")
                            .append("Part Order: ")
                            .append(noteModel.getSelectedPartOrder().getOrderNumber())
                            .append("</th></tr>");
                }
            }
            // Headers: Include "Line Type" column only if showType() is true
            stringBuilder.append("<tr>")
                    .append("<th>Part Number</th>");
            if (noteModel.getSelectedPartOrder().showType()) {
                stringBuilder.append("<th>Line Type</th>");
            }
            stringBuilder.append("<th>Description</th>")
                    .append("<th>Qty</th>")
                    .append("</tr>");


            // Loop through each PartDTO to add table rows
            noteModel.getSelectedPartOrder().getParts().forEach(partDTO -> {
                stringBuilder.append("<tr>")
                        .append("<td>").append(partDTO.getPartNumber()).append("</td>");
                // Add Line Type cell only if showType() is true
                if (noteModel.getSelectedPartOrder().showType()) {
                    stringBuilder.append("<td>").append(partDTO.getLineType()).append("</td>");
                }
                stringBuilder.append("<td>").append(partDTO.getPartDescription()).append("</td>")
                        .append("<td>").append(partDTO.getPartQuantity()).append("</td>")
                        .append("</tr>");
            });
            stringBuilder.append("</table>");
        }
        return stringBuilder.toString();
    }

    private String buildPartOrderToPlainText() {
        PartOrderDTO partOrderDTO = noteModel.getSelectedPartOrder();
        StringBuilder stringBuilder = new StringBuilder();
        ObservableList<PartDTO> parts = partOrderDTO.getParts();
        String orderNumber = noteModel.getSelectedPartOrder().getOrderNumber();
        if (orderNumber != null && !orderNumber.isEmpty()) {
            stringBuilder.append("Part Order: ").append(orderNumber).append("\r\n");
        }
        // Build the rows
        for (PartDTO part : parts) {
            stringBuilder.append(String.format("%-15s", part.getPartNumber()));
            int lineTypeLength = 0;
            if (partOrderDTO.showType()) {
                String lineType = part.getLineType();
                stringBuilder.append("(");
                stringBuilder.append(lineType);
                stringBuilder.append(") ");
                lineTypeLength = lineType.length() + 4;
            }
            int descriptionWidth = 80 - 15 - 10 - 2 - lineTypeLength;
            stringBuilder.append(String.format("%-" + descriptionWidth + "." + descriptionWidth + "s", part.getPartDescription()));
            stringBuilder.append("  Qty. ").append(part.getPartQuantity());
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }

    public void loadUser() {
        // will eventually pull user off of hard disk
        noteModel.setUser(userRepo.getUser());
        logger.info("Loading user: {}", noteModel.getUser().getSesa());
    }


    private String buildNameDateToPlainText() {
        return noteModel.getUser().getFullName() + "\t\t" + noteModel.getBoundNote().formattedDate();
    }

    private String buildNameDateToHTML() {
        return "<table style=\"width: 100%; font-size: 13px; background-color: #F5F5F5;\" class=\"ql-table-blob\">" +
                "<tbody><tr><td class=\"slds-cell-edit cellContainer\">" +
                "<span class=\"slds-grid slds-grid--align-spread\">" +
                "<a href=\"" +
                noteModel.getUser().getProfileLink() +
                "\" target=\"_blank\" title=\"FirstName LastName\" class=\"slds-truncate outputLookupLink\">" +
                noteModel.getUser().getFullName() +
                "</a>" +
                "</span></td>" +
                "<td class=\"slds-cell-edit cellContainer\">" +
                "<span class=\"slds-grid slds-grid--align-spread\">" +
                "<span class=\"slds-truncate uiOutputDateTime\">" +
                noteModel.getBoundNote().formattedDate() +
                "</span>" +
                "</span></td></tr></tbody></table>";
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
                .append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getContactName())).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getBoundNote().getContactEmail()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getBoundNote().getContactPhoneNumber()).append("<br><br>")
                .append("<b>Shipping Address</b>").append("<br>");
        if (!noteModel.getBoundNote().getInstalledAt().isEmpty())
            stringBuilder.append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getInstalledAt())).append("<br>")
                    .append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getStreet())).append("<br>")
                    .append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getCity())).append(" ")
                    .append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getState())).append(" ")
                    .append(noteModel.getBoundNote().getZip()).append("<br>")
                    .append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getCountry()));
        return stringBuilder.toString();
    }

    private String basicInformationToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Customer Provided Information").append("\r\n");
        stringBuilder.append("Case/WO # ");
        if (!noteModel.getBoundNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getCaseNumber());
            if (!noteModel.getBoundNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getBoundNote().getWorkOrder()).append("\r\n")
                .append("Model: ").append(noteModel.getBoundNote().getModelNumber()).append("\r\n")
                .append("S/N: ").append(noteModel.getBoundNote().getSerialNumber()).append("\r\n").append("\r\n")
                .append("--- Call-in person ---").append("\r\n")
                .append("Name: ").append(noteModel.getBoundNote().getCallInPerson()).append("\r\n")
                .append("Phone: ").append(noteModel.getBoundNote().getCallInPhoneNumber()).append("\r\n")
                .append("Email: ").append(noteModel.getBoundNote().getCallInEmail()).append("\r\n").append("\r\n")
                .append("Entitlement: ").append(noteModel.getBoundNote().getActiveServiceContract()).append("\r\n")
                .append("Scheduling Terms: ").append(noteModel.getBoundNote().getSchedulingTerms()).append("\r\n")
                .append("Service Level: ").append(noteModel.getBoundNote().getServiceLevel()).append("\r\n")
                .append("Status of the UPS: ").append(noteModel.getBoundNote().getUpsStatus()).append("\r\n")
                .append("Load Supported: ").append(convertBool(noteModel.getBoundNote().isLoadSupported())).append("\r\n");
        return stringBuilder.toString();
    }

    private String subjectToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        if(noteModel.getBoundNote().getModelNumber().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getTitle()).append("\r\n");
        } else {
            stringBuilder.append(noteModel.getBoundNote().getModelNumber()).append(" - ").append(noteModel.getBoundNote().getTitle()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private String convertBool(boolean convert) {
        if (convert) return "Yes";
        else return "No";
    }

    private String basicInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<strong>Customer Provided Information</strong><br>");
        stringBuilder.append("<span style=\"color: rgb(0, 101, 105);\">Case/WO # </span>");
        if (!noteModel.getBoundNote().getCaseNumber().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getCaseNumber());
            if (!noteModel.getBoundNote().getWorkOrder().isEmpty()) {
                stringBuilder.append(" / ");
            }
        }
        stringBuilder.append(noteModel.getBoundNote().getWorkOrder()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Model: </span>").append(noteModel.getBoundNote().getModelNumber()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">S/N: </span>").append(noteModel.getBoundNote().getSerialNumber()).append("<br><br>")
                .append("<b>Call-in person</b><br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(ClipboardUtils.escapeHtmlContent(noteModel.getBoundNote().getCallInPerson())).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getBoundNote().getCallInPhoneNumber()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Email: </span>").append(noteModel.getBoundNote().getCallInEmail()).append("<br>")
                .append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Entitlement: </span>").append(noteModel.getBoundNote().getActiveServiceContract()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Scheduling Terms: </span>").append(noteModel.getBoundNote().getSchedulingTerms()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Service Level: </span>").append(noteModel.getBoundNote().getServiceLevel()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Status of the UPS: </span>").append(noteModel.getBoundNote().getUpsStatus()).append("<br>")
                .append("<span style=\"color: rgb(0, 101, 105);\">Load Supported: </span>").append(convertBool(noteModel.getBoundNote().isLoadSupported())).append("<br>");
        return stringBuilder.toString();
    }

//    private String getCorrectiveActionText() {
//        // Retrieve the corrective action text
//        String correctiveActionText = noteModel.getBoundNote().getAdditionalCorrectiveActionText();
//        // Preprocess the text to escape special characters
//        String escapedText = ClipboardUtils.escapeHtmlContent(correctiveActionText);
//        // Replace line breaks with <br> for HTML formatting
//        escapedText = escapedText.replaceAll("\\r\\n|\\n|\\r", "<br>");
//        // Append the escaped text to the StringBuilder and return
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(escapedText);
//        stringBuilder.append("<br>");
//        return stringBuilder.toString();
//    }

    private String loggedCallToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        stringBuilder.append(basicInformationToPlainText()).append("\r\n");
        stringBuilder.append(issueToPlainText()).append("\r\n");
        if (!noteModel.getBoundNote().getPartOrders().isEmpty()) {
            stringBuilder.append("\r\n").append("--- Parts Needed ---").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        stringBuilder.append(shippingInformationToPlainText()).append("\r\n");
        if (!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getCreatedWorkOrder()).append("\r\n").append("\r\n");
        }
        if (!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getAdditionalCorrectiveActionText()).append("\r\n").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        if (!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getTex()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private String customerRequestToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        stringBuilder.append(basicInformationToPlainText()).append("\r\n");
        stringBuilder.append(issueToPlainText()).append("\r\n");
        if (!noteModel.getBoundNote().getPartOrders().isEmpty()) {
            stringBuilder.append("\r\n").append("--- Parts Needed ---").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        stringBuilder.append(shippingInformationToPlainText()).append("\r\n");
        return stringBuilder.toString();
    }

    private String customerRequestToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>").append("\r\n");
        stringBuilder.append(basicInformationToHTML()).append("<br>").append("\r\n");
        stringBuilder.append(issueToHTML()).append("<br>").append("\r\n");
        if (!noteModel.getBoundNote().getPartOrders().isEmpty()) {
            stringBuilder.append(copyAllPartOrdersToHTML(false)).append("<br>").append("\r\n");
        }
        stringBuilder.append(shippingInformationToHTML()).append("<br>").append("\r\n");
        return stringBuilder.toString();
    }

    public void copyIssue() {
        ClipboardUtils.copyHtmlToClipboard(issueToHTML(), issueToPlainText());
    }

    private String issueToPlainText() {
        return "--- Issue ---" + "\r\n" +
                noteModel.getBoundNote().getIssue();
    }

    private String issueToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<strong>Issue</strong><br>");
        // Retrieve the issue text
        String issueText = noteModel.getBoundNote().getIssue();
        // Preprocess the text to escape special characters
        String escapedText = ClipboardUtils.escapeHtmlContent(issueText);
        // Replace line breaks with <br> for HTML formatting
        escapedText = escapedText.replaceAll("\\r\\n|\\n|\\r", "<br>");
        stringBuilder.append(escapedText);
        stringBuilder.append("<br>");
        return stringBuilder.toString();
    }

    private String answerToCustomerToPlainText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToPlainText()).append("\r\n").append("\r\n");
        if (!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getCreatedWorkOrder()).append("\r\n").append("\r\n");
        }
        if (!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getAdditionalCorrectiveActionText()).append("\r\n").append("\r\n");
        }
        stringBuilder.append(copyAllPartOrdersToPlainText()).append("\r\n");
        if (!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getTex()).append("\r\n");
        }
        return stringBuilder.toString();
    }

    private String answerToCustomerToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>");
        if (!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getCreatedWorkOrder()).append("<br><br>");
        }
        if (!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(getCorrectiveActionText());
        }
        stringBuilder.append(copyAllPartOrdersToHTML(true)).append("<br>");
        if (!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ")
                    .append(noteModel.getBoundNote().getTex()).append("<br>");
        }
        return stringBuilder.toString();
    }

    private String getCorrectiveActionText() {
        // Retrieve the corrective action text
        String correctiveActionText = noteModel.getBoundNote().getAdditionalCorrectiveActionText();
        // Preprocess the text to escape special characters
        String escapedText = ClipboardUtils.escapeHtmlContent(correctiveActionText);
        // Replace line breaks with <br> for HTML formatting
        escapedText = escapedText.replaceAll("\\r\\n|\\n|\\r", "<br>");
        // Append the escaped text to the StringBuilder and return
        return escapedText + "<br>";
    }

    public void setComplete() {
        logger.info("Note {} has been set to completed", noteModel.getBoundNote().getId());
        noteModel.getBoundNote().setCompleted(true);
    }

    public void createNewNote() {
        // let's update the list note before moving on
        saveOrUpdateNote(); // I feel like this can go
        NoteDTO noteDTO = new NoteDTO(0, false);
        noteDTO.setId(noteRepo.insertNote(noteDTO));
        noteModel.getNotes().add(noteDTO);
        noteModel.getNotes().sort(Comparator.comparing(NoteDTO::getTimestamp).reversed());
        noteModel.getBoundNote().setId(noteDTO.getId());
        noteModel.clearBoundNoteFields();
        noteModel.openNoteTab();
    }

    public void cloneNote() {
        String answer = DialogueFx.showYesNoCancelDialog();
        if(!answer.equals("cancel")) {
            // create a new note
            NoteDTO noteDTO = new NoteDTO(0, false);
            // copy fields from bound note to our new note
            noteDTO.copyFrom(noteModel.getBoundNote());
            // set the timestamp on our new note
            noteDTO.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            // insert the note and get the id from the database
            noteDTO.setId(noteRepo.insertNote(noteDTO));
            // add cloned to the title
            noteDTO.setTitle("(cloned) " + noteDTO.getTitle());
            // update our note this is not redundant because title doesn't get inserted on creation
            noteRepo.updateNote(noteDTO);
            // add the note to our list of notes
            noteModel.getNotes().add(noteDTO);
            // sort our list so our new note floats to the top
            noteModel.getNotes().sort(Comparator.comparing(NoteDTO::getTimestamp).reversed());
            // let's make the bound note copy our new note
            noteModel.getBoundNote().copyFrom(noteDTO);
            // time to add part orders / parts if selected
            if(answer.equals("yes")) {
                cloneParts(noteDTO);
            }
            noteModel.getBoundNote().getPartOrders().clear();
            noteModel.refreshBoundNote();
            if(answer.equals("yes")) refreshPartOrders();
        } else {
            logger.info("Note {} cloning has been cancelled", noteModel.getBoundNote().getId());
        }
    }

    private void cloneParts(NoteDTO noteDTO) {
        int partOrderId;
        for(PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
            // create new part order object
            PartOrderDTO newPartOrderDTO = new PartOrderDTO(noteDTO.getId());
            // insert new part order and get id from it.
            partOrderId = partOrderRepo.insertPartOrder(newPartOrderDTO);
            // cycle through parts that were in the original part order
            for(PartDTO partDTO : partOrderDTO.getParts()) {
                PartDTO newPartDTO = new PartDTO(partOrderId, partDTO);
                partOrderRepo.insertPart(newPartDTO);
            }
        }
    }

    public void logCurrentEntitlement() {
        logger.info("Current entitlement set to: {}", noteModel.getCurrentEntitlement());
    }

    public void refreshPartOrders() {
        logger.debug("Refreshing bound note, and UI, setting to: {}", noteModel.getBoundNote().getId());
        checkAndLoadPartOrdersIfNeeded();
        setStatusLabelWithNoteInformation();
    }

    public void setStatusLabelWithNoteInformation() {
        noteModel.setStatusLabel("Note: " + noteModel.getBoundNote().getId() + "  " + noteModel.getBoundNote().formattedDate());
    }

    private void checkAndLoadPartOrdersIfNeeded() {
        NoteDTO noteDTO = noteModel.getBoundNote();
        if (noteDTO.getPartOrders().isEmpty()) {
            logger.debug("No Part orders found in memory, checking database..");
            noteDTO.setPartOrders(FXCollections.observableArrayList(partOrderRepo.findAllPartOrdersByNoteId(noteDTO.getId())));
            if (!noteModel.getBoundNote().getPartOrders().isEmpty()) {
                logger.debug("{} part orders loaded into memory", noteDTO.getPartOrders().size());
                noteModel.setSelectedPartOrder(noteDTO.getPartOrders().getFirst());
                getAllPartsForEachPartOrder();
            } else logger.debug("There are no part orders for this note");
        } else
            logger.debug("There are {} part orders already in memory", noteDTO.getPartOrders().size());
        noteModel.refreshBoundNote();
    }

    private void getAllPartsForEachPartOrder() {
        for (PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
            partOrderDTO.setParts(FXCollections.observableArrayList(partOrderRepo.getPartsByPartOrder(partOrderDTO)));
        }
    }

    // this synchronizes the bound object to the correct object in the list
    public void saveOrUpdateNote() {
        for (NoteDTO noteDTO : noteModel.getNotes()) {
            // let's find the correct note in the list
            if (noteDTO.getId() == noteModel.getBoundNote().getId()) {
                // compares bound note to matching list note, if there is a change it logs it and copies it.
                if (!NoteTools.notesAreTheSameAndSync(noteDTO, noteModel.getBoundNote())) {
                    // copies bound note to the note in the list with matching id
                    if (noteRepo.noteExists(noteDTO)) {
                        logger.debug("Updated note: {}", noteDTO.getId());
                        noteRepo.updateNote(noteDTO);
                    } else {
                        noteDTO.setId(noteRepo.insertNote(noteDTO));
                        logger.debug("Inserted note: {}", noteDTO.getId());
                    }
                } else logger.debug("No changes have been made to fields, ignoring call to save");
            }
        }
    }

    public void insertPartOrder() {
        int noteId = noteModel.getBoundNote().getId();
        PartOrderDTO partOrderDTO = new PartOrderDTO(0, noteId, "", false);
        partOrderDTO.setId(partOrderRepo.insertPartOrder(partOrderDTO));
        noteModel.getBoundNote().getPartOrders().add(partOrderDTO);
        noteModel.setSelectedPartOrder(noteModel.getBoundNote().getPartOrders().getLast());
    }

    public void updatePartOrder() {
        partOrderRepo.updatePartOrder(noteModel.getSelectedPartOrder());
    }

    public void deletePart() {
        PartDTO partDTO = noteModel.getSelectedPart();
        PartOrderDTO partOrderDTO = noteModel.getSelectedPartOrder();
//        System.out.println("Part to be deleted: " + partDTO.getId());
        partOrderRepo.deletePart(partDTO);
        noteModel.setSelectedPart(null);
        partOrderDTO.getParts().remove(partDTO);
    }

    public void insertPart() {
        PartDTO partDTO = new PartDTO(noteModel.getSelectedPartOrder().getId());
        partDTO.setId(partOrderRepo.insertPart(partDTO)); // TODO changes this when hooked to database
        noteModel.getSelectedPartOrder().getParts().add(partDTO);
    }

    public void updatePart() {
        PartDTO partDTO = noteModel.getSelectedPart();
        partOrderRepo.updatePart(partDTO);
        logger.debug("Updated part order: {} part # {}", partDTO.getId(), partDTO.getPartNumber());
    }

    public ObservableList<NoteDTO> getNotes() {
        return noteModel.getNotes();
    }

    public ObjectProperty<NoteDTO> getBoundNoteProperty() {
        return noteModel.boundNoteProperty();
    }

    public IntegerProperty getOffsetProperty() {
        return noteModel.offsetProperty();
    }

    public IntegerProperty getPageSizeProperty() {
        return noteModel.pageSizeProperty();
    }

    public void deleteNote() {
        int id = noteModel.getBoundNote().getId();
//        System.out.println("deleting note: " + id);
        // deletedNoteDTO will be the reference to the correct NoteDTO in the list
        NoteDTO deletedNoteDTO = null;
        for (NoteDTO noteDTO : noteModel.getNotes()) {
            if (noteDTO.idProperty().get() == id) {
                deletedNoteDTO = noteDTO;
            }
        }
        if (!noteModel.getBoundNote().getPartOrders().isEmpty()) {
            noteModel.getBoundNote().getPartOrders().forEach(this::deletePartOrder);
        }
        if(deletedNoteDTO != null)
            noteRepo.deleteNote(deletedNoteDTO);
        noteModel.getNotes().remove(deletedNoteDTO);
    }

    public void deleteSelectedPartOrder() {
        deletePartOrder(noteModel.getSelectedPartOrder());
    }

    public void deletePartOrder(PartOrderDTO partOrderDTO) {
        if (!partOrderDTO.getParts().isEmpty()) {
            Iterator<PartDTO> iterator = partOrderDTO.getParts().iterator();
            while (iterator.hasNext()) {
                PartDTO partDTO = iterator.next();
                partOrderRepo.deletePart(partDTO);  // Delete the note from repository
                iterator.remove();
            }
        }
        partOrderRepo.deletePartOrder(noteModel.getSelectedPartOrder());
    }

    public UserDTO getUser() {
        return noteModel.getUser();
    }

    public void refreshEntitlementComboBox() {
        noteModel.refreshEntitlements();
    }

    public NoteMessage checkButtonEnable() {
        if (noteModel.getBoundNote().getId() == noteModel.getNotes().getFirst().getId()) {
            return NoteMessage.DISABLE_NEXT_BUTTON;
        } else {
            return NoteMessage.ENABLE_NEXT_BUTTON;
        }
    }

}
