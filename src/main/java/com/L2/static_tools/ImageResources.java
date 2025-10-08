package com.L2.static_tools;

import javafx.scene.image.Image;

import java.util.Objects;

public class ImageResources {
    public static final Image YES;
    public static final Image NO;
    public static final Image MAIL;
    public static final Image VIEW;
    public static final Image COPY;
    public static final Image DOWN;
    public static final Image UP;
    public static final Image NEW;
    public static final Image DELETE;
    public static final Image PASTE;
    public static final Image CLONE;
    public static final Image SYNC;
    public static final Image COPY_CUSTOMER_REQUEST;
    public static final Image COPY_ANSWER_TO_CUSTOMER;
    public static final Image COPY_LOGGED_CALL;
    public static final Image SEARCH;
    public static final Image SAVE;
    public static final Image TSELOGO16;
    public static final Image TSELOGO24;
    public static final Image TSELOGO64;
    public static final Image EDIT;
    public static final Image CANCEL;
    public static final Image PERSON;
    public static final Image HELP;
    public static final Image DICTIONARY;
    public static final Image TABLE;
    public static final Image NO_IMAGE_AVAILABLE;
    public static final Image NOTES;
    public static final Image GEAR;
    public static final Image LIST;
    public static final Image OWN;
    public static final Image FLAG;
    public static final Image PREFERENCE;
    public static final Image EMAIL;

    static {
        try {
            EMAIL = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/letter-16.png"),
                    "Failed to load resource: /images/letter-16.png"
            ));
            PREFERENCE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/preference-16.png"),
                    "Failed to load resource: /images/flag-16.png"
            ));
            FLAG = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/flag-16.png"),
                    "Failed to load resource: /images/flag-16.png"
            ));
            OWN = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/own-16.png"),
                    "Failed to load resource: /images/own-16.png"
            ));
            NOTES = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/notes-16.png"),
                    "Failed to load resource: /images/notes-16.png"
            ));
            GEAR = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/gear-16.png"),
                    "Failed to load resource: /images/gear-16.png"
            ));
            LIST = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/list-16.png"),
                    "Failed to load resource: /images/list-16.png"
            ));
            NO_IMAGE_AVAILABLE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/no-image357x265.png"),
                    "Failed to load resource: /images/no-image357x265.png"
            ));
            HELP = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/help-16.png"),
                    "Failed to load resource: /images/help-16.png"
            ));
            DICTIONARY = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/dictionary-16.png"),
                    "Failed to load resource: /images/dictionary-16.png"
            ));
            TABLE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/table-16.png"),
                    "Failed to load resource: /images/table-16.png"
            ));
            PERSON = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/person-16.png"),
                    "Failed to load resource: /images/person-16.png"
            ));
            CANCEL = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/cancel-16.png"),
                    "Failed to load resource: /images/cancel-16.png"
            ));
            EDIT = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/modify-16.png"),
                    "Failed to load resource: /images/modify-16.png"
            ));
            SAVE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/save-16.png"),
                    "Failed to load resource: /images/save-16.png"
            ));
            TSELOGO16 = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/TSELogo-16.png"),
                    "Failed to load resource: /images/TSELogo-16.png"
            ));
            TSELOGO24 = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/TSELogo-24.png"),
                    "Failed to load resource: /images/TSELogo-24.png"
            ));
            TSELOGO64 = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/TSELogo-64.png"),
                    "Failed to load resource: /images/TSELogo-64.png"
            ));
            SEARCH = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/search-16.png"),
                    "Failed to load resource: /images/search-16.png"
            ));
            COPY_LOGGED_CALL = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/call-16.png"),
                    "Failed to load resource: /images/call-16.png"
            ));
            COPY_ANSWER_TO_CUSTOMER = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/smile-16.png"),
                    "Failed to load resource: /images/smile-16.png"
            ));
            COPY_CUSTOMER_REQUEST = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/question-16.png"),
                    "Failed to load resource: /images/question-16.png"
            ));
            SYNC = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/sync-16.png"),
                    "Failed to load resource: /images/sync-16.png"
            ));
            DOWN = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/down-16.png"),
                    "Failed to load resource: /images/down-16.png"
            ));
            UP = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/up-16.png"),
                    "Failed to load resource: /images/up-16.png"
            ));
            NEW = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/create-16.png"),
                    "Failed to load resource: /images/create-16.png"
            ));
            PASTE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/paste-16.png"),
                    "Failed to load resource: /images/paste-16.png"
            ));
            CLONE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/clone-16.png"),
                    "Failed to load resource: /images/clone-16.png"
            ));
            YES = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/yes-16.png"),
                    "Failed to load resource: /images/yes-16.png"
            ));
            NO = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/no-16.png"),
                    "Failed to load resource: /images/no-16.png"
            ));
            DELETE = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/delete-16.png"),
                    "Failed to load resource: /images/delete-16.png"
            ));
            MAIL = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/mail-16.png"),
                    "Failed to load resource: /images/mail-16.png"
            ));
            VIEW = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/view-16.png"),
                    "Failed to load resource: /images/view-16.png"
            ));
            COPY = new Image(Objects.requireNonNull(
                    ImageResources.class.getResourceAsStream("/images/copy-16.png"),
                    "Failed to load resource: /images/copy-16.png"
            ));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Failed to initialize ImageResources due to missing resource", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize ImageResources", e);
        }
    }
}
