package com.sofka.msa.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    public static Date currentDate() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

    }

    /***
     * <b> Obtains an instance of first date week </b>
     * <p>
     * [Author rtravez, Nov 21, 2024]
     * </p>
     *
     * @return
     */
    public static Date firstDayWeek() {
        return Date.from(LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

    /**
     * <b> Obtains an instance of last date week. </b>
     * <p>
     * [Author rtravez, Nov 21, 2024]
     * </p>
     *
     * @return
     */
    public static Date lastDayWeek() {
        return Date.from(LocalDate.now().with(DayOfWeek.SUNDAY).atStartOfDay(ZoneId.systemDefault()).toInstant());
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
    public static String convertDateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
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
    public static Date convertStringToDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.error("convertStringToDate:", e);
        }
        return null;
    }
}
