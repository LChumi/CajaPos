package com.cumple.pos.service;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CajaPosService {

    private static final int BAUD_RATE = 9600;
    private static final int DATABITS_DEFAULT=8;
    private static final boolean BUSCAR_PUERTOS=false;

    private final POS pos = new POS(true);

    public DatosRecepcion procesarPago(String puerto, DatosEnvio datosEnvio)throws Exception{
        pos.ConfigurarConexionPOS(puerto,BAUD_RATE,DATABITS_DEFAULT,BUSCAR_PUERTOS);
        return pos.ProcesarPago(datosEnvio);
    }

    public DatosRecepcion anularPago(String numeroReferencia){
        try {
            return pos.AnularPago(numeroReferencia);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DatosRecepcion obtenerUltima(){
        try {
            return pos.ObtenerUltima();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> listaPuerto() {
        List<String> listaPuertosCom= new ArrayList<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            log.error("No se encontraron puertos COM disponibles");
            return new ArrayList<>();
        } else {
            log.info("Puertos COM disponibles ");
            for (SerialPort port : ports) {
                log.info("Puerto encontrado: {} ", port.getSystemPortName());
                listaPuertosCom.add(port.getDescriptivePortName());
            }
        }
        return listaPuertosCom;
    }
}
