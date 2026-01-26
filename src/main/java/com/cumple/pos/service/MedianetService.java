package com.cumple.pos.service;

import cajapinpad.Conexion;
import cajapinpad.ProccessData;
import com.cumple.pos.models.DatosEnvioPP;
import com.cumple.pos.models.PagoResponse;
import com.cumple.pos.utils.helper.CajaPosHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.cumple.pos.utils.parser.ParserPagoResponse.parse;

@Slf4j
@Service
public class MedianetService {

    private final CajaPosHelper helper;
    private final Conexion  conexion;
    private final ProccessData processData;


    public MedianetService(){
        this.helper = new CajaPosHelper();
        this.conexion = new Conexion();
        this.processData = ProccessData.getInstance();
    }

    public void enviarLT(double monto, String ipPos, int puertoPos){
        byte[] datasend = helper.buildBytes(monto);

        log.info("Enviando LT: {}" , processData.hex2AsciiStr(processData.byte2hex(datasend)));

        getConexion(ipPos, puertoPos, datasend);
    }

    public PagoResponse ProcesarPago(DatosEnvioPP dEnvio, String ip, int puerto){
        byte[] dataSend = helper.buildCompraBytes(dEnvio);
        return getConexion(ip, puerto, dataSend);
    }

    private PagoResponse getConexion(String ipPos, int puertoPos, byte[] datasend) {
        log.info("Enviando Pago: {}" , processData.hex2AsciiStr(processData.byte2hex(datasend)));
        conexion.sendData(ipPos, puertoPos, datasend, 30000);

        byte[] respuestaBytes= conexion.getDataRecived();

        String ascii = processData.hex2AsciiStr(processData.byte2hex(respuestaBytes));
        System.out.println("Respuesta: " + ascii);

        String hashReceived = ascii.substring(ascii.length() -32, ascii.length());
        System.out.println("HASH RECEIVED: " + hashReceived);
        System.out.println("STATUS HASH RECEIVED: " + helper.validateHash(hashReceived));

        PagoResponse r2 = parse(ascii);
        System.out.println(r2);
        return r2;
    }

}
