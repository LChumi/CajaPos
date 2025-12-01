package com.cumple.pos.service;

import cajapinpad.CifradoTramas;
import cajapinpad.Conexion;
import cajapinpad.ProccessData;
import com.cumple.pos.models.DatosEnvioPP;
import com.cumple.pos.utils.helper.CajaPosHelper;
import com.cumple.pos.utils.mapper.ParsedPP;
import com.cumple.pos.utils.parser.PPParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MedianetService {

    private final CajaPosHelper helper;
    private final Conexion  conexion;
    private final ProccessData processData;
    private final CifradoTramas cifrado;


    public MedianetService(){
        this.helper = new CajaPosHelper();
        this.conexion = new Conexion();
        this.processData = ProccessData.getInstance();
        this.cifrado = new CifradoTramas();
    }

    public void enviarLT(double monto, String ipPos, int puertoPos){
        byte[] datasend = helper.buildBytes(monto);

        log.info("Enviando LT: {}" , processData.byte2hex(datasend));

        getConexion(ipPos, puertoPos, datasend);
    }

    public void ProcesarPago(DatosEnvioPP dEnvio, String ip, int puerto){
        byte[] dataSend = helper.buildCompraBytes(dEnvio);

        log.info("Enviando Pago: {}" , processData.hex2AsciiStr(processData.byte2hex(dataSend)));

        getConexion(ip, puerto, dataSend);
    }

    private void getConexion(String ipPos, int puertoPos, byte[] datasend) {
        //conexion.sendData(ipPos, puertoPos, datasend, 30000);

        //byte[] respuestaBytes= conexion.getDataRecived();

        //String ascii = processData.hex2AsciiStr(processData.byte2hex(respuestaBytes));

        String ascii = "0176PP1100021020020013006000014140060010231500610230016008201911191700635225719012000000871703200082610010324004231225016AUTORIZACION OK.260020227025520081XXXXXX3015 3406460DB5C875ECC47CDF073FEB16C86A2664D77CA87CD1D111F0FCAC55052160A5535020Mastercard 36001050025MASTERCARD/MAES 510020352013VILLA/DOLORES54020A0000000041010 58010000000800059004E8009020C3220C6D737691999DB055F5D928";

        String asscii = "0176PP1100021020020013006000014140060010231500610230016008201911191700635225719012000000871703200082610010324004231225016AUTORIZACIONOK.260020227025520081XXXXXX30153406460DB5C875ECC47CDF073FEB16C86A2664D77CA87CD1D111F0FCAC55052160A5535020Mastercard36001050025MASTERCARD/MAES510020352013VILLA/DOLORES54020A000000004101058010000000800059004E8009020C3220C6D737691999DB055F5D928";

        //parsera la informacion
        PPParser parser = new PPParser();
        ParsedPP response = parser.parse(asscii.trim());

        PPParser parser2 = new PPParser();
        ParsedPP response2 = parser2.parse(ascii.trim());

        log.info("Data recibida: {}",  ascii);

        String hashReceived = ascii.substring(ascii.length() - 32);
        log.info("Hash recibida: {}",  hashReceived);
        log.info("Status hash recibido {}", cifrado.validateHash(hashReceived));

        System.out.println("------------------------------------------------Response------------------------------------");

        response.getTlv().forEach((k,v)-> System.out.println(k + " = " + v));

        System.out.println("-------------------------------------------------Responde 2-----------------------------------");

        response2.getTlv().forEach((k,v)-> System.out.println(k + " = " + v));

        System.out.println("-------------------------------------------------Responde-----------------------------------");

        System.out.println(response);

        System.out.println("-------------------------------------------------Responde 2-----------------------------------");

        System.out.println(response2);
    }

}
