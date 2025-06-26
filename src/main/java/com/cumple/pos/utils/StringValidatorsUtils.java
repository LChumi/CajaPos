package com.cumple.pos.utils;


public class StringValidatorsUtils {
    private static final String FORMATO_PUERTO = "^COM\\d+$";

    public static boolean validarFormatoPuerto(String puerto) {
        return puerto == null || !puerto.matches(FORMATO_PUERTO);
    }

}
