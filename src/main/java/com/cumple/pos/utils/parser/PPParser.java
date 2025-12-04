package com.cumple.pos.utils.parser;

import cajapinpad.CifradoTramas;
import com.cumple.pos.models.PagoResponse;

import static com.cumple.pos.utils.parser.PagoResponseParser.LONGITUDES_FIJAS;


public class PPParser {

    private final CifradoTramas cifrado = new CifradoTramas();

    public PagoResponse parse(String trama) {
        PagoResponse response = new PagoResponse();

        // --- Cabecera ---
        String resultado = safeSubstring(trama, 8, 10);
        response.setResultado(resultado);
        response.setAprobada("00".equals(resultado));

        // --- Cuerpo ---
        int offset = 13;
        while (offset + 5 <= trama.length()) {
            String codigoStr = trama.substring(offset, offset+2);
            int codigo = isNumeric(codigoStr) ? Integer.parseInt(codigoStr) : -1;

            String longitudStr = trama.substring(offset+2, offset+5);
            int longitud;
            if (isNumeric(longitudStr)) {
                longitud = Integer.parseInt(longitudStr);
            } else {

                longitud = LONGITUDES_FIJAS.getOrDefault(codigo, longitudStr.length());
            }

            if (offset + 5 + longitud > trama.length()) break;

            String valor = trama.substring(offset+5, offset+5+longitud);

            switch (codigo) {
                case 25: response.setMensajeResultado(valor); break;
                case 2:  response.setCodigoAutorizador(valor); break;
                case 13: response.setReferencia(valor); break;
                case 14: response.setLote(valor); break;
                case 15: response.setHora(valor); break;
                case 16: response.setFecha(valor); break;
                case 17: response.setNumeroAutorizacion(valor); break;
                case 19: response.setMid(valor); break;
                case 20: response.setTid(valor); break;
                case 22: response.setPanEncriptado(valor); break;
                case 24: response.setFechaVencimiento(valor); break;
                case 26: response.setCodigoRed(valor); break;
                case 27:
                    // Campo tarjeta truncada: máximo 25 caracteres
                    int len = Math.min(longitud, 25);
                    String truncada = trama.substring(offset+5, offset+5+len).trim();
                    response.setTarjetaTruncada(truncada);
                    break;
                case 30: response.setBancoAdquirente(valor); break;
                case 31: response.setBancoEmisor(valor); break;
                case 50: response.setGrupoTarjeta(valor); break;
                case 51: response.setModoLectura(valor); break;
                case 52: response.setNombreTarjetahabiente(valor); break;
                case 53: response.setMontoFijo(valor); break;
                case 54: response.setAid(valor); break;
                case 58: response.setTvr(valor); break;
                case 59: response.setTsi(valor); break;
            }

            offset += 5 + longitud;
        }

        return response;
    }

    private String safeSubstring(String s, int begin, int end) {
        if (begin >= s.length()) return "";
        if (end > s.length()) end = s.length();
        return s.substring(begin, end);
    }

    private boolean isNumeric(String s) {
        return s != null && s.matches("\\d+");
    }

    public PagoResponse parsePago(String trama) {
        PagoResponse response = new PagoResponse();

        // Cabecera fija
        response.setResultado(trama.substring(8, 10));
        response.setAprobada("00".equals(response.getResultado()));

        int offset = 13; // después de cabecera

        // Campo 25: Mensaje Resultado (20 caracteres)
        response.setMensajeResultado(trama.substring(offset+5, offset+5+20).trim());
        offset += 5 + 20;

        // Campo 2: Código Autorizador (2 caracteres)
        response.setCodigoAutorizador(trama.substring(offset+5, offset+5+2));
        offset += 5 + 2;

        // Campo 13: Referencia (6 caracteres)
        response.setReferencia(trama.substring(offset+5, offset+5+6));
        offset += 5 + 6;

        // Campo 14: Lote (6 caracteres)
        response.setLote(trama.substring(offset+5, offset+5+6));
        offset += 5 + 6;

        // Campo 15: Hora (6 caracteres)
        response.setHora(trama.substring(offset+5, offset+5+6));
        offset += 5 + 6;

        // Campo 16: Fecha (8 caracteres)
        response.setFecha(trama.substring(offset+5, offset+5+8));
        offset += 5 + 8;

        // Campo 17: Número Autorización (6 caracteres)
        response.setNumeroAutorizacion(trama.substring(offset+5, offset+5+6));
        offset += 5 + 6;

        // Campo 19: MID (15 caracteres)
        response.setMid(trama.substring(offset+5, offset+5+15));
        offset += 5 + 15;

        // Campo 20: TID (8 caracteres)
        response.setTid(trama.substring(offset+5, offset+5+8));
        offset += 5 + 8;

        // Campo 24: Fecha Vencimiento (4 caracteres)
        response.setFechaVencimiento(trama.substring(offset+5, offset+5+4));
        offset += 5 + 4;

        // Campo 26: Código Red (2 caracteres)
        response.setCodigoRed(trama.substring(offset+5, offset+5+2));
        offset += 5 + 2;

        // Campo 27: Tarjeta Truncada (máx 25 caracteres)
        response.setTarjetaTruncada(trama.substring(offset+5, offset+5+25).trim());
        offset += 5 + 25;

        // Campo 50: Grupo Tarjeta (25 caracteres)
        response.setGrupoTarjeta(trama.substring(offset+5, offset+5+25).trim());
        offset += 5 + 25;

        // Campo 51: Modo Lectura (2 caracteres)
        response.setModoLectura(trama.substring(offset+5, offset+5+2));
        offset += 5 + 2;

        // Campo 52: Nombre Tarjetahabiente (40 caracteres)
        response.setNombreTarjetahabiente(trama.substring(offset+5, offset+5+40).trim());
        offset += 5 + 40;

        // Campo 54: AID (20 caracteres)
        response.setAid(trama.substring(offset+5, offset+5+20).trim());
        offset += 5 + 20;

        // Campo 58: TVR (10 caracteres)
        response.setTvr(trama.substring(offset+5, offset+5+10).trim());
        offset += 5 + 10;

        // Campo 59: TSI (4 caracteres)
        response.setTsi(trama.substring(offset+5, offset+5+4).trim());
        offset += 5 + 4;

        return response;
    }
}