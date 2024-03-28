package com.sellosmonterrey.administrador;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BackupUtils {

    public static String getBackupDirPath() {
        String homeDir = System.getProperty("user.home");
        return homeDir + "/backups";
    }

    public static String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date()); // Set calendar to current date
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Set hour to 0
        calendar.set(Calendar.MINUTE, 0); // Set minute to 0
        calendar.set(Calendar.SECOND, 0); // Set second to 0
        calendar.set(Calendar.MILLISECOND, 0); // Set millisecond to 0
        java.util.Date onlyDate = calendar.getTime(); // Get date without time
        return dateFormat.format(onlyDate);
    }
}