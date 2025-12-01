package com.cumple.pos.utils.helper;

import cajapinpad.CifradoTramas;
import cajapinpad.ProccessData;
import com.cumple.pos.models.DatosEnvioPP;

public class CajaPosHelper {

    private final ProccessData processData;
    private final CifradoTramas cifrado;

    public CajaPosHelper() {
        this.processData = ProccessData.getInstance();
        this.cifrado = new CifradoTramas();
    }

    //Normalizar mont: 12 digitos sin punto y dos ultimos decimales
    public String normalizarMonto(double monto) {
        long montoDecimales = Math.round(monto * 100);
        return String.format("%012d", montoDecimales);
    }

    //Campos para armar un LT (Lectura de tarjeta)
    public byte[] buildBytes(double monto) {
        String mont = normalizarMonto(monto);

        String[] campos = new String[3];
        campos[0] = "LT"; //identificador
        campos[1] = mont;

        // hash rellenado a la derecha
        campos[2] = processData.padright(cifrado.getHash(), 32 , ' ');

        return processData.getFinalData(campos);
    }

    //Campos para un PP (Proceso de Pago, compras)
    public byte[] buildCompraBytes(DatosEnvioPP dEnvio){

        String subtotal = normalizarMonto(dEnvio.getSubtotal());
        String iva =  normalizarMonto(dEnvio.getIva());
        String subtotal0 = normalizarMonto(dEnvio.getSubtotal0());
        String total = normalizarMonto(dEnvio.getTotal());

        // 11 campos + hash = 12
        String[] campos = new String[17];

        getCampos(dEnvio, subtotal, iva, subtotal0, total, campos);
        campos[10] = dEnvio.getHora();
        campos[11] = dEnvio.getFecha();
        campos[12] = dEnvio.getPVenta();
        campos[13] = dEnvio.getMid();
        campos[14] = dEnvio.getTid();
        campos[15] = dEnvio.getCid();
        campos[16] = processData.padright(cifrado.getHash(), 32 , ' '); // hash

        return processData.getFinalData(campos);
    }

    public byte[] buildAnulacionBytes(DatosEnvioPP dEnvio){
        String subtotal = normalizarMonto(dEnvio.getSubtotal());
        String iva =  normalizarMonto(dEnvio.getIva());
        String subtotal0 = normalizarMonto(dEnvio.getSubtotal0());
        String total = normalizarMonto(dEnvio.getTotal());

        String[] campos = new String[20];

        getCampos(dEnvio, subtotal, iva, subtotal0, total, campos);
        campos[10] = dEnvio.getReferencia();
        campos[11] = dEnvio.getLote();
        campos[12] = dEnvio.getHora();
        campos[13] = dEnvio.getFecha();
        campos[14] = dEnvio.getNumeroAutorizacion();
        campos[15] = dEnvio.getPVenta();
        campos[16] = dEnvio.getMid();
        campos[17] = dEnvio.getTid();
        campos[18] = dEnvio.getCid();
        campos[19] = processData.padright(cifrado.getHash(), 32 , ' '); // hash

        return processData.getFinalData(campos);
    }

    private void getCampos(DatosEnvioPP dEnvio, String subtotal, String iva, String subtotal0, String total, String[] campos) {
        campos[0] = "PP"; //Identificador del mensaje
        campos[1] = dEnvio.getTipoTransaccion(); //03 Anulaciones
        campos[2] = dEnvio.getCodigoDiferido(); //00 Compra Corriente resto tipos de diferido
        campos[3] = dEnvio.getPlazo(); // 00 plazo
        campos[4] = "00"; // 00 mez de gracia
        campos[5] = total; // Monto total de la Transaccion
        campos[6] = subtotal; //Monto base que graba el IVA
        campos[7] = subtotal0; //Monto base que no graba el IVA
        campos[8] = iva; // Impuesto de la transaccion
        campos[9] = subtotal0;
    }

}
