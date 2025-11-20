package com.cumple.pos.command;

import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ObtenerUltimaTransaccionCommand(POS pos, String puerto) implements Command<DatosRecepcion> {

    @Override
    public DatosRecepcion execute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOS(puerto, 9600, 8, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conexcion al POS configurada en el puerto: {}", puerto);

        DatosRecepcion drecepcion = pos.ObtenerUltima();
        log.info("Obteniendo ultima transaccion en el puerto:{} ", puerto);
        return drecepcion;
    }
}
