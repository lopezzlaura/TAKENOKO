package com.polytech.bdsm.takenoko.logger;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Bureau De Sebastien Mosser
 * @version 8.0
 */
public class CustomFormatter extends Formatter {

    /**
     * Method to add a format to each log message
     * Every message begin with the current hour, minutes, and seconds separated by ' : '
     *
     * @param record The log without any format
     * @return The String to be display with the format
     */
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        sb.append("\n").append(format.format(calendar.getTime())).append("\t").append(record.getMessage());
        return sb.toString();
    }
}
