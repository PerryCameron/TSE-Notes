package com.L2.mvci.note.mvci.partorderbox.mvci.parts;

import com.L2.dto.UserDTO;
import com.L2.dto.global_spares.RangesFx;
import com.L2.dto.global_spares.SparesDTO;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.widgetFx.DialogueFx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

public class PartInteractor {
    private static final Logger logger = LoggerFactory.getLogger(PartInteractor.class);
    private final PartModel partModel;
    private final GlobalSparesRepositoryImpl globalSparesRepo;

    public PartInteractor(PartModel partModel) {
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
        System.out.println("newRange: " + newRange);
        // getting the range from the list that equals what we a clicked on
        RangesFx selectedRange = partModel.getRanges().stream()
                .filter(range -> range.getRange().equals(newRange))
                .findFirst()
                .orElse(null);
        System.out.println("selectedRange found from list: " + selectedRange);
        // if there is a selected range
        if (selectedRange != null) {
            partModel.setSelectedRange(selectedRange);
            System.out.println("selectedRange found from list and referenced to partModel: " + selectedRange);
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

    public void savePart() {
        int success = globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
        partModel.updatedNotesProperty().set(success == 1);
    }

    // userDTO.getFullName() will be the name of the person making the edit
    public void saveEditHistory(UserDTO userDTO) {
        String utcTimestamp = ZonedDateTime.now(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));
        SparesDTO sparesDTO = partModel.selectedSpareProperty().get();

        sparesDTO.setLastUpdate(utcTimestamp);
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

    public void savePartKeyWords() {
        int success = globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
        partModel.getUpdatedKeywordsProperty().set(success == 1);
    }

    public void printProductFamilies() {
        if (partModel.getProductFamilies() == null) System.out.println("ProductFamilies is null");
        else
            partModel.getProductFamilies().forEach(System.out::println);
    }

    public void saveImage() {
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
//            partModel.setImage(newImage);
            partModel.getImageView().setImage(newImage);
            globalSparesRepo.saveImageToDatabase(partModel.selectedSpareProperty().get().getId(), imageBytes);
        } catch (Exception e) {
            DialogueFx.errorAlert("Error", "Error saving image: " + e.getMessage());
            e.printStackTrace();
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
            byte[] imageAsByte = globalSparesRepo.getImage(partModel.selectedSpareProperty().get().getId());
            if (imageAsByte != null) {
                image = new Image(new ByteArrayInputStream(imageAsByte));
//                partModel.setImage(image);
                partModel.getImageView().setImage(image);
            } else {
                logger.warn("Image is not available for this spare");
            }
        } catch (Exception e) {
            logger.error("Error in getImage: {}", e.getMessage(), e);
        }
    }
}
