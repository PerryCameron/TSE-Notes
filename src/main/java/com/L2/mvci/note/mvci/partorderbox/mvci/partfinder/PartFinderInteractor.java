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
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

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

        if(jsonResponse.isEmpty()) {
            logger.warn("No product families found");
            return;
        }

        try {
            partModel.setProductFamilies(partModel.getObjectMapper().readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            ));
        } catch (JsonProcessingException ex) {
            logger.error("Error deserializing JSON: {}", ex.getMessage());
            logger.info("The JSON: {}", jsonResponse);
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
            case NOTE -> Platform.runLater(() -> {
                partModel.updatedNotesProperty().set(success == 1);
            });
            case IMAGE -> logger.info("New image for {} saved", partModel.selectedSpareProperty().get().getSpareItem());
            case KEYWORD -> Platform.runLater(() -> {
                partModel.getUpdatedKeywordsProperty().set(success == 1);
            });
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

    // TODO get rid of this when no longer needed
    public void printProductFamilies() {
        if (partModel.getProductFamilies() == null) System.out.println("ProductFamilies is null");
        else
            partModel.getProductFamilies().forEach(System.out::println);
    }

    public void saveImage(SaveType type, ExecutorService executorService) {
        if (partModel.selectedSpareProperty().get() == null) {
            DialogueFx.errorAlert("Error", "No spare item selected.");
            return;
        }
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.hasImage()) {
            DialogueFx.errorAlert("Error", "No image found in clipboard. Use Windows + Shift + S to capture an image.");
            return;
        }
        Image fxImage = clipboard.getImage();
        // Convert clipboard image to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
        Task<Image> saveImageTask = new Task<>() {
            @Override
            protected Image call() {
                try {
                    // Resize image to 357x265 pixels
                    BufferedImage resizedImage = resizeImage(bufferedImage, 357, 265);
                    // Convert to PNG bytes (~100 KB)
                    byte[] imageBytes = convertToPngBytes(resizedImage);
                    // Update ImageView
                    Image newImage = new Image(new ByteArrayInputStream(imageBytes));
                    globalSparesRepo.saveImageToDatabase(partModel.selectedSpareProperty().get().getSpareItem(), imageBytes);
                    saveEditHistory();
                    return newImage;
                } catch (Exception e) {
                    DialogueFx.errorAlert("Error", "Error saving image: " + e.getMessage());
                }
                return null;
            }
        };
        saveImageTask.setOnSucceeded(event -> {
            globalSparesRepo.updateSpare(partModel.selectedSpareProperty().get());
            partModel.getImageView().setImage(saveImageTask.getValue());
        });
        saveImageTask.setOnFailed(event -> {
            Throwable e = saveImageTask.getException();
            logger.error("Failed to save image: {}", e.getMessage(), e);
            DialogueFx.errorAlert("Error", "Failed to save image: " + e.getMessage());
        });
        // Start the task on a background thread
        executorService.submit(saveImageTask);
    }

    // run on non-FX thread
    private BufferedImage resizeImage(BufferedImage original, int targetWidth, int targetHeight) {
        // Skip resizing if original is smaller
        if (original.getWidth() <= targetWidth && original.getHeight() <= targetHeight) {
            logger.info("No resize is needed");
            return original;
        }
        // Resize with Imgscalr, preserving aspect ratio
        return Scalr.resize(original, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                targetWidth, targetHeight);
    }

    // run on non-FX thread
    private byte[] convertToPngBytes(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    /**
     * Loads an image for the currently selected spare item and displays it in the associated ImageView.
     * <p>
     * This method retrieves image data from the {@code globalSparesRepo} for the spare item obtained
     * from {@code partModel.selectedSpareProperty().get().getSpareItem()}. The image loading operation
     * is performed on a background thread to prevent blocking the JavaFX Application Thread, ensuring
     * a responsive UI. If the image data is available, it is converted to an {@code Image} and displayed
     * in the {@code ImageView} obtained from {@code partModel.getImageView()}. If no image data is
     * available or an error occurs, a fallback image ("/images/no-image357x265.png") is displayed instead.
     * </p>
     * <p>
     * The method uses a JavaFX {@code Task} to handle the asynchronous loading, with UI updates
     * performed on the JavaFX Application Thread via the task's {@code setOnSucceeded} and
     * {@code setOnFailed} handlers. Errors during image loading are logged, and the fallback image
     * is set to ensure the {@code ImageView} remains in a valid state.
     * </p>
     * <p>
     * <b>Note:</b> This method assumes that {@code globalSparesRepo.getImage()} is thread-safe and
     * that accessing {@code partModel.selectedSpareProperty().get().getSpareItem()} is safe off the
     * JavaFX Application Thread. If the latter involves UI-related objects, the spare item should be
     * retrieved on the JavaFX Application Thread before starting the task.
     * </p>
     */
    public void getImage(ExecutorService executorService) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                // Perform potentially blocking operation off the FX thread
                byte[] imageAsByte = globalSparesRepo.getImage(partModel.selectedSpareProperty().get().getSpareItem());
                if (imageAsByte != null) {
                    return new Image(new ByteArrayInputStream(imageAsByte));
                } else {
                    // Load fallback image
                    return new Image(Objects.requireNonNull(ButtonFx.class.getResourceAsStream("/images/no-image357x265.png")));
                }
            }
        };
        loadImageTask.setOnSucceeded(event -> {
            // Update ImageView on the FX thread
                Image image = loadImageTask.getValue();
                partModel.getImageView().setImage(image);
        });
        loadImageTask.setOnFailed(event -> {
            // Handle errors on the FX thread
                logger.debug("Error in getImage: {}", loadImageTask.getException().getMessage(), loadImageTask.getException());
                // Optionally set fallback image on error
                Image fallbackImage = new Image(Objects.requireNonNull(ButtonFx.class.getResourceAsStream("/images/no-image357x265.png")));
                partModel.getImageView().setImage(fallbackImage);
        });
        // Start the task on a background thread
        executorService.submit(loadImageTask);
    }

    /**
     * Deserializes a small JSON string containing update history into a list of {@code UpdatedByDTO} objects
     * and updates the model's observable list.
     * <p>
     * This method retrieves the JSON string from the {@code lastUpdatedBy} field of the currently
     * selected spare item in {@code partModel.selectedSpareProperty()}. The JSON data is expected to be
     * small (no more than 200 characters) and stored in memory, making deserialization fast enough to
     * perform on the JavaFX Application Thread without impacting UI responsiveness. The deserialized
     * {@code List<UpdatedByDTO>} is added to the {@code partModel.getUpdatedByDTOs()} observable list,
     * which is cleared beforehand to avoid duplicates. If deserialization fails, an error alert is
     * displayed using {@code DialogueFx.errorAlert()}.
     * </p>
     * <p>
     * If the selected spare item or its {@code lastUpdatedBy} field is null, or if the JSON string is
     * empty, the method exits early without modifying the observable list. All operations, including
     * JSON deserialization and UI updates, are performed on the JavaFX Application Thread, as the
     * lightweight nature of the data does not warrant background processing.
     * </p>
     * <p>
     * <b>Note:</b> This method assumes that {@code partModel.getObjectMapper()} returns a thread-safe
     * {@code ObjectMapper} instance and that the JSON data is small and in-memory. For larger JSON
     * payloads, consider using a {@code javafx.concurrent.Task} to offload deserialization to a
     * background thread.
     * </p>
     */
    public void getUpdatedByToPOJO() {
        // Early exit if no selected spare or no lastUpdatedBy data
        if (partModel.selectedSpareProperty().get() == null ||
                partModel.selectedSpareProperty().get().getLastUpdatedBy() == null) {
            return;
        }
        String lastUpdateJSON = partModel.selectedSpareProperty().get().getLastUpdatedBy();
        // Early exit if JSON is null or empty
        if (lastUpdateJSON == null || lastUpdateJSON.trim().isEmpty()) {
            return;
        }
        try {
            ObjectMapper mapper = partModel.getObjectMapper();
            // Configure ObjectMapper to accept either "updated_by" or "updatedDateTime" as valid field names
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // Deserialize JSON into List<UpdatedByDTO>
            List<UpdatedByDTO> deserializedList = mapper.readValue(lastUpdateJSON,
                    mapper.getTypeFactory().constructCollectionType(List.class, UpdatedByDTO.class));
            // Clear existing list to avoid duplicates
            partModel.getUpdatedByDTOs().clear();
            // Add deserialized items to the list
            partModel.getUpdatedByDTOs().addAll(deserializedList);
        } catch (JsonProcessingException e) {
            DialogueFx.errorAlert("Object mapping failed", e.getMessage());
        }
    }

    // runs on non-FX thread
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
        Platform.runLater(() -> {
            partModel.selectedSpareProperty().get().setLastUpdate(currentTimestamp);
        });
        // Get the existing list of UpdatedByDTOs
        List<UpdatedByDTO> updatedByDTOs = partModel.getUpdatedByDTOs();
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
            Platform.runLater(() -> {
                partModel.selectedSpareProperty().get().setLastUpdatedBy(updatedJson);
            });
            logger.info("Added JSON: {}", updatedJson);
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
