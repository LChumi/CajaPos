package com.cumple.pos.utils;

public class StringValidatorsUtils {

    private static final String FORMATO_PUERTO = "^COM\\d+$";
    private static final String FORMATO_IP = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$";

    public static boolean validarFormatoPuertoCom(String puerto) {
        return puerto == null || !puerto.matches(FORMATO_PUERTO);
    }

    public static boolean validarFormatoIP(String ip) {
        return ip != null && ip.matches(FORMATO_IP);
    }

    public static boolean validarFormatoPuertoNumerico(String puerto) {
        if (puerto == null || puerto.isBlank()) return false;

        try {
            int valor = Integer.parseInt(puerto);
            return valor >= 0 && valor <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
