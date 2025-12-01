package com.cumple.pos.utils.parser;

import cajapinpad.CifradoTramas;
import com.cumple.pos.models.PagoResponse;
import com.cumple.pos.utils.conversion.TlvParser;
import com.cumple.pos.utils.mapper.ParsedPP;

import java.util.Map;

public class PPParser {

    private final CifradoTramas cifrado = new CifradoTramas();
    private final TlvParser  tlvParser = new TlvParser();

    public ParsedPP parse(String raw){
        if (raw.length() < 4 + 32) throw new IllegalArgumentException("TRAMA INCOMPLETA");

        // (1) quitar longitud inicial (4 chars)
        String bodyPlusHash = raw.substring(4);

        // (2) separar hash final (32 chars)
        String hash = bodyPlusHash.substring(bodyPlusHash.length() - 32);
        String sinHash = bodyPlusHash.substring(0, bodyPlusHash.length() - 32);

        // (3) validar hash
        boolean hashOk = cifrado.validateHash(hash);

        // (4) quitar cabecera fija PP110xxnnn
        // cabecera: tipo(2) + sec(1) + paquetes(1) + cantCampos(3) = 7
        String cuerpoTlv = sinHash.substring(7);

        // (5) aplicar TLV
        Map<String, String> tlv = tlvParser.parse(cuerpoTlv);

        // (6) mapear PagoResponse
        PagoResponse pago = mapPago(tlv);

        return new ParsedPP(raw, sinHash, hash, hashOk, tlv, pago);
    }

    private PagoResponse mapPago(Map<String, String> c) {
        PagoResponse r = new PagoResponse();

        r.setResultado(c.get("01"));
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
