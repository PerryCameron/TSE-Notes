package com.L2.static_tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TestTime {
    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        String timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
//        System.out.println(timeStamp);

//        ---------------------------------------------------------------------
        LocalDateTime dateTime = LocalDateTime.now();
        // Use a formatter that includes the short time zone name
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a z");
        // Assuming you want to format it with the system's default time zone
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = dateTime.atZone(zone);
        // Format the date and time with the short time zone name
//        System.out.println(zonedDateTime.format(formatter));

        DateTimeFormatter formatter2 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//        System.out.println(dateTime.format(formatter2));
    }
}
