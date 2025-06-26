package com.L2.static_tools;

public class ResourceTest {
    public static void main(String[] args) {
        String[] paths = {
                "/images/yes-16.png", "/images/no-16.png", "/images/mail-16.png", "/images/view-16.png",
                "/images/copy-16.png", "/images/down-16.png", "/images/up-16.png", "/images/create-16.png",
                "/images/delete-16.png", "/images/paste-16.png", "/images/clone-16.png", "/images/sync-16.png",
                "/images/question-16.png", "/images/smile-16.png", "/images/call-16.png", "/images/search-16.png",
                "/images/save-16.png", "/images/TSELogo-24.png", "/images/modify-16.png", "/images/cancel-16.png",
                "/images/person-16.png", "/images/help-16.png", "/images/dictionary-16.png", "/images/table-16.png"
        };
        for (String path : paths) {
            System.out.println(path + ": " + (ImageResources.class.getResourceAsStream(path) == null ? "missing" : "found"));
        }
    }
}
