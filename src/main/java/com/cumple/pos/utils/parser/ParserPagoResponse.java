package com.cumple.pos.utils.parser;


import com.cumple.pos.models.PagoResponse;

public class ParserPagoResponse {

    public static PagoResponse parse(String trama) {

        PagoResponse r = new PagoResponse();

        // Cabecera
        String resultado = trama.substring(8,10);
        int cantidad = Integer.parseInt(trama.substring(10,13));

        r.setResultado(resultado);
        r.setAprobada("00".equals(resultado));

        String cuerpo = trama.substring(13);
        int offset = 0;

        for (int i = 0; i < cantidad; i++) {

            if (offset + 5 > cuerpo.length()) break;

            String cc = cuerpo.substring(offset, offset + 2);
            String lll = cuerpo.substring(offset + 2, offset + 5);

            if (!lll.matches("\\d{3}")) break;

            int len = Integer.parseInt(lll);
            if (offset + 5 + len > cuerpo.length()) break;

            String data = cuerpo.substring(offset + 5, offset + 5 + len);

            mapCampo(r, cc, data.trim());

            offset += 5 + len;
        }

        return r;
    }

    private static void mapCampo(PagoResponse r, String cc, String v) {
        switch (cc) {
            case "02": r.setCodigoAutorizador(v); break;
            case "13": r.setReferencia(v); break;
            case "14": r.setLote(v); break;
            case "15": r.setHora(v); break;
            case "16": r.setFecha(v); break;
            case "17": r.setNumeroAutorizacion(v); break;
            case "19": r.setMid(v); break;
            case "20": r.setTid(v); break;
            case "22" : r.setPanEncriptado(v); break;
            case "24": r.setFechaVencimiento(v); break;
            case "25": r.setMensajeResultado(v); break;
            case "26": r.setCodigoRed(v); break;
            case "27": r.setTarjetaTruncada(v); break;
            case "28": r.setInteres(v); break;
            case "29": r.setPublicidad(v); break;
            case "30": r.setBancoAdquirente(v); break;
            case "31": r.setBancoEmisor(v); break;
            case "34": r.setCobrandSha(v); break;
            case "35": r.setChipAppName(v); break;
            case "36": r.setTipoCuenta(v); break;
            case "42": r.setCupo(v); break;
            case "43": r.setMontoMaxidolar(v); break;
            case "45": r.setNumTarjetaPayClub(v); break;
            case "50": r.setGrupoTarjeta(v); break;
            case "51": r.setModoLectura(v); break;
            case "52": r.setNombreTarjetahabiente(v); break;
            case "53": r.setMontoFijo(v); break;
            case "54": r.setAid(v); break;
            case "58": r.setTvr(v); break;
            case "59": r.setTsi(v); break;
            case "90": r.setHash(v); break;
        }
    }


}
