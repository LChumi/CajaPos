package com.cumple.pos.service;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CajaPosService {

    private static final int BAUD_RATE = 9600;
    private static final int DATABITS_DEFAULT = 8;
    private static final boolean BUSCAR_PUERTOS = false;

    private synchronized  void configurarConexionSiEsNecesario(POS pos, String puerto) throws Exception {
        pos.ConfigurarConexionPOS(puerto, BAUD_RATE, DATABITS_DEFAULT,BUSCAR_PUERTOS);
        log.info("Conexion al POS configurada en el puerto: {}", puerto);
    }

    public DatosRecepcion procesarPago(String puerto, DatosEnvio datosEnvio) throws Exception {
        POS pos = new POS(false);
        try {
            configurarConexionSiEsNecesario(pos,puerto);

            DatosEnvio dEnvio = new DatosEnvio();
            dEnvio.setBaseImponible(datosEnvio.getBaseImponible());
            dEnvio.setTipoCredito(datosEnvio.getTipoCredito());
            dEnvio.setBase0(datosEnvio.getBase0());
            dEnvio.setCuotas(datosEnvio.getCuotas());
            dEnvio.setFin(datosEnvio.getFin());
            dEnvio.setIVA(datosEnvio.getIVA());

            DatosRecepcion dRecepcion = new DatosRecepcion();
            dRecepcion=pos.ProcesarPago(dEnvio);

            log.info("Estado: {}, transaccion en curso: {}",pos.getStatus(), pos.getTransaccionEnCurso());
            desconectarPuerto(pos);
            return dRecepcion;
        } catch (Exception e){
            desconectarPuerto(pos);
            log.error("Ocurrio un problema al procesar pago en el puerto:{}, message:{}",puerto,e.getMessage());
            throw new Exception("ERROR "+e.getMessage());
        }
    }

    public DatosRecepcion anularPago(String puerto, String numeroReferencia){
        POS pos = new POS(false);
        try {
            configurarConexionSiEsNecesario(pos,puerto);
            DatosRecepcion dRecepcion = new DatosRecepcion();
            dRecepcion = pos.AnularPago(numeroReferencia);

            log.info("Anulacion de Pago {}",numeroReferencia);
            desconectarPuerto(pos);
            return dRecepcion;
        } catch (Exception e){
            desconectarPuerto(pos);
            log.error("Ocurrio un problemas al anular el pago en el puerto:{}, message:{}",puerto,e.getMessage());
            throw new RuntimeException("ERROR: "+e.getMessage());
        }
    }

    public DatosRecepcion obtenerUltima(String puerto) {
        POS pos = new POS(false);
        try{
            configurarConexionSiEsNecesario(pos,puerto);
            DatosRecepcion dRecepcion = new DatosRecepcion();
            dRecepcion = pos.ObtenerUltima();

            log.info("Obteniendo ultima transaccion en el puerto:{} ",puerto);
            desconectarPuerto(pos);
            return dRecepcion;
        }catch (Exception e){
            desconectarPuerto(pos);
            log.error("Error al obtener la utlima transaccion ");
            throw new RuntimeException("ERROR: "+e.getMessage());
        }
    }

    public Map<String,String> listarPuerto() {
        Map<String,String> response = new HashMap<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0){
            log.error("No se encontraron puertos COM disponibles");
            return null;
        } else {
            log.info("Puertos COM disponibles");
            for (SerialPort port : ports) {
                log.info("Descripcion del puerto:{}, nombre del puerto COM:{}, nombre del dispositivo:{} ",
                        port.getDescriptivePortName(),port.getSystemPortName(),port.getPortDescription());
                 response.put(port.getSystemPortName(),port.getPortDescription());
            }
        }
        return response;
    }

    private void desconectarPuerto(POS pos){
        boolean desconectando = pos.DesconectarPuerto();
        log.info("Desconectando puerto: {}", desconectando);
    }
}