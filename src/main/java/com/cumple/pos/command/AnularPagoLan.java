package com.cumple.pos.command;

import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record AnularPagoLan(POS pos, String puerto, String ip,
                            String numeroReferencia) implements Command<DatosRecepcion> {

    @Override
    public DatosRecepcion execute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOSLAN(ip, puerto, 90000, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conectado al POS via LAN EN {}:{}", ip, puerto);

        DatosRecepcion drecepcion = pos.AnularPago(numeroReferencia);
        log.info("Anulacion de pago ref: {}", numeroReferencia);

        return drecepcion;
    }
}
