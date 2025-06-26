package com.L2.static_tools;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageResources {
    public static final Image YES_IMAGE;
    public static final Image NO_IMAGE;
    public static final Image MAIL;
    public static final Image VIEW;

    static {
        try {
            YES_IMAGE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/yes-16.png"),
                    "Failed to load resource: /images/yes-16.png"
            ));
            NO_IMAGE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/no-16.png"),
                    "Failed to load resource: /images/no-16.png"
            ));
            MAIL = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/mail-16.png"),
                    "Failed to load resource: /images/mail-16.png"
            ));
            VIEW = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/view-16.png"),
                    "Failed to load resource: /images/view-16.png"
            ));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize ImageResources", e);
        }
    }
}
