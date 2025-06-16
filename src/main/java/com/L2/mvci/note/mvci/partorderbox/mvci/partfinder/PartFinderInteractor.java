package com.L2.mvci.note.mvci.partorderbox.mvci.partfinder;

import com.L2.dto.UpdatedByDTO;
import com.L2.dto.global_spares.RangesFx;
import com.L2.enums.SaveType;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.widgetFx.ButtonFx;
import com.L2.widgetFx.DialogueFx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PartFinderInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartFinderInteractor.class);
    private final PartFinderModel partModel;
    private final GlobalSparesRepositoryImpl globalSparesRepo;

    public PartFinderInteractor(PartFinderModel partModel) {
        this.partModel = partModel;
        this.globalSparesRepo = new GlobalSparesRepositoryImpl();
    }

    public void mapProductFamiliesJSONtoPOJO() {
        String jsonResponse = partModel.selectedSpareProperty().get().getPim();
        partModel.getProductFamilies().clear();
        try {
            partModel.setProductFamilies(partModel.getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            ));
        } catch (JsonProcessingException ex) {
            logger.error("Error deserializing JSON: {}", ex.getMessage());
        }
    }

    public void setSelectedRange() {
        String newRange = partModel.comboBoxSelectedRangeProperty().get();
        // getting the range from the list that equals what we a clicked on
        RangesFx selectedRange = partModel.getRanges().stream()
                .filter(range -> range.getRange().equals(newRange))
                .findFirst()
                .orElse(null);
        // if there is a selected range
        if (selectedRange != null) {
            partModel.setSelectedRange(selectedRange);
        } else {
            logger.error("No matching range found for: {}", newRange);
            if (!partModel.getRanges().isEmpty()) {
                logger.warn("Defaulting to first range");
                partModel.setSelectedRange(partModel.getRanges().getFirst());
            } else {
                logger.error("Ranges list is empty, setting selectedRange to null");
                partModel.setSelectedRange(null);
            }
        }
    }

    public void savePart(SaveType type) {
        saveEditHistory();
        int success = globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
        switch (type) {
            case NOTE -> partModel.updatedNotesProperty().set(success == 1);
            case IMAGE -> logger.info("New image for {} saved", partModel.selectedSpareProperty().get().getSpareItem());
            case KEYWORD -> partModel.getUpdatedKeywordsProperty().set(success == 1);
        }
    }

    public void cancelNoteUpdate() {
        partModel.updatedNotesProperty().set(true);
    }

    public void saveToJson() {
        logger.debug("Entering saveToJson, thread: {}", Thread.currentThread().getName());
        try {
            logger.debug("Checking PartModel");
            if (partModel == null) {
                throw new IllegalStateException("PartModel is null");
            }
            logger.debug("Accessing ObjectMapper");
            var objectMapper = partModel.getObjectMapper();
            if (objectMapper == null) {
                throw new IllegalStateException("ObjectMapper is null");
            }
            logger.debug("Accessing ProductFamilies");
            var productFamilies = partModel.getProductFamilies();
            if (productFamilies == null) {
                throw new IllegalStateException("ProductFamilies is null");
            }
            logger.debug("Serializing ProductFamilies: {}", productFamilies);
            String updatedJson = objectMapper.writeValueAsString(productFamilies);
            logger.debug("Accessing selectedSpareProperty");
            var selectedSpare = partModel.selectedSpareProperty().get();
            if (selectedSpare == null) {
                throw new IllegalStateException("Selected spare is null");
            }
            logger.debug("Setting PIM with JSON: {}", updatedJson);
            selectedSpare.setPim(updatedJson);
            logger.debug("Saved ProductFamilies to JSON: {}", updatedJson);

            int success = globalSparesRepo.updateSpare(selectedSpare);
            partModel.updatedRangeProperty().set(success == 1);
        } catch (Throwable t) {
            logger.error("Error in saveToJson: {}", t.getMessage(), t);
            Platform.runLater(() ->
                    DialogueFx.errorAlert("Failed to save", "Error saving JSON: " + t.getMessage()));
        }
    }

    public void printProductFamilies() {
        if (partModel.getProductFamilies() == null) System.out.println("ProductFamilies is null");
        else
            partModel.getProductFamilies().forEach(System.out::println);
    }

    public void saveImage(SaveType type) {
        try {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            if (!clipboard.hasImage()) {
                DialogueFx.errorAlert("Error", "No image found in clipboard. Use Windows + Shift + S to capture an image.");
                return;
            }
            // Convert clipboard image to BufferedImage
            Image fxImage = clipboard.getImage();
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
            // Resize image to 357x265 pixels
            BufferedImage resizedImage = resizeImage(bufferedImage, 357, 265);
            // Convert to PNG bytes (~100 KB)
            byte[] imageBytes = convertToPngBytes(resizedImage);
            // Update ImageView
            Image newImage = new Image(new ByteArrayInputStream(imageBytes));
            partModel.getImageView().setImage(newImage);
            globalSparesRepo.saveImageToDatabase(partModel.selectedSpareProperty().get().getSpareItem(), imageBytes);
            // let's record that we edited the record
            savePart(type);
        } catch (Exception e) {
            DialogueFx.errorAlert("Error", "Error saving image: " + e.getMessage());
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        // Calculate scaled dimensions while preserving aspect ratio
        double aspectRatio = (double) original.getWidth() / original.getHeight();
        int scaledWidth = targetWidth;
        int scaledHeight = (int) (targetWidth / aspectRatio);
        if (scaledHeight > targetHeight) {
            scaledHeight = targetHeight;
            scaledWidth = (int) (targetHeight * aspectRatio);
        }

        // Create resized image
        BufferedImage resized = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return resized;
    }

    private byte[] convertToPngBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public void getImage() {
        Image image;
        try {
            byte[] imageAsByte = globalSparesRepo.getImage(partModel.selectedSpareProperty().get().getSpareItem());
            if (imageAsByte != null) {
                image = new Image(new ByteArrayInputStream(imageAsByte));
            } else {
                image = new Image(Objects.requireNonNull(ButtonFx.class.getResourceAsStream("/images/no-image357x265.png")));
            }
            partModel.getImageView().setImage(image);
        } catch (Exception e) {
            logger.debug("Error in getImage: {}", e.getMessage(), e);
        }
    }

    public void getUpdatedByToPOJO() {
        if (partModel.selectedSpareProperty().get() == null) {
            return;
        }
        if (partModel.selectedSpareProperty().get().getLastUpdatedBy() == null) {
            return;
        }
        String lastUpdateJSON = partModel.selectedSpareProperty().get().getLastUpdatedBy();
        // Clear existing list to avoid duplicates
        partModel.getUpdatedByDTOs().clear();
        // Check if JSON is null or empty
        if (lastUpdateJSON == null || lastUpdateJSON.trim().isEmpty()) {
            return;
        }
        try {
            ObjectMapper mapper = partModel.getObjectMapper();
            // Configure ObjectMapper to accept either "updated_by" or "updatedDateTime" as valid field names
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // Deserialize JSON into List<UpdatedByDTO>
            java.util.List<UpdatedByDTO> deserializedList = mapper.readValue(lastUpdateJSON,
                    mapper.getTypeFactory().constructCollectionType(java.util.List.class, UpdatedByDTO.class));
            // Add deserialized items to the list
            partModel.getUpdatedByDTOs().addAll(deserializedList);
            // partModel.setUpdatedBys(updatedByDTOList);
            partModel.getUpdatedByDTOs().forEach(System.out::println);
        } catch (JsonProcessingException e) {
            DialogueFx.errorAlert("Object mapping failed", e.getMessage());
        }
    }

    public void saveEditHistory() {
        // Check if selected spare exists
        if (partModel.selectedSpareProperty().get() == null) {
            return;
        }
        // Check if user exists
        if (partModel.getNoteModel().userProperty().get() == null) {
            return;
        }
        // Get current user and timestamp
        String currentUser = partModel.getNoteModel().userProperty().get().getFullName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String currentTimestamp = now.format(formatter);
        // update lastUpdate field in the currently selected SparesDTO
        partModel.selectedSpareProperty().get().setLastUpdate(currentTimestamp);
        // Get the existing list of UpdatedByDTOs
        java.util.List<UpdatedByDTO> updatedByDTOs = partModel.getUpdatedByDTOs();
        // Look for an existing entry by the current user
        boolean foundRecentEntry = false;
        for (UpdatedByDTO entry : updatedByDTOs) {
            if (entry.getUpdatedBy() != null && entry.getUpdatedBy().equals(currentUser)) {
                // Parse the existing timestamp
                try {
                    ZonedDateTime lastUpdateTime = ZonedDateTime.parse(
                            entry.getUpdatedDateTime(),
                            formatter.withZone(ZoneId.of("UTC"))
                    );
                    // Check if the last update was within 24 hours
                    long hoursSinceLastUpdate = ChronoUnit.HOURS.between(lastUpdateTime, now);
                    if (hoursSinceLastUpdate < 24) {
                        // Update the timestamp of the existing entry
                        entry.setUpdatedDateTime(currentTimestamp);
                        foundRecentEntry = true;
                        break;
                    }
                } catch (Exception e) {
                    logger.error("Error parsing timestamp: {}", entry.getUpdatedDateTime());
                    // Continue to check other entries or add a new one if parsing fails
                }
            }
        }
        // If no recent entry was found, add a new UpdatedByDTO
        if (!foundRecentEntry) {
            UpdatedByDTO newUpdate = new UpdatedByDTO();
            newUpdate.setUpdatedBy(currentUser);
            newUpdate.setUpdatedDateTime(currentTimestamp);
            updatedByDTOs.add(newUpdate);
        }
        // Serialize the list to JSON
        try {
            ObjectMapper mapper = partModel.getObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // Optional: for readable JSON
            String updatedJson = mapper.writeValueAsString(updatedByDTOs);

            // Set JSON to SparesDTO's lastUpdatedBy field
            partModel.selectedSpareProperty().get().setLastUpdatedBy(updatedJson);
        } catch (Exception e) {
            logger.error("Error serializing UpdatedByDTOs: {}", e.getMessage());
        }
    }

    public void refreshPartInfo() {
        // updates editing data (person - timestamp)
        getUpdatedByToPOJO();
        // triggers view refresh
        partModel.refreshPartInfo();
    }
}
