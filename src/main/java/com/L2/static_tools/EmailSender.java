package com.L2.static_tools;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);

    public static void displayEmail(String to, String cc, String subject, String htmlBody) {
        ActiveXComponent outlook = null;
        Dispatch mailItem = null;
        Dispatch tempMailItem = null;
        try {
            // Initialize Outlook application
            outlook = new ActiveXComponent("Outlook.Application");
            logger.info("Outlook application initialized");

            // Create a temporary mail item to get the default signature
            tempMailItem = Dispatch.call(outlook, "CreateItem", new Variant(0)).toDispatch();
            String defaultSignature = Dispatch.get(tempMailItem, "HTMLBody").getString();
            logger.debug("Retrieved default signature: {}", defaultSignature);

            // Create the actual mail item (0 = olMailItem)
            mailItem = Dispatch.call(outlook, "CreateItem", new Variant(0)).toDispatch();
            logger.debug("Created new mail item");

            // Combine the provided HTML body with the default signature
            String combinedHtmlBody = htmlBody + defaultSignature;
            if (!htmlBody.toLowerCase().contains("</body>")) {
                // Ensure the HTML is wrapped in <html><body> tags if not already
                combinedHtmlBody = "<html><body>" + htmlBody + "</body></html>" + defaultSignature;
            }
            logger.debug("Combined HTML body with signature");

            // Set email properties
            Dispatch.put(mailItem, "To", to);
            Dispatch.put(mailItem, "CC", cc);
            Dispatch.put(mailItem, "Subject", subject);
            Dispatch.put(mailItem, "HTMLBody", combinedHtmlBody);
            logger.debug("Set email properties: To={}, CC={}, Subject={}", to, cc, subject);

            // Display the email in Outlook's UI
            Dispatch.call(mailItem, "Display");
            logger.info("Email displayed in Outlook for To={}", to);

        } catch (Exception e) {
            logger.error("Failed to display email", e);
            throw new RuntimeException("Failed to display email in Outlook", e);
        } finally {
            // Clean up COM objects
            if (tempMailItem != null) {
                try {
                    tempMailItem.safeRelease();
                } catch (Exception e) {
                    logger.warn("Error releasing tempMailItem", e);
                }
            }
            if (mailItem != null) {
                try {
                    mailItem.safeRelease();
                } catch (Exception e) {
                    logger.warn("Error releasing mailItem", e);
                }
            }
            if (outlook != null) {
                try {
                    outlook.safeRelease();
                } catch (Exception e) {
                    logger.warn("Error releasing Outlook", e);
                }
            }
            // Release COM thread
            com.jacob.com.ComThread.Release();
            logger.debug("COM thread released");
        }
    }
//    public static void displayEmail(String to, String cc, String subject, String body) {
//        ActiveXComponent outlook = null;
//        Dispatch mailItem = null;
//        try {
//            // Initialize Outlook application
//            outlook = new ActiveXComponent("Outlook.Application");
//            logger.info("Outlook application initialized");
//
//            // Create a new mail item (0 = olMailItem)
//            mailItem = Dispatch.call(outlook, "CreateItem", new Variant(0)).toDispatch();
//            logger.debug("Created new mail item");
//
//            // Set email properties
//            Dispatch.put(mailItem, "To", to);
//            Dispatch.put(mailItem, "CC", cc);
//            Dispatch.put(mailItem, "Subject", subject);
//            Dispatch.put(mailItem, "HTMLBody", body); // Use "HTMLBody" for HTML content
//            logger.debug("Set email properties: To={}, CC={}, Subject={}", to, cc, subject);
//
//            // Display the email in Outlook's UI
//            Dispatch.call(mailItem, "Display");
//            logger.info("Email displayed in Outlook for To={}", to);
//
//        } catch (Exception e) {
//            logger.error("Failed to display email", e);
//            throw new RuntimeException("Failed to display email in Outlook", e);
//        } finally {
//            // Clean up COM objects
//            if (mailItem != null) {
//                try {
//                    mailItem.safeRelease();
//                } catch (Exception e) {
//                    logger.warn("Error releasing mailItem", e);
//                }
//            }
//            if (outlook != null) {
//                try {
//                    outlook.safeRelease();
//                } catch (Exception e) {
//                    logger.warn("Error releasing Outlook", e);
//                }
//            }
//            // Release COM thread
//            com.jacob.com.ComThread.Release();
//            logger.debug("COM thread released");
//        }
//    }
    }
//    public static void openEmail(String to, String subject, String body, String cc)
//            throws URISyntaxException, IOException {
//        // Encode all components, replacing '+' with '%20' for spaces
//        String encodedTo = URLEncoder.encode(to, "UTF-8").replace("+", "%20");
//        String encodedSubject = URLEncoder.encode(subject, "UTF-8").replace("+", "%20");
//        String encodedBody = URLEncoder.encode(body, "UTF-8").replace("+", "%20");
//        String encodedCc = cc != null && !cc.isEmpty() ? URLEncoder.encode(cc, "UTF-8").replace("+", "%20") : "";
//
//        // Build the mailto URI
//        StringBuilder uriString = new StringBuilder("mailto:" + encodedTo);
//        uriString.append("?subject=").append(encodedSubject);
//        uriString.append("&body=").append(encodedBody);
//        if (!encodedCc.isEmpty()) {
//            uriString.append("&cc=").append(encodedCc);
//        }
//        URI mailtoUri = new URI(uriString.toString());
//
//        // Open the default mail client
//        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
//            Desktop.getDesktop().mail(mailtoUri);
//        } else {
//            throw new IOException("Desktop or MAIL action not supported on this platform.");
//        }
//    } // I have been using this to send an email from my JavaFX app.  It opens Outlook, and when I hit send it looks like nothing happens, there is no email in the sent box, however if I open another email and send it then it may then show in the sent box
//
//    // Overload for calls without CC
//    public static void openEmail(String to, String subject, String body)
//            throws URISyntaxException, IOException {
//        openEmail(to, subject, body, null);
//    }

