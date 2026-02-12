package com.cumple.pos.service;

import cajapinpad.CifradoTramas;
import cajapinpad.Conexion;
import cajapinpad.ProccessData;
import com.cumple.pos.models.DatosEnvioPP;
import com.cumple.pos.models.PagoResponse;
import com.cumple.pos.utils.enums.CampoPP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cumple.pos.utils.DateUtils.obtenerFechaActual;
import static com.cumple.pos.utils.DateUtils.obtenerHoraActual;
import static com.cumple.pos.utils.parser.ParserPagoResponse.parse;

@Slf4j
@Service
public class MedianetService {

    private final Conexion conexion;
    private final ProccessData processData;
    private final CifradoTramas cifrado;

    public MedianetService() {
        this.conexion = new Conexion();
        this.processData = ProccessData.getInstance();
        this.cifrado = new CifradoTramas();
    }

    public PagoResponse ProcesarPago(DatosEnvioPP dEnvio, String ip, int puerto) {
        byte[] dataSend = build(dEnvio);
        return getConexion(ip, puerto, dataSend);
    }

    private PagoResponse getConexion(String ipPos, int puertoPos, byte[] datasend) {
        log.info("Enviando Pago: {}", processData.hex2AsciiStr(processData.byte2hex(datasend)));
        conexion.sendData(ipPos, puertoPos, datasend, 30000);

        byte[] respuestaBytes = conexion.getDataRecived();

        String ascii = processData.hex2AsciiStr(processData.byte2hex(respuestaBytes));
        System.out.println("Respuesta: " + ascii);

        String hashReceived = ascii.substring(ascii.length() - 32, ascii.length());
        System.out.println("HASH RECEIVED: " + hashReceived);
        System.out.println("STATUS HASH RECEIVED: " + validateHash(hashReceived));

        PagoResponse r2 = parse(ascii);
        System.out.println(r2);
        return r2;
    }

    private byte[] build(DatosEnvioPP d) {

        validar(d);

        List<String> campos = new ArrayList<>();

        String tipo = d.getTipoTransaccion();
        String codDif = d.getCodigoDiferido();

        boolean esDiferido = "02".equals(tipo) || ("03".equals(tipo) && !"00".equals(codDif));

        boolean esAnulacion = "03".equals(tipo);

        // 0 Identificador
        campos.add("PP");

        // 1 Secuencia placeholder
        campos.add("");

        // 2 -3
        campos.add(CampoPP.TIPO_TRANSACCION.build(tipo));
        campos.add(CampoPP.CODIGO_DIFERIDO.build(codDif));

        // plazo cuando corresponde
        if (esDiferido) {
            campos.add(CampoPP.PLAZO.build(d.getPlazo()));
        }

        //Montos orden fijo
        campos.add(CampoPP.MONTO_TOTAL.build(normalizarMonto(d.getTotal())));
        campos.add(CampoPP.SUBTOTAL_IVA.build(normalizarMonto(d.getSubtotal())));
        campos.add(CampoPP.SUBTOTAL0.build(normalizarMonto(d.getSubtotal0())));
        campos.add(CampoPP.IVA.build(normalizarMonto(d.getIva())));

        // Hora / Fecha
        campos.add(CampoPP.HORA.build(obtenerHoraActual()));
        campos.add(CampoPP.FECHA.build(obtenerFechaActual()));

        // Identificadores
        campos.add(CampoPP.PUNTO_VENTA.build(d.getPVenta()));
        campos.add(CampoPP.MID.build(d.getMid()));
        campos.add(CampoPP.TID.build(d.getTid()));
        campos.add(CampoPP.CID.build(d.getCid()));

        // Campos de anulación
        if (esAnulacion) {
            campos.add(CampoPP.REFERENCIA.build(d.getReferencia()));
            campos.add(CampoPP.LOTE.build(d.getLote()));
            campos.add(CampoPP.NUMERO_AUTORIZACION.build(d.getNumeroAutorizacion()));
        }

        // Secuencia real (sin PP ni hash)
        int camposSinHash = campos.size() - 1;
        String secuencia = "11" + String.format("%03d", camposSinHash - 1);
        campos.set(1, secuencia);

        // Hash siempre al final
        campos.add(processData.padright(cifrado.getHash(), 32, ' '));

        return processData.getFinalData(campos.toArray(new String[0]));
    }

    private void validar(DatosEnvioPP d) {

        String tipo = d.getTipoTransaccion();
        String codDif = d.getCodigoDiferido();

        if ("01".equals(tipo) && !"00".equals(codDif)) {
            throw new IllegalArgumentException("Compra corriente no puede ser diferido");
        }

        if ("02".equals(tipo) && "00".equals(codDif)) {
            throw new IllegalArgumentException("Compra diferido requiere codigo diferido");
        }

        if (("02".equals(tipo) || "03".equals(tipo))
                && !"00".equals(codDif)
                && d.getPlazo() == null) {
            throw new IllegalArgumentException("Diferido requiere plazo");
        }

        if ("03".equals(tipo)) {
            if (d.getReferencia() == null
                    || d.getLote() == null
                    || d.getNumeroAutorizacion() == null) {
                throw new IllegalArgumentException("Anulación incompleta");
            }
        }
    }

    private String normalizarMonto(double monto) {
        long montoDecimales = Math.round(monto * 100);
        return String.format("%012d", montoDecimales);
    }

    public boolean validateHash(String hash) {
        return cifrado.validateHash(hash);
    }

}