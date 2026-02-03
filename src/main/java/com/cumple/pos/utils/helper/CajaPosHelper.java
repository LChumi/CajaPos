package com.cumple.pos.utils.helper;

import cajapinpad.CifradoTramas;
import cajapinpad.ProccessData;
import com.cumple.pos.models.DatosEnvioPP;
import com.cumple.pos.utils.enums.CampoPP;

import static com.cumple.pos.utils.DateUtils.obtenerFechaActual;
import static com.cumple.pos.utils.DateUtils.obtenerHoraActual;

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

        String[] campos = new String[5];
        campos[0] = "LT"; //identificador
        campos[1] = "11001";
        campos[2] = "06012";
        campos[3] = mont;

        // hash rellenado a la derecha
        campos[4] = processData.padright(cifrado.getHash(), 32 , ' ');

        return processData.getFinalData(campos);
    }

    //Campos para un PP (Proceso de Pago, compras)
    public byte[] buildCompraBytes(DatosEnvioPP dEnvio){

        String subtotal = normalizarMonto(dEnvio.getSubtotal());
        String iva =  normalizarMonto(dEnvio.getIva());
        String subtotal0 = normalizarMonto(dEnvio.getSubtotal0());
        String total = normalizarMonto(dEnvio.getTotal());

        // 11 campos + hash = 12
        String[] campos = new String[15];
        String secuencia = "11012";
        getCampos(dEnvio, secuencia, subtotal, iva, subtotal0, total, campos);
        campos[14] = processData.padright(cifrado.getHash(), 32 , ' '); // hash
        return processData.getFinalData(campos);
    }

    ///  Campos para anulaciones
    public byte[] buildAnulacionBytes(DatosEnvioPP dEnvio){
        String subtotal = normalizarMonto(dEnvio.getSubtotal());
        String iva =  normalizarMonto(dEnvio.getIva());
        String subtotal0 = normalizarMonto(dEnvio.getSubtotal0());
        String total = normalizarMonto(dEnvio.getTotal());

        String[] campos = new String[19];
        String secuencia = "11017";

        getCampos(dEnvio, subtotal, secuencia, iva, subtotal0, total, campos);
        campos[15] = CampoPP.REFERENCIA.build(dEnvio.getReferencia());
        campos[16] = CampoPP.LOTE.build(dEnvio.getLote());
        campos[17] = CampoPP.NUMERO_AUTORIZACION.build(dEnvio.getNumeroAutorizacion());
        campos[18] = processData.padright(cifrado.getHash(), 32 , ' '); // hash

        return processData.getFinalData(campos);
    }

    private void getCampos(DatosEnvioPP dEnvio, String secuencia, String subtotal, String iva, String subtotal0, String total, String[] campos) {
        campos[0] = "PP"; //Identificador del mensaje
        campos[1] = secuencia;
        campos[2] = CampoPP.TIPO_TRANSACCION.build(dEnvio.getTipoTransaccion()); //03 Anulaciones
        campos[3] = CampoPP.CODIGO_DIFERIDO.build(dEnvio.getCodigoDiferido()); //00 Compra Corriente resto tipos de diferido
        //campos[4] = CampoPP.PLAZO.build(dEnvio.getPlazo()); // 00 plazo
        campos[4] = CampoPP.MONTO_TOTAL.build(total); // Monto total de la Transaccion
        campos[5] = CampoPP.SUBTOTAL_IVA.build(subtotal); //Monto base que graba el IVA
        campos[6] = CampoPP.SUBTOTAL0.build(subtotal0); //Monto base que no graba el IVA
        campos[7] = CampoPP.IVA.build(iva); // Impuesto de la transaccion
        campos[8] = CampoPP.HORA.build(obtenerHoraActual());
        campos[9] = CampoPP.FECHA.build(obtenerFechaActual());
        campos[10] = CampoPP.PUNTO_VENTA.build(dEnvio.getPVenta());
        campos[11] = CampoPP.MID.build(dEnvio.getMid());
        campos[12] = CampoPP.TID.build(dEnvio.getTid());
        campos[13] = CampoPP.CID.build(dEnvio.getCid());
    }


}
