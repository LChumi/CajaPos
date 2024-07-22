package com.cumple.pos.command;

import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ObtenerUltimaTransaccionCommand implements Command<DatosRecepcion> {

    private final POS pos;
    private final String puerto;


    @Override
    public DatosRecepcion exceute() throws Exception {
        pos.ConfigurarConexionPOS(puerto, 9600, 8, false);
        log.info("Conexcion al POS configurada en el puerto: {}", puerto);

        DatosRecepcion drecepcion = pos.ObtenerUltima();
        log.info("Obteniendo ultima transaccion en el puerto:{} ",puerto);
        return drecepcion;
    }
}
