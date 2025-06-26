package com.L2.mvci.note.mvci.partorderbox.mvci.partviewer;

import com.L2.mvci.note.mvci.partorderbox.mvci.partfinder.PartFinderInteractor;
import com.L2.repository.implementations.GlobalSparesRepositoryImpl;
import com.L2.static_tools.ImageResources;
import com.L2.widgetFx.ButtonFx;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class PartViewerInteractor {


    private final GlobalSparesRepositoryImpl globalSparesRepo;
    private static final Logger logger = LoggerFactory.getLogger(PartViewerInteractor.class);
    private final PartViewerModel partViewerModel;


    public PartViewerInteractor(PartViewerModel partViewerModel) {
        this.partViewerModel = partViewerModel;
        this.globalSparesRepo = new GlobalSparesRepositoryImpl();
    }

    public void getImage(ExecutorService executorService) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                // Perform potentially blocking operation off the FX thread
                byte[] imageAsByte = globalSparesRepo.getImage(partViewerModel.getSparesDTO().getSpareItem());
                if (imageAsByte != null) {
                    return new Image(new ByteArrayInputStream(imageAsByte));
                } else {
                    // Load fallback image
                    return ImageResources.NO_IMAGE_AVAILABLE;
                }
            }
        };
        loadImageTask.setOnSucceeded(event -> {
            // Update ImageView on the FX thread
            Image image = loadImageTask.getValue();
            partViewerModel.getImageView().setImage(image);
        });
        loadImageTask.setOnFailed(event -> {
            // Handle errors on the FX thread
            logger.debug("Error in getImage: {}", loadImageTask.getException().getMessage(), loadImageTask.getException());
            // Optionally set fallback image on error
            partViewerModel.getImageView().setImage(ImageResources.NO_IMAGE_AVAILABLE);
        });
        // Start the task on a background thread
        executorService.submit(loadImageTask);
    }
}
