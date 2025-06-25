package com.cumple.pos.command;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ObtenerUltimaTransaccionLan implements Command<DatosRecepcion> {

    private final POS pos;
    private final String ip;
    private final String puerto;

    @Override
    public DatosRecepcion exceute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOSLAN(ip, puerto, 90000, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conectado al POS via LAN EN {}:{}",  ip, puerto);

        DatosRecepcion dRecepcion = pos.ObtenerUltima();
        log.info("Obteniendo ultima transaccion en el puerto:{} ", puerto);

        return dRecepcion;
    }
}
