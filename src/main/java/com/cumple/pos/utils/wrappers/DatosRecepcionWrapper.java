package com.cumple.pos.utils.wrappers;

import com.DF.COM.obj.DatosRecepcion;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DatosRecepcionWrapper {
    private DatosRecepcion datosRecepcion;

    public String getMensajeResultado() {
        try {
            if (datosRecepcion.getCodigoResultado() != null && !datosRecepcion.getCodigoResultado().isEmpty()) {
                int codigoResultado = Integer.parseInt(datosRecepcion.getCodigoResultado());
                
                switch (codigoResultado){
                    case 0:
                        return  "Transacción Aprobada";
                    case 1:
                        return  "Transacción declinada";
                    case 2:
                        return  "Consulte a su emisor";
                    case 3:
                        return  "Establecimiento Inválido";
                    case 4:
                        return  "Retenga tarjeta y llame";
                    case 5:
                        return  "Transacción Rechazada";
                    case 12:
                        return  "Transacción Inválida";
                    case 13:
                        return  "Monto Inválido";
                    case 14:
                        return  "Error en número de tarjeta";
                    case 15:
                        return  "Error en número de tarjeta";
                    case 17:
                        return  "Socio cancelado";
                    case 18:
                        return  "Error en fecha";
                    case 19:
                        return  "Por favor reintente";
                    case 25:
                        return  "Transacción incorrecta";
                    case 30:
                        return  "Error en formato";
                    case 31:
                        return  "Banco no aceptado";
                    case 38:
                        return  "Error en número de tarjeta";
                    case 39:
                        return  "No tarjeta de crédito";
                    case 41:
                        return  "Retenga tarjeta y llame";
                    case 43:
                        return  "Retenga tarjeta y llame";
                    case 51:
                        return  "Cupo no disponible";
                    case 52:
                        return  "Error en tipo de cuenta";
                    case 53:
                        return  "Error en tipo de cuenta";
                    case 54:
                        return  "Tarjeta expirada";
                    case 55:
                        return  "PIN Incorrecto";
                    case 56:
                        return  "Retenga tarjeta y llame";
                    case 57:
                        return  "Transación Inválida";
                    case 58:
                        return  "Servicio Inválido";
                    case 61:
                        return  "Monto límite excedido";
                    case 62:
                        return  "Tarjeta no permitida";
                    case 65:
                        return  "Límite de transacciones excedido";
                    case 67:
                        return  "Retenga tarjeta y llame";
                    case 68:
                        return  "returnpuesta tardía";
                    case 75:
                        return  "Límite de PIN excedido";
                    case 76:
                        return  "Cuenta inválida";
                    case 77:
                        return  "Modalidad inválida";
                    case 78:
                        return  "Tarjeta no procesable";
                    case 79:
                        return  "Vigencia errada";
                    case 80:
                        return  "Establecimiento cancelado";
                    case 81:
                        return  "Número de cuotas errado";
                    case 82:
                        return  "Autorización no existe";
                    case 85:
                        return  "Llamar a emisor";
                    case 87:
                        return  "KR Reintente";
                    case 88:
                        return  "Favor reintente";
                    case 89:
                        return  "Terminal inválido";
                    case 91:
                        return  "Entidad fuera de línea";
                    case 92:
                        return  "Error en número de tarjeta";
                    case 93:
                        return  "No disponible";
                    case 94:
                        return  "No disponible";
                    case 95:
                        return  "Descuadre preturnentado";
                    case 96:
                        return  "Entidad fuera de línea";
                    case 99:
                        return  "KR Reintente";
                    default:
                        return "Codigo de resultado Desconocido";
                }
            }else {
                return "Codigo de resultado vacio";
            }
        }catch (NumberFormatException e) {
            return "ERROR" + e.getMessage();
        }
    }
}
