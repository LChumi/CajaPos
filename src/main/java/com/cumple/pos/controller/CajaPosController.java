package com.cumple.pos.controller;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.cumple.pos.service.CajaPosService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pos")
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CajaPosController {

    private final CajaPosService cajaPosService;

    @GetMapping("/listaPuertosCom")
    public ResponseEntity<Map<String, String>> listaPuertosCom() {
        try {
            Map<String, String> puertos = cajaPosService.listarPuertos();
            return ResponseEntity.ok(puertos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/procesarPago/{puertoCom}")
    public ResponseEntity<?> recibir(@PathVariable String puertoCom, @RequestBody DatosEnvio datosEnvio) {
        try {
            DatosRecepcion recepcion = cajaPosService.procesarPago(puertoCom, datosEnvio);
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/anular-pago/{puertoCom}")
    public ResponseEntity<DatosRecepcion> anularPago(@PathVariable String puertoCom, @RequestParam String numReferencia) {
        try {
            DatosRecepcion recepcion = cajaPosService.anularPago(puertoCom, numReferencia);
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/ultima-transaccion/{puertoCom}")
    public ResponseEntity<DatosRecepcion> obtenerUltimaTransaccion(@PathVariable String puertoCom) {
        try {
            DatosRecepcion recepcion = cajaPosService.obtenerUltima(puertoCom);
            if (recepcion == null || recepcion.getMensajeResultado() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/procesar_pago_lan/{puerto}/{ip}")
    public ResponseEntity<?> recibir(@PathVariable String puerto,
                                     @PathVariable String ip,
                                     @RequestBody DatosEnvio datosEnvio){
        try {
            DatosRecepcion recepcion = cajaPosService.procesarPagoLan(puerto, ip, datosEnvio);
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/anular_pago_lan/{puerto}/{ip}")
    public ResponseEntity<DatosRecepcion> anularPago(@PathVariable String puerto,
                                                     @PathVariable String ip,
                                                     @RequestParam String numReferencia) {
        try {
            DatosRecepcion recepcion = cajaPosService.anularPagoLan(puerto, ip, numReferencia);
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/ultima_transaccion_lan/{puerto}/{ip}")
    public ResponseEntity<DatosRecepcion> obtenerUltimaTransaccion(@PathVariable String puerto,
                                                                   @PathVariable String ip) {
        try {
            DatosRecepcion recepcion = cajaPosService.obtenerUltimaLan(puerto,ip);
            if (recepcion == null || recepcion.getMensajeResultado() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(recepcion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
