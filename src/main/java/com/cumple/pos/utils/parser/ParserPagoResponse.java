package com.cumple.pos.utils.parser;

import com.cumple.pos.models.PagoResponse;

import java.util.HashMap;
import java.util.Map;

public class ParserPagoResponse {

    public static PagoResponse parse(String trama) {
        //leer cabecera fija
        String talla = trama.substring(0 , 4);
        String tipo = trama.substring(4, 6); //PP
        String secuencia = trama.substring(6, 7);
        String paquetes = trama.substring(7, 8);
        String resultado = trama.substring(8, 10);
        int totalCampos = Integer.parseInt(trama.substring(10, 13));

        //Parsear cuerpo
        Map<String, String> campos = parseCampos(trama.substring(13));

        return buildResponse(resultado, campos);
    }

    //Dedsfragemetar cuerpo de la trama
    private static Map<String, String> parseCampos(String cuerpo) {
        Map<String, String> map = new HashMap<>();

        int index = 0;
        while(index < cuerpo.length() ) {
            if (index + 5 > cuerpo.length()) break;

            String code = cuerpo.substring(index, index + 2);
            int len = Integer.parseInt(cuerpo.substring(index + 2, index + 5));

            if (index + 5 + len > cuerpo.length()) break;

            String data = cuerpo.substring(index + 5, index + 6 + len);

            map.put(code, data);

            index += 5 + len;

        }
        return map;
    }

    private static PagoResponse buildResponse(String resultado, Map<String, String> c) {
        PagoResponse r = new PagoResponse();
        r.setResultado(resultado);
        r.setAprobada("00".equals(resultado));

        r.setMensajeResultado(c.get("25"));
        r.setCodigoAutorizador(c.get("02"));
        r.setReferencia(c.get("13"));
        r.setLote(c.get("14"));
        r.setHora(c.get("15"));
        r.setFecha(c.get("16"));
        r.setNumeroAutorizacion(c.get("17"));
        r.setMid(c.get("19"));
        r.setTid(c.get("20"));
        r.setPanEncriptado(c.get("22"));
        r.setFechaVencimiento(c.get("24"));
        r.setCodigoRed(c.get("26"));
        r.setTarjetaTruncada(c.get("27"));
        r.setBancoAdquirente(c.get("30"));
        r.setBancoEmisor(c.get("31"));
        r.setGrupoTarjeta(c.get("50"));
        r.setModoLectura(c.get("51"));
        r.setNombreTarjetahabiente(c.get("52"));
        r.setMontoFijo(c.get("53"));
        r.setAid(c.get("54"));
        r.setTvr(c.get("58"));
        r.setTsi(c.get("59"));

        return r;
    }
}
