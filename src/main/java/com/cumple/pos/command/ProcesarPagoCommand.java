package com.cumple.pos.command;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProcesarPagoCommand implements Command<DatosRecepcion> {

    private final POS pos;
    private final DatosEnvio datosEnvio;
    private final String puerto;

    @Override
    public DatosRecepcion exceute() throws Exception {
        pos.ConfigurarConexionPOS(puerto,9600,8,false);
        log.info("Conexion al POS configurada en el puerto: {}", puerto);

        DatosRecepcion dRecepcion = pos.ProcesarPago(datosEnvio);
        log.info("Estado:{}, transaccion en curso:{}",pos.getStatus(),pos.getTransaccionEnCurso());

        return dRecepcion;
    }
}
