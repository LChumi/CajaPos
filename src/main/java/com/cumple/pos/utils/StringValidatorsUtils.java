package com.cumple.pos.utils;

import org.apache.commons.lang3.StringUtils;

public class StringValidatorsUtils {
    private static final String FORMATO_PUERTO = "^COM\\d+$";

    public static boolean validarFormatoPuerto(String puerto) {
        return puerto !=null && puerto.matches(FORMATO_PUERTO);
    }

    public static String sanitizar(String input){
        return input != null ? input.replaceAll("[^a-zA-Z0-9]", "") : null;
    }

    public static boolean esNumerico(String cadena){
        return StringUtils.isNumeric(cadena);
    }

    public static boolean esAlfanumerico(String cadena){
        return StringUtils.isAlphanumeric(cadena);
    }
}
