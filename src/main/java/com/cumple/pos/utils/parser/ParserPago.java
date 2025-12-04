package com.cumple.pos.utils.parser;

import com.cumple.pos.models.PagoResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParserPago {

    public static PagoResponse parse(String trama) {

        // === CABECERA ===
        String resultado = trama.substring(8, 10);

        // === CUERPO COMPLETO ===
        String cuerpo = trama.substring(13);

        // === 1) Leer CAMPO 25 (mensaje) ANTES de borrar espacios ===
        String code25 = cuerpo.substring(0, 2);   // debe ser "25"
        int len25 = Integer.parseInt(cuerpo.substring(2, 5));
        String mensaje = cuerpo.substring(5, 5 + len25);

        // nuevo offset
        int offset = 5 + len25;

        // === 2) Limpiar espacios SOLO del resto ===
        String resto = cuerpo.substring(offset).replace(" ", "");

        // === 3) Parse CC + LLL + DATA ===
        Map<String,String> campos = new LinkedHashMap<>();
        int i = 0;

        while (i + 5 <= resto.length()) {
            String code = resto.substring(i, i+2);
            String lenStr = resto.substring(i+2, i+5);
            if (!lenStr.matches("\\d{3}")) break;

            int len = Integer.parseInt(lenStr);
            if (i + 5 + len > resto.length()) break;

            String value = resto.substring(i+5, i+5+len);
            campos.put(code, value);

            i += 5 + len;
        }

        // === 4) Construir respuesta ===
        PagoResponse r = new PagoResponse();

        r.setResultado(resultado);
        r.setAprobada("00".equals(resultado));
        r.setMensajeResultado(mensaje.trim());

        r.setCodigoAutorizador(campos.get("02"));
        r.setReferencia(campos.get("13"));
        r.setLote(campos.get("14"));
        r.setHora(campos.get("15"));
        r.setFecha(campos.get("16"));
        r.setNumeroAutorizacion(campos.get("17"));
        r.setMid(campos.get("19"));
        r.setTid(campos.get("20"));
        r.setPanEncriptado(campos.get("22"));
        r.setFechaVencimiento(campos.get("24"));
        r.setCodigoRed(campos.get("26"));
        r.setTarjetaTruncada(campos.get("27"));
        r.setBancoAdquirente(campos.get("30"));
        r.setBancoEmisor(campos.get("31"));
        r.setGrupoTarjeta(campos.get("50"));
        r.setModoLectura(campos.get("51"));
        r.setNombreTarjetahabiente(campos.get("52"));
        r.setMontoFijo(campos.get("53"));
        r.setAid(campos.get("54"));
        r.setTvr(campos.get("58"));
        r.setTsi(campos.get("59"));

        return r;
    }
}