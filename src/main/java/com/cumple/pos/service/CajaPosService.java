package com.cumple.pos.service;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.command.*;
import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.DF.Enum.ETipoConexion.LAN;
import static com.DF.Enum.ETipoConexion.SERIAL;
import static com.cumple.pos.utils.StringValidatorsUtils.*;

@Service
@Slf4j
public class CajaPosService {

    public DatosRecepcion procesarPago(String puerto, DatosEnvio datosEnvio) throws Exception {
        if (validarFormatoPuertoCom(puerto)) {
            throw new IllegalArgumentException("Formato de puerto no valido");
        }
        if (datosEnvio == null) {
            throw new IllegalArgumentException("Datos de envio no valido");
        }

        POS pos = new POS(false, SERIAL);
        try {
            ProcesarPagoCommand command = new ProcesarPagoCommand(pos, datosEnvio, puerto);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Ocurrio un problema al procesar el pago en el puerto: {} , message: {} ", puerto, e.getMessage(), e);
            throw new Exception("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion anularPago(String puerto, String numeroReferencia) {
        if (validarFormatoPuertoCom(puerto)) {
            throw new IllegalArgumentException("Formato de puerto no valido");
        }

        POS pos = new POS(false, SERIAL);
        try {
            AnularPagoCommand command = new AnularPagoCommand(pos, puerto, numeroReferencia);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Ocurrio un problema al anular el pago en el puert: {}, message: {}", puerto, e.getMessage());
            throw new RuntimeException("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion obtenerUltima(String puerto) {
        if (validarFormatoPuertoCom(puerto)) {
            throw new IllegalArgumentException("Formato de puerto no valido");
        }

        POS pos = new POS(false, SERIAL);
        try {
            ObtenerUltimaTransaccionCommand command = new ObtenerUltimaTransaccionCommand(pos, puerto);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Error al obtener la ultima transaccion en el puerto: {}", puerto);
            throw new RuntimeException("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public Map<String, String> listarPuertos() {
        Map<String, String> response = new HashMap<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            log.error("Nose encontraron puertos COM disponibles");
            return null;
        } else {
            log.info("Puertos COM disponibles");
            for (SerialPort port : ports) {
                log.info("Descripción del puerto: {}, nombre del puerto COM: {}, nombre del dispositivo: {}",
                        port.getDescriptivePortName(), port.getSystemPortName(), port.getPortDescription());
                response.put(port.getSystemPortName(), port.getPortDescription());
            }
        }
        return response;
    }

    public DatosRecepcion procesarPagoLan(String puerto, String ip, DatosEnvio datosEnvio) throws Exception {
        if (datosEnvio == null) {
            throw new IllegalArgumentException("Datos de envio no valido");
        }

        if (!validarFormatoIP(ip) && !validarFormatoPuertoNumerico(puerto)) {
            throw new IllegalArgumentException("Formato de IP no valido");
        }

        POS pos = new POS(false, LAN);
        try {
            ProcesarPagoLan command = new ProcesarPagoLan(pos, datosEnvio,ip, puerto);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Ocurrio un problema al procesar el pago via LAN en: {}:{}",ip, puerto, e);
            desconectarPuerto(pos);
            throw new Exception("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion anularPagoLan(String puerto, String ip, String numeroReferencia) {

        if (!validarFormatoIP(ip) && !validarFormatoPuertoNumerico(puerto)) {
            throw new IllegalArgumentException("Formato de IP no valido");
        }

        POS pos = new POS(false, LAN);
        try {
            AnularPagoLan command = new AnularPagoLan(pos, puerto, ip, numeroReferencia);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Ocurrio un problema al anular el pago via LAN en: {}:{}, message: {}",ip, puerto, e.getMessage());
            throw new RuntimeException("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    public DatosRecepcion obtenerUltimaLan(String puerto, String ip) {

        if (!validarFormatoIP(ip) && !validarFormatoPuertoNumerico(puerto)) {
            throw new IllegalArgumentException("Formato de IP no valido");
        }

        POS pos = new POS(false, SERIAL);
        try {
            ObtenerUltimaTransaccionLan command= new ObtenerUltimaTransaccionLan(pos,ip, puerto);
            DatosRecepcion dRecepcion = command.exceute();

            validarDatosRecepcion(dRecepcion);
            return dRecepcion;
        } catch (Exception e) {
            log.error("Error al obtener la ultima transaccion via LAN en: {}:{}",ip, puerto);
            throw new RuntimeException("ERROR " + e.getMessage());
        } finally {
            desconectarPuerto(pos);
        }
    }

    private void desconectarPuerto(POS pos){
        try{
            boolean desconectado = pos.DesconectarPuerto();
            if (!desconectado) {
                log.warn("Reintentando desconexion...");
                desconectado = pos.DesconectarPuerto();
            }
            pos.dispose();; //Asegura la liberacion de recursos
            log.info("Pos desconectado: {}", desconectado);
        } catch (IOException ex) {
            log.error("Error al desconectar puerto: {}", ex.getMessage());
        }
    }

    private void validarDatosRecepcion(DatosRecepcion dRecepciom) {
        if (dRecepciom.getCodigoResultado() == null || dRecepciom.getCodigoResultado().isEmpty()) {
            dRecepciom.setCodigoResultado("99");
        }
    }
}