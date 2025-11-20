package com.cumple.pos.command;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ProcesarPagoLan(POS pos, DatosEnvio datosEnvio, String ip,
                              String puerto) implements Command<DatosRecepcion> {

    @Override
    public DatosRecepcion execute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOSLAN(ip, puerto, 90000, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conectado al POS via LAN EN {}:{}", ip, puerto);

        getTipotransaccion(datosEnvio);

        DatosRecepcion dRecepcion;
        try {
            dRecepcion = pos.ProcesarPago(datosEnvio);
        } catch (NullPointerException npe){
            log.warn("El POS devolvió un campo P57 nulo, se ignora publicidad/intereses");
            log.debug("Detalle del error POS", npe);
            dRecepcion = pos.ObtenerUltima();
            if (dRecepcion == null) {
                throw new Exception("No se pudo recuperar la transacción, respuesta POS inválida");
            }
        }
        log.info("Estado:{}, transaccion en curso:{}", pos.getStatus(), pos.getTransaccionEnCurso());

        return dRecepcion;
    }

    private void getTipotransaccion(DatosEnvio datosEnvio) {
        switch (datosEnvio.getTipoCredito()) {
            case DiferidoCorriente:
            case DiferidoConIntereses:
            case DiferidoSinIntereses:
                datosEnvio.setTipoTransaccion("1");
                break;
            default:
                datosEnvio.setTipoTransaccion("0");
                break;
        }
    }

}
