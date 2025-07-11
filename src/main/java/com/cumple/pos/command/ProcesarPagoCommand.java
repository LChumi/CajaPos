package com.cumple.pos.command;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ProcesarPagoCommand implements Command<DatosRecepcion> {

    private final POS pos;
    private final DatosEnvio datosEnvio;
    private final String puerto;

    @Override
    public DatosRecepcion exceute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOS(puerto, 9600, 8, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conexion al POS configurada en el puerto: {}", puerto);

        DatosRecepcion dRecepcion = pos.ProcesarPago(datosEnvio);
        log.info("Estado:{}, transaccion en curso:{}", pos.getStatus(), pos.getTransaccionEnCurso());

        return dRecepcion;
    }
}
