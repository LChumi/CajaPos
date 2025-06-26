package com.cumple.pos.command;

import com.DF.COM.obj.DatosRecepcion;
import com.DF.POS.POS;
import com.cumple.pos.exception.PosNotConnectedException;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AnularPagoLan  implements Command<DatosRecepcion> {

    private final POS pos;
    private final String puerto;
    private final String ip;
    private final String numeroReferencia;

    @Override
    public DatosRecepcion exceute() throws Exception {
        boolean conectado = pos.ConfigurarConexionPOS(puerto, 9600, 8, false);
        if (!conectado) {
            throw new PosNotConnectedException("No se pudo conectar al POS via LAN");
        }
        log.info("Conectado al POS via LAN EN {}:{}",  ip, puerto);

        DatosRecepcion drecepcion= pos.AnularPago(numeroReferencia);
        log.info("Anulacion de pago ref: {}", numeroReferencia);

        return drecepcion;
    }
}
