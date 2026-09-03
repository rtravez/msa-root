package com.rtravez.msa.util;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * DateUtil.
 *
 * @author rtravez on 07/03/2024
 * @version 1.0
 * @since 1.0.0
 */
@Slf4j
public final class DateUtil {

    /**
     * Constructor.
     */
    private DateUtil() {
    }

    /**
     * Obtiene la fecha actual LocalDateTime.
     *
     * @return Date
     * @author components on 07/03/2024
     */
    public static LocalDateTime currentDate() {
        return LocalDateTime.now(ZoneId.systemDefault());

    }

    /***
     * <b> Obtains an instance of first date week </b>
     * <p>
     * [Author rtravez, Nov 21, 2024]
     * </p>
     *
     * @return
     */
    public static LocalDateTime firstDayWeek() {
        return LocalDate.now(ZoneId.systemDefault()).with(DayOfWeek.MONDAY).atStartOfDay();

    }

    /**
     * <b> Obtains an instance of last date week. </b>
     * <p>
     * [Author rtravez, Nov 21, 2024]
     * </p>
     *
     * @return
     */
    public static LocalDateTime lastDayWeek() {
        return LocalDate.now(ZoneId.systemDefault()).with(DayOfWeek.SUNDAY).atStartOfDay();
    }

    /**
     * <b> Método que permite convertir una fecha a formato cadena. </b>
     * <p>
     * [Author rtravez, Dec 20, 2024]
     * </p>
     *
     * @param date
     * @return
     */
    public static String convertDateToString(LocalDateTime date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTimeFormatter.format(date);
    }

    /**
     * <b> Permite convertir un String en fecha. </b>
     * <p>
     * [Author rtravez, Jan 24, 2024]
     * </p>
     *
     * @param date
     * @return
     */
    public static LocalDateTime convertStringToDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDateTime.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            log.error("convertStringToDate:", e);
        }
        return null;
    }
}
