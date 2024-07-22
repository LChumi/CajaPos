package com.cumple.pos.utils;

public class StringUtils {
    private static final String FORMATO_PUERTO = "^COM\\d+$";

    public static boolean validarFormatoPuerto(String puerto) {
        return puerto !=null && puerto.matches(FORMATO_PUERTO);
    }

    public static String sanitizar(String input){
        return input.replaceAll("[^a-zA-Z0-9]", "");
    }
}
