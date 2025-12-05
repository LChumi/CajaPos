package com.cumple.pos.service;

import cajapinpad.CifradoTramas;
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

    public PagoResponse ProcesarPago(DatosEnvioPP dEnvio, String ip, int puerto){
        byte[] dataSend = helper.buildCompraBytes(dEnvio);

        log.info("Enviando Pago: {}" , processData.hex2AsciiStr(processData.byte2hex(dataSend)));

        return getConexion(ip, puerto, dataSend);
    }

    private PagoResponse getConexion(String ipPos, int puertoPos, byte[] datasend) {
        //conexion.sendData(ipPos, puertoPos, datasend, 30000);

        //byte[] respuestaBytes= conexion.getDataRecived();

        //String ascii = processData.hex2AsciiStr(processData.byte2hex(respuestaBytes));


        System.out.println("----------------------------------------****************************************************---------------------------------------------------------------------------");

        String t2 = "016FPP110002102002001300600003814006900246150060940011600820240718170067060711901200000087170320008200790342400427082501900 AUTORIZACION OK.26002022701648137930XXXX9914340642357B934A11FE68E277BB5131D9EA68AE3084DD7080693707B38830B6A0349DF35020VISA CREDITO        36001050025VISA/ELECTRON            510020752012PAYWAVE/VISA54020A0000000031010 580100000000000590040000600355685799FE42F49E9FF02D5EEC87";

        System.out.println("----- PARSING 1 -----");
        PagoResponse r2 = parse(t2);
        System.out.println(r2);
        return r2;
    }

}
