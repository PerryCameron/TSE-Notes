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

    public static void displayEmailForReview(String to, String cc, String subject, String htmlBody) {
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

            // Clean up temp item early
            tempMailItem.safeRelease();
            tempMailItem = null;

            // Create the actual mail item (0 = olMailItem)
            mailItem = Dispatch.call(outlook, "CreateItem", new Variant(0)).toDispatch();
            logger.debug("Created new mail item");

            // Prepare combined HTML: Insert signature before </body> if possible
            String combinedHtmlBody = htmlBody;
            if (htmlBody.toLowerCase().contains("</body>")) {
                // Insert signature before </body>
                int bodyEndIndex = combinedHtmlBody.toLowerCase().lastIndexOf("</body>");
                combinedHtmlBody = combinedHtmlBody.substring(0, bodyEndIndex) + defaultSignature + combinedHtmlBody.substring(bodyEndIndex);
            } else {
                // Wrap in <html><body> and add signature before </body>
                combinedHtmlBody = "<html><body>" + htmlBody + defaultSignature + "</body></html>";
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

            // Do NOT release here—handle cleanup later (see below)

        } catch (Exception e) {
            logger.error("Failed to display email", e);
            throw new RuntimeException("Failed to display email in Outlook", e);
        } finally {
            // Only clean up temp if it exists (already released above, but for safety)
            if (tempMailItem != null) {
                try {
                    tempMailItem.safeRelease();
                } catch (Exception e) {
                    logger.warn("Error releasing tempMailItem", e);
                }
            }
            // Do NOT release mailItem, outlook, or ComThread here
        }
    }

    // Add this to your JavaFX app's shutdown logic (e.g., in Application.stop() or a button handler after user is done)
    public static void cleanupOutlookResources(ActiveXComponent outlook, Dispatch mailItem) {
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
        com.jacob.com.ComThread.Release();
        logger.debug("COM thread released");
    }
}