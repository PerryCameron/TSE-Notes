package com.L2.static_tools;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;


public class EmailSender {
    public static void openEmail(String to, String subject, String body, String cc)
            throws URISyntaxException, IOException {
        // Encode all components, replacing '+' with '%20' for spaces
        String encodedTo = URLEncoder.encode(to, "UTF-8").replace("+", "%20");
        String encodedSubject = URLEncoder.encode(subject, "UTF-8").replace("+", "%20");
        String encodedBody = URLEncoder.encode(body, "UTF-8").replace("+", "%20");
        String encodedCc = cc != null && !cc.isEmpty() ? URLEncoder.encode(cc, "UTF-8").replace("+", "%20") : "";

        // Build the mailto URI
        StringBuilder uriString = new StringBuilder("mailto:" + encodedTo);
        uriString.append("?subject=").append(encodedSubject);
        uriString.append("&body=").append(encodedBody);
        if (!encodedCc.isEmpty()) {
            uriString.append("&cc=").append(encodedCc);
        }
        URI mailtoUri = new URI(uriString.toString());

        // Open the default mail client
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            Desktop.getDesktop().mail(mailtoUri);
        } else {
            throw new IOException("Desktop or MAIL action not supported on this platform.");
        }
    } // I have been using this to send an email from my JavaFX app.  It opens Outlook, and when I hit send it looks like nothing happens, there is no email in the sent box, however if I open another email and send it then it may then show in the sent box

    // Overload for calls without CC
    public static void openEmail(String to, String subject, String body)
            throws URISyntaxException, IOException {
        openEmail(to, subject, body, null);
    }
}
