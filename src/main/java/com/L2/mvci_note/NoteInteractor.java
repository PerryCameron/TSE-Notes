package com.L2.mvci_note;

import com.L2.dto.NoteDTO;
import com.L2.dto.EntitlementDTO;
import com.L2.dto.PartDTO;
import com.L2.dto.PartOrderDTO;
import com.L2.repository.implementations.NoteRepositoryImpl;
import com.L2.repository.implementations.PartOrderRepositoryImpl;
import com.L2.static_tools.AppFileTools;
import com.L2.static_tools.ClipboardUtils;
import com.L2.static_tools.FakeData;
import com.L2.static_tools.TableFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;

import static com.L2.static_tools.ApplicationPaths.entitlementsFile;
import static com.L2.static_tools.ApplicationPaths.settingsDir;

public class NoteInteractor {

    private final NoteModel noteModel;
    private static final Logger logger = LoggerFactory.getLogger(NoteInteractor.class);
    private final NoteRepositoryImpl noteRepo;
    private final PartOrderRepositoryImpl partOrderRepo;

    public NoteInteractor(NoteModel noteModel) {

        this.noteModel = noteModel;
        this.noteRepo = new NoteRepositoryImpl();
        this.partOrderRepo = new PartOrderRepositoryImpl();
    }

    public void loadEntitlements() {
        try {
            // Ensure the directory and file exist
            AppFileTools.createFileIfNotExists(settingsDir);
            // Load the entitlements
            ObservableList<EntitlementDTO> entitlements = AppFileTools.getEntitlements(entitlementsFile);
            noteModel.setEntitlements(entitlements);
            logger.info("Loaded entitlements: {}", entitlements.size());
        } catch (IOException e) {
            logger.error("Failed to load entitlements: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadNotes() {
        NoteDTO boundNote = new NoteDTO();
        noteModel.setBoundNote(boundNote);
        noteModel.getNotes().addAll(noteRepo.getAllNotes());
        noteModel.getNotes().sort(Comparator.comparing(NoteDTO::getTimestamp));
        boundNote.copyFrom(noteModel.getNotes().getLast());
        checkAndLoadPartOrdersIfNeeded();
    }

    public void setActiveServieContract() {
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

    public void copyPartOrder() {
        ClipboardUtils.copyHtmlToClipboard(buildPartOrderToHTML(), buildPartOrderToPlainText());
    }

    public String copyAllPartOrdersToPlainText() {
        if (noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
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
        if (noteModel.getBoundNote().getPartOrders().size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
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
            stringBuilder.append("<b>Parts Needed</b><br>")
            .append("<table border=\"1\">");
            logger.info("Adding order: {}", noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber());
            if (!noteModel.getBoundNote().getSelectedPartOrder().getOrderNumber().isEmpty()) {
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
            noteModel.getBoundNote().getSelectedPartOrder().getParts().forEach(partDTO -> stringBuilder.append("<tr>")
                    .append("<td>").append(partDTO.getPartNumber()).append("</td>")
                    .append("<td>").append(partDTO.getPartDescription()).append("</td>")
                    .append("<td>").append(partDTO.getPartQuantity()).append("</td>")
                    .append("</tr>"));
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
                .append("<span style=\"color: rgb(0, 101, 105);\">Phone: </span>").append(noteModel.getBoundNote().getContactPhoneNumber()).append("<br><br>")
                .append("<b>Shipping Address</b>").append("<br>");
        if (!noteModel.getBoundNote().getInstalledAt().isEmpty())
            stringBuilder.append(noteModel.getBoundNote().getInstalledAt()).append("<br>")
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

    private String convertBool(boolean convert) {
        if (convert) return "Yes";
        else return "No";
    }

    private String basicInformationToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
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
                .append("<span style=\"color: rgb(0, 101, 105);\">Name: </span>").append(noteModel.getBoundNote().getCallInPerson()).append("<br>")
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

    public void copyCustomerRequest() {
        logger.info("Copying customer request, flashing Group A (Name/Date, Basic Information, Issue, Part Orders and Shipping Information.");
        ClipboardUtils.copyHtmlToClipboard(customerRequestToHTML(), customerRequestToPlainText());
    }

    private String customerRequestToPlainText() {
        return buildNameDateToPlainText() + "\r\n" + "\r\n" +
                basicInformationToPlainText() + "\r\n" +
                issueToPlainText() + "\r\n" +
                "--- Parts Needed ---" + "\r\n" +
                copyAllPartOrdersToPlainText() + "\r\n" +
                shippingInformationToPlainText() + "\r\n";
    }

    private String customerRequestToHTML() {
        return buildNameDateToHTML() + "<br>" + "\r\n" +
                basicInformationToHTML() + "<br>" + "\r\n" +
                issueToHTML() + "<br>" + "\r\n" +
                copyAllPartOrdersToHTML() + "<br>" +
                shippingInformationToHTML() + "<br>" + "\r\n";
    }

    public void copyIssue() {
        ClipboardUtils.copyHtmlToClipboard(issueToHTML(), issueToPlainText());
    }

    private String issueToPlainText() {
        return "--- Issue ---" + "\r\n" +
                noteModel.getBoundNote().getIssue();
    }

    private String issueToHTML() {
        return "<strong>Issue</strong>" + "<br>" +
                noteModel.getBoundNote().getIssue() + "<br>";
    }

    public void copyCorrectiveAction() {
        logger.info("Copying corrective action, flashing group B (FinalBox, Related, Part Orders, Name/Date");
        ClipboardUtils.copyHtmlToClipboard(correctiveActionToHTML(), correctiveActionToPlainText());
    }

    private String correctiveActionToPlainText() {
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

    private String correctiveActionToHTML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildNameDateToHTML()).append("<br>");
        if (!noteModel.getBoundNote().getCreatedWorkOrder().isEmpty()) {
            stringBuilder.append("Created ")
            .append(noteModel.getBoundNote().getCreatedWorkOrder()).append("<br><br>");
        }
        if (!noteModel.getBoundNote().getAdditionalCorrectiveActionText().isEmpty()) {
            stringBuilder.append(noteModel.getBoundNote().getAdditionalCorrectiveActionText()).append("<br>").append("<br>");
        }
        stringBuilder.append(copyAllPartOrdersToHTML()).append("<br>");
        if (!noteModel.getBoundNote().getTex().isEmpty()) {
            stringBuilder.append("Created ")
            .append(noteModel.getBoundNote().getTex()).append("<br>");
        }
        return stringBuilder.toString();
    }

    public void setComplete() {
//        logger.info("Note {} has been set to completed", noteModel.getBoundNote().getId() );
//        noteModel.getBoundNote().setCompleted(true);
        printAllNotes();
    }

    public void createNewNote() {
        // let's update the list note before moving on
        saveNote();
        NoteDTO noteDTO = new NoteDTO(0, false);
        noteDTO.setId(noteRepo.insertNote(noteDTO));
        noteModel.getNotes().add(noteDTO);
        noteModel.getBoundNote().setId(noteDTO.getId());
        noteModel.clearBoundNoteFields();
    }

    public void logCurrentEntitlement() {
        logger.info("Current entitlement set to: {}", noteModel.getCurrentEntitlement());
    }

    public void displayNextNote() {
        int index = getIndexById(noteModel.getBoundNote().getId());
        if (index < noteModel.getNotes().size() - 1) {
            saveNote();
            noteModel.getBoundNote().copyFrom(noteModel.getNotes().get(index + 1));
            checkAndLoadPartOrdersIfNeeded();
        } else logger.debug("You have reached the last element on the list");
        noteModel.setStatusLabel("Note: " + noteModel.getBoundNote().getId()  + "  " + noteModel.getBoundNote().formattedDate());
    }

    public void displayPreviousNote() {
        int index = getIndexById(noteModel.getBoundNote().getId());
        if (index > 0) {
            saveNote();
            noteModel.getBoundNote().copyFrom(noteModel.getNotes().get(index - 1));
            checkAndLoadPartOrdersIfNeeded();
        } else logger.debug("You have reached the first element on the list");
        noteModel.setStatusLabel("Note: " + noteModel.getBoundNote().getId() + "  " + noteModel.getBoundNote().formattedDate());
    }

    private void checkAndLoadPartOrdersIfNeeded() {
        NoteDTO noteDTO = noteModel.getBoundNote();
        if(noteDTO.getPartOrders().isEmpty()) {
            logger.debug("No Part orders found in memory, checking database..");
            noteDTO.setPartOrders(FXCollections.observableArrayList(partOrderRepo.findAllPartOrdersByNoteId(noteDTO.getId())));
            if(noteModel.getBoundNote().getPartOrders().size() > 0) {
                logger.debug("{} part orders loaded into memory", noteDTO.getPartOrders().size());
                noteModel.getBoundNote().setSelectedPartOrder(noteDTO.getPartOrders().getFirst());
                getAllPartsForEachPartOrder();
            }
            else logger.debug("There are no part orders for this note");
        } else
            logger.debug("There are {} part orders already in memory", noteDTO.getPartOrders().size());
        noteModel.refreshBoundNote();
    }

    private void getAllPartsForEachPartOrder() {
        for(PartOrderDTO partOrderDTO : noteModel.getBoundNote().getPartOrders()) {
            partOrderDTO.setParts(FXCollections.observableArrayList(partOrderRepo.getPartsByPartOrder(partOrderDTO)));
        }
    }

    public int getIndexById(int id) {
        for (NoteDTO note : noteModel.getNotes()) {
            if (note.getId() == id) {
                return noteModel.getNotes().indexOf(note);
            }
        }
        return -1;
    }

    public void printAllNotes() {
        System.out.println("Bound Note: " + noteModel.getBoundNote().getId() + " date: " + noteModel.getBoundNote().formattedDate());
        for (NoteDTO note : noteModel.getNotes()) {
            System.out.println("note: " + note.getId() + " date: " + note.formattedDate());
        }
    }

    // this synchronizes the bound object to the correct object in the list
    public void saveNote() {
        for (NoteDTO noteDTO : noteModel.getNotes()) {
            if (noteDTO.getId() == noteModel.getBoundNote().getId()) {
                // copies bound note to the note in the list with matching id
                noteDTO.copyFrom(noteModel.getBoundNote());
                if (noteRepo.noteExists(noteDTO)) {
                    logger.debug("Updated note: {}", noteDTO.getId());
                    noteRepo.updateNote(noteDTO);
                } else {
                    noteDTO.setId(noteRepo.insertNote(noteDTO));
                    logger.debug("Inserted note: {}", noteDTO.getId());
                }
            }
        }
    }

    public void insertPartOrder() {
        int noteId = noteModel.getBoundNote().getId();
        PartOrderDTO partOrderDTO = new PartOrderDTO(0, noteId,"");
        partOrderDTO.setId(partOrderRepo.insertPartOrder(partOrderDTO));
        noteModel.getBoundNote().getPartOrders().add(partOrderDTO);
        noteModel.getBoundNote().setSelectedPartOrder(noteModel.getBoundNote().getPartOrders().getLast());
    }

    public void updatePartOrder() {
        System.out.println("updating part order");
        partOrderRepo.updatePartOrder(noteModel.getBoundNote().getSelectedPartOrder());
    }

    public void deletePartOrder() {
        partOrderRepo.deletePartOrder(noteModel.getBoundNote().getSelectedPartOrder());
    }

    public void deletePart() {
        partOrderRepo.deletePart(noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart());
    }

    public void insertPart() {
        PartDTO partDTO = new PartDTO(noteModel.getBoundNote().getSelectedPartOrder().getId());
        partDTO.setId(partOrderRepo.insertPart(partDTO)); // TODO changes this when hooked to database
        noteModel.getBoundNote().getSelectedPartOrder().getParts().add(partDTO);
    }

    public void updatePart() {
        PartDTO partDTO = noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart();
        partOrderRepo.updatePart(partDTO);
        logger.debug("Updated part order: {} part # {}", partDTO.getId(), partDTO.getPartNumber());
    }

    public void test() {
        System.out.println("Bound Note: " + noteModel.getBoundNote());
        System.out.println("Selected Part Order: " + noteModel.getBoundNote().getSelectedPartOrder());
        if(noteModel.getBoundNote().getSelectedPartOrder() == null)
            System.out.println("Selected Part Order is null");
        else
        System.out.println("Selected part: " + noteModel.getBoundNote().getSelectedPartOrder().getSelectedPart());
    }

    public void setNotesReference(ObservableList<NoteDTO> notes) {
        noteModel.setNotes(notes);
    }
}
