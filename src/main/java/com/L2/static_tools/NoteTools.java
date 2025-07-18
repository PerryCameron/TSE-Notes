package com.L2.static_tools;

import com.L2.dto.NoteFx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class NoteTools {


    private static final Logger logger = LoggerFactory.getLogger(NoteTools.class);

    public static boolean notesAreTheSameAndSync(NoteFx noteDTO1, NoteFx noteDTO2) {
        boolean areSame = true;

        if (noteDTO1 == null || noteDTO2 == null) {
            logger.debug("One or both NoteDTO objects are null");
            return false;
        }

        if (noteDTO1.getId() != noteDTO2.getId()) {
            logger.debug("Field 'id' has changed: {} -> {}", noteDTO1.getId(), noteDTO2.getId());
            noteDTO1.setId(noteDTO2.getId());
            areSame = false;
        }
        if (!noteDTO1.getTimestamp().equals(noteDTO2.getTimestamp())) {
            logger.debug("Field 'timestamp' has changed: {} -> {}", noteDTO1.getTimestamp(), noteDTO2.getTimestamp());
            noteDTO1.setTimestamp(noteDTO2.getTimestamp());
            areSame = false;
        }
        if(noteDTO1.getWorkOrder() == null) noteDTO1.setWorkOrder("");
        if (!noteDTO1.getWorkOrder().equals(noteDTO2.getWorkOrder())) {
            logger.debug("Field 'workOrder' has changed: {} -> {}", noteDTO1.getWorkOrder(), noteDTO2.getWorkOrder());
            noteDTO1.setWorkOrder(noteDTO2.getWorkOrder());
            areSame = false;
        }
        if(noteDTO1.getCaseNumber() == null) noteDTO1.setCaseNumber("");
        if (!noteDTO1.getCaseNumber().equals(noteDTO2.getCaseNumber())) {
            logger.debug("Field 'caseNumber' has changed: {} -> {}", noteDTO1.getCaseNumber(), noteDTO2.getCaseNumber());
            noteDTO1.setCaseNumber(noteDTO2.getCaseNumber());
            areSame = false;
        }
        if(noteDTO1.getSerialNumber() == null) noteDTO1.setSerialNumber("");
        if (!noteDTO1.getSerialNumber().equals(noteDTO2.getSerialNumber())) {
            logger.debug("Field 'serialNumber' has changed: {} -> {}", noteDTO1.getSerialNumber(), noteDTO2.getSerialNumber());
            noteDTO1.setSerialNumber(noteDTO2.getSerialNumber());
            areSame = false;
        }
        if(noteDTO1.getModelNumber() == null) noteDTO1.setModelNumber("");
        if (!noteDTO1.getModelNumber().equals(noteDTO2.getModelNumber())) {
            logger.debug("Field 'modelNumber' has changed: {} -> {}", noteDTO1.getModelNumber(), noteDTO2.getModelNumber());
            noteDTO1.setModelNumber(noteDTO2.getModelNumber());
            areSame = false;
        }
        if(noteDTO1.getCallInPerson() == null) noteDTO1.setCallInPerson("");
        if (!noteDTO1.getCallInPerson().equals(noteDTO2.getCallInPerson())) {
            logger.debug("Field 'callInPerson' has changed: {} -> {}", noteDTO1.getCallInPerson(), noteDTO2.getCallInPerson());
            noteDTO1.setCallInPerson(noteDTO2.getCallInPerson());
            areSame = false;
        }
        if(noteDTO1.getCallInPhoneNumber() == null) noteDTO1.setCallInPhoneNumber("");
        if (!noteDTO1.getCallInPhoneNumber().equals(noteDTO2.getCallInPhoneNumber())) {
            logger.debug("Field 'callInPhoneNumber' has changed: {} -> {}", noteDTO1.getCallInPhoneNumber(), noteDTO2.getCallInPhoneNumber());
            noteDTO1.setCallInPhoneNumber(noteDTO2.getCallInPhoneNumber());
            areSame = false;
        }
        if(noteDTO1.getCallInEmail() == null) noteDTO1.setCallInEmail("");
        if (!noteDTO1.getCallInEmail().equals(noteDTO2.getCallInEmail())) {
            logger.debug("Field 'callInEmail' has changed: {} -> {}", noteDTO1.getCallInEmail(), noteDTO2.getCallInEmail());
            noteDTO1.setCallInEmail(noteDTO2.getCallInEmail());
            areSame = false;
        }
        if (noteDTO1.isUnderWarranty() != noteDTO2.isUnderWarranty()) {
            logger.debug("Field 'underWarranty' has changed: {} -> {}", noteDTO1.isUnderWarranty(), noteDTO2.isUnderWarranty());
            noteDTO1.setUnderWarranty(noteDTO2.isUnderWarranty());
            areSame = false;
        }
        if(noteDTO1.getActiveServiceContract() == null) noteDTO1.setActiveServiceContract("");
        if (!noteDTO1.getActiveServiceContract().equals(noteDTO2.getActiveServiceContract())) {
            logger.debug("Field 'activeServiceContract' has changed: {} -> {}", noteDTO1.getActiveServiceContract(), noteDTO2.getActiveServiceContract());
            noteDTO1.setActiveServiceContract(noteDTO2.getActiveServiceContract());
            areSame = false;
        }
        if(noteDTO1.getServiceLevel() == null) noteDTO1.setServiceLevel("");
        if (!noteDTO1.getServiceLevel().equals(noteDTO2.getServiceLevel())) {
            logger.debug("Field 'serviceLevel' has changed: {} -> {}", noteDTO1.getServiceLevel(), noteDTO2.getServiceLevel());
            noteDTO1.setServiceLevel(noteDTO2.getServiceLevel());
            areSame = false;
        }
        if(noteDTO1.getSchedulingTerms() == null) noteDTO1.setSchedulingTerms("");
        if (!noteDTO1.getSchedulingTerms().equals(noteDTO2.getSchedulingTerms())) {
            logger.debug("Field 'schedulingTerms' has changed: {} -> {}", noteDTO1.getSchedulingTerms(), noteDTO2.getSchedulingTerms());
            noteDTO1.setSchedulingTerms(noteDTO2.getSchedulingTerms());
            areSame = false;
        }
        if(noteDTO1.getUpsStatus() == null) noteDTO1.setUpsStatus("");
        if (!noteDTO1.getUpsStatus().equals(noteDTO2.getUpsStatus())) {
            logger.debug("Field 'upsStatus' has changed: {} -> {}", noteDTO1.getUpsStatus(), noteDTO2.getUpsStatus());
            noteDTO1.setUpsStatus(noteDTO2.getUpsStatus());
            areSame = false;
        }
        if(noteDTO1.getWorkOrder() == null) noteDTO1.setWorkOrder("");
        if (noteDTO1.isLoadSupported() != noteDTO2.isLoadSupported()) {
            logger.debug("Field 'loadSupported' has changed: {} -> {}", noteDTO1.isLoadSupported(), noteDTO2.isLoadSupported());
            noteDTO1.setLoadSupported(noteDTO2.isLoadSupported());
            areSame = false;
        }
        if (noteDTO1.getTitle() == null) noteDTO1.setTitle("");
        if (noteDTO2.getTitle() != null)
            if (!noteDTO1.getTitle().equals(noteDTO2.getTitle())) {
                logger.debug("Field 'title' has changed: {} -> {}", noteDTO1.getTitle(), noteDTO2.getTitle());
                noteDTO1.setTitle(noteDTO2.getTitle());
                areSame = false;
            }
        if(noteDTO1.getIssue() == null) noteDTO1.setIssue("");
        if (!noteDTO1.getIssue().equals(noteDTO2.getIssue())) {
            logger.debug("Field 'issue' has changed: {} -> {}", noteDTO1.getIssue(), noteDTO2.getIssue());
            noteDTO1.setIssue(noteDTO2.getIssue());
            areSame = false;
        }
        if(noteDTO1.getContactName() == null) noteDTO1.setContactName("");
        if (!noteDTO1.getContactName().equals(noteDTO2.getContactName())) {
            logger.debug("Field 'contactName' has changed: {} -> {}", noteDTO1.getContactName(), noteDTO2.getContactName());
            noteDTO1.setContactName(noteDTO2.getContactName());
            areSame = false;
        }
        if(noteDTO1.getContactPhoneNumber() == null) noteDTO1.setContactPhoneNumber("");
        if (!noteDTO1.getContactPhoneNumber().equals(noteDTO2.getContactPhoneNumber())) {
            logger.debug("Field 'contactPhoneNumber' has changed: {} -> {}", noteDTO1.getContactPhoneNumber(), noteDTO2.getContactPhoneNumber());
            noteDTO1.setContactPhoneNumber(noteDTO2.getContactPhoneNumber());
            areSame = false;
        }
        if(noteDTO1.getContactEmail() == null) noteDTO1.setContactEmail("");
        if (!noteDTO1.getContactEmail().equals(noteDTO2.getContactEmail())) {
            logger.debug("Field 'contactEmail' has changed: {} -> {}", noteDTO1.getContactEmail(), noteDTO2.getContactEmail());
            noteDTO1.setContactEmail(noteDTO2.getContactEmail());
            areSame = false;
        }
        if(noteDTO1.getStreet() == null) noteDTO1.setStreet("");
        if (!noteDTO1.getStreet().equals(noteDTO2.getStreet())) {
            logger.debug("Field 'street' has changed: {} -> {}", noteDTO1.getStreet(), noteDTO2.getStreet());
            noteDTO1.setStreet(noteDTO2.getStreet());
            areSame = false;
        }
        if(noteDTO1.getInstalledAt() == null) noteDTO1.setInstalledAt("");
        if (!noteDTO1.getInstalledAt().equals(noteDTO2.getInstalledAt())) {
            logger.debug("Field 'installedAt' has changed: {} -> {}", noteDTO1.getInstalledAt(), noteDTO2.getInstalledAt());
            noteDTO1.setInstalledAt(noteDTO2.getInstalledAt());
            areSame = false;
        }
        if(noteDTO1.getCity() == null) noteDTO1.setCity("");
        if (!noteDTO1.getCity().equals(noteDTO2.getCity())) {
            logger.debug("Field 'city' has changed: {} -> {}", noteDTO1.getCity(), noteDTO2.getCity());
            noteDTO1.setCity(noteDTO2.getCity());
            areSame = false;
        }
        if(noteDTO1.getState() == null) noteDTO1.setState("");
        if (!noteDTO1.getState().equals(noteDTO2.getState())) {
            logger.debug("Field 'state' has changed: {} -> {}", noteDTO1.getState(), noteDTO2.getState());
            noteDTO1.setState(noteDTO2.getState());
            areSame = false;
        }
        if(noteDTO1.getZip() == null) noteDTO1.setZip("");
        if (!noteDTO1.getZip().equals(noteDTO2.getZip())) {
            logger.debug("Field 'zip' has changed: {} -> {}", noteDTO1.getZip(), noteDTO2.getZip());
            noteDTO1.setZip(noteDTO2.getZip());
            areSame = false;
        }
        if(noteDTO1.getCountry() == null) noteDTO1.setCountry("");
        if (!noteDTO1.getCountry().equals(noteDTO2.getCountry())) {
            logger.debug("Field 'country' has changed: {} -> {}", noteDTO1.getCountry(), noteDTO2.getCountry());
            noteDTO1.setCountry(noteDTO2.getCountry());
            areSame = false;
        }
        if(noteDTO1.getCreatedWorkOrder() == null) noteDTO1.setCreatedWorkOrder("");
        if (!noteDTO1.getCreatedWorkOrder().equals(noteDTO2.getCreatedWorkOrder())) {
            logger.debug("Field 'createdWorkOrder' has changed: {} -> {}", noteDTO1.getCreatedWorkOrder(), noteDTO2.getCreatedWorkOrder());
            noteDTO1.setCreatedWorkOrder(noteDTO2.getCreatedWorkOrder());
            areSame = false;
        }
        if(noteDTO1.getTex() == null) noteDTO1.setTex("");
        if (!noteDTO1.getTex().equals(noteDTO2.getTex())) {
            logger.debug("Field 'tex' has changed: {} -> {}", noteDTO1.getTex(), noteDTO2.getTex());
            noteDTO1.setTex(noteDTO2.getTex());
            areSame = false;
        }
        if (noteDTO1.getPartsOrder() != noteDTO2.getPartsOrder()) {
            logger.debug("Field 'partsOrder' has changed: {} -> {}", noteDTO1.getPartsOrder(), noteDTO2.getPartsOrder());
            noteDTO1.setPartsOrder(noteDTO2.getPartsOrder());
            areSame = false;
        }
        if (noteDTO1.isCompleted() != noteDTO2.isCompleted()) {
            logger.debug("Field 'completed' has changed: {} -> {}", noteDTO1.isCompleted(), noteDTO2.isCompleted());
            noteDTO1.setCompleted(noteDTO2.isCompleted());
            areSame = false;
        }
        if (noteDTO1.isEmail() != noteDTO2.isEmail()) {
            logger.debug("Field 'isEmail' has changed: {} -> {}", noteDTO1.isEmail(), noteDTO2.isEmail());
            noteDTO1.setIsEmail(noteDTO2.isEmail());
            areSame = false;
        }
        if(noteDTO1.getAdditionalCorrectiveActionText() == null) noteDTO1.setAdditionalCorrectiveActionText("");
        if (!noteDTO1.getAdditionalCorrectiveActionText().equals(noteDTO2.getAdditionalCorrectiveActionText())) {
            logger.debug("Field 'additionalCorrectiveActionText' has changed: {} -> {}", noteDTO1.getAdditionalCorrectiveActionText(), noteDTO2.getAdditionalCorrectiveActionText());
            noteDTO1.setAdditionalCorrectiveActionText(noteDTO2.getAdditionalCorrectiveActionText());
            areSame = false;
        }
        if(noteDTO1.getRelatedCaseNumber() == null) noteDTO1.setRelatedCaseNumber("");
        if (!noteDTO1.getRelatedCaseNumber().equals(noteDTO2.getRelatedCaseNumber())) {
            logger.debug("Field 'relatedCaseNumber' has changed: {} -> {}", noteDTO1.getRelatedCaseNumber(), noteDTO2.getRelatedCaseNumber());
            noteDTO1.setRelatedCaseNumber(noteDTO2.getRelatedCaseNumber());
            areSame = false;
        }
        if(noteDTO1.gettAndM() == null) noteDTO1.settAndM("");
        if (!noteDTO1.gettAndM().equals(noteDTO2.gettAndM())) {
            logger.debug("Field 't & M' has changed: {} -> {}", noteDTO1.gettAndM(), noteDTO2.gettAndM());
            noteDTO1.settAndM(noteDTO2.gettAndM());
            areSame = false;
        }
        // If no differences are found, return true, otherwise false
        return areSame;
    }

    public static String normalizeDate(String inputDate) {
        try {
            // Parse the input date in MM-DD-YYYY format
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            LocalDate date = LocalDate.parse(inputDate, inputFormatter);


            // Format it into ISO format (YYYY-MM-DD)
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            // If parsing fails, return the original input (it's not a date)
            return inputDate;
        }
    }

}
