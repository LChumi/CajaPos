package com.cumple.pos.utils.enums;

import lombok.Getter;

public enum CampoPP {
    TIPO_TRANSACCION(1, 2, "Tipo de transacción"),
    CODIGO_DIFERIDO(3, 2, "Código diferido"),
    PLAZO(4, 2, "Plazo de diferido"),
    MESES_GRACIA(5, 2, "Meses de gracia"),
    MONTO_TOTAL(6, 12, "Monto total de la transacción"),
    SUBTOTAL_IVA(7, 12, "Monto base que grava IVA"),
    SUBTOTAL0(8, 12, "Monto base que no grava IVA"),
    IVA(9, 12, "Impuesto IVA de la transacción"),
    REFERENCIA(13, 6, "Referencia"),
    LOTE(14,6, "Lote"),
    HORA(15, 6, "Hora de la transacción (HHMMSS)"),
    FECHA(16, 8, "Fecha de la transacción (AAAAMMDD)"),
    NUMERO_AUTORIZACION(17,6, "Numero de autorizacion"),
    PUNTO_VENTA(18, 5, "Código Identificador único de Caja "),
    MID(19, 12, "Merchant ID"),
    TID(20, 8, "Terminal ID"),
    CID(21, 15, "CID de la caja emparejada al pos");

    private final int codigo;
    private final int longitud;
    @Getter
    private final String descripcion;

    CampoPP(int codigo, int longitud, String descripcion) {
        this.codigo = codigo;
        this.longitud = longitud;
        this.descripcion = descripcion;
    }

    public String build(String valor) {
        // Código siempre 2 dígitos
        String cod = String.format("%02d", codigo);
        // Longitud siempre 3 dígitos
        String len = String.format("%03d", longitud);

        // Normalizar el valor según la longitud del campo
        String val;
        if (valor.matches("\\d+")) {
            // Si es numérico → rellenar con ceros a la izquierda
            val = String.format("%0" + longitud + "d", Long.parseLong(valor));
        } else {
            // Si es alfanumérico → rellenar con ceros a la izquierda
            val = String.format("%" + longitud + "s", valor).replace(' ', '0');
        }
        return cod + len + val;
    }

}