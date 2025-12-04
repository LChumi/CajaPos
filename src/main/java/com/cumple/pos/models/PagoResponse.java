package com.cumple.pos.models;

import lombok.Data;

@Data
public class PagoResponse {

    private String resultado;
    private String mensajeResultado;
    private String codigoAutorizador;
    private String referencia;
    private String lote;
    private String hora;
    private String fecha;
    private String numeroAutorizacion;
    private String mid;
    private String tid;
    private String panEncriptado;
    private String fechaVencimiento;
    private String codigoRed;
    private String tarjetaTruncada;
    private String bancoAdquirente;
    private String bancoEmisor;
    private String grupoTarjeta;
    private String modoLectura;
    private String nombreTarjetahabiente;
    private String montoFijo;
    private String aid;
    private String tvr;
    private String tsi;
    private boolean aprobada;
}