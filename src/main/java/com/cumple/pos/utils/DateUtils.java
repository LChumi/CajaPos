package com.cumple.pos.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String obtenerFechaActual() {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return hoy.format(formatter);
    }
    public  static String obtenerHoraActual() {
        LocalDateTime ahora = LocalDateTime.now();
        return ahora.format(DateTimeFormatter.ofPattern("HHmmss"));
    }
}