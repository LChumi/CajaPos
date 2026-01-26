package com.cumple.pos.models;

import lombok.Data;

@Data
public class DatosEnvioPP {
    double subtotal;
    double subtotal0;
    double iva;
    double total;
    String tipoTransaccion;
    String codigoDiferido;
    String plazo;
    String mid;
    String tid;
    String cid;
    String pVenta;
    //Campos anulacion
    private String referencia;
    private String lote;
    private String numeroAutorizacion;
}
