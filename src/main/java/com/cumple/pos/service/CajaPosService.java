package com.cumple.pos.service;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.command.AnularPagoCommand;
import com.cumple.pos.command.ObtenerUltimaTransaccionCommand;
import com.cumple.pos.command.ProcesarPagoCommand;
import com.cumple.pos.utils.StringValidatorsUtils;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CajaPosService {

    public DatosRecepcion procesarPago(String puerto, DatosEnvio datosEnvio) throws Exception{
        if (!StringValidatorsUtils.validarFormatoPuerto(puerto)){
            throw new IllegalArgumentException("Formato de puerto no valido");
        }
        if (datosEnvio == null){
            throw new IllegalArgumentException("Datos de envio no valido");
        }

        POS pos = new POS(false);
        try {
            ProcesarPagoCommand command = new ProcesarPagoCommand(pos,datosEnvio, puerto);
            return command.exceute();
        }catch (Exception e){
            log.error("Ocurrio un problema al procesar el pago en el puerto: {} , message: {} ",puerto,e.getMessage());
            throw  new Exception("ERROR "+e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion anularPago(String puerto, String numeroReferencia) throws Exception{
        if (!StringValidatorsUtils.validarFormatoPuerto(puerto)){
            throw new IllegalArgumentException("Formato de puerto no valido");
        }

        POS pos = new POS(false);
        try {
            AnularPagoCommand command = new AnularPagoCommand(pos,puerto,numeroReferencia);
            return command.exceute();
        }catch (Exception e){
            log.error("Ocurrio un problema al anular el pago en el puert: {}, message: {}",puerto,e.getMessage());
            throw new RuntimeException("ERROR "+e.getMessage());
        }finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion obtenerUltima(String puerto) throws Exception{
        if (!StringValidatorsUtils.validarFormatoPuerto(puerto)){
            throw new IllegalArgumentException("Formato de puerto no valido");
        }

        POS pos = new POS(false);
        try {
            ObtenerUltimaTransaccionCommand command = new ObtenerUltimaTransaccionCommand(pos,puerto);
            return command.exceute();
        }catch (Exception e){
            log.error("Error al obtener la ultima transaccion en el puerto: {}",puerto);
            throw new RuntimeException("ERROR "+e.getMessage());
        }finally {
            desconectarPuerto(pos);
        }
    }

    public Map<String, String> listarPuertos(){
        Map<String, String> response = new HashMap<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0){
            log.error("Nose encontraron puertos COM disponibles");
            return null;
        } else {
            log.info("Puertos COM disponibles");
            for (SerialPort port : ports){
                log.info("Descripción del puerto: {}, nombre del puerto COM: {}, nombre del dispositivo: {}",
                        port.getDescriptivePortName(), port.getSystemPortName(), port.getPortDescription());
                response.put(port.getSystemPortName(),port.getPortDescription());
            }
        }
        return response;
    }

    private void desconectarPuerto(POS pos){
        boolean desconectando = pos.DesconectarPuerto();
        log.warn("Desconectando puerto: {}", desconectando);
    }


}