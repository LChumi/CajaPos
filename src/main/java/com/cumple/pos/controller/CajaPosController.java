package com.cumple.pos.controller;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.cumple.pos.service.CajaPosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/com")
@CrossOrigin("*")
public class CajaPosController {

    @Autowired
    private CajaPosService cajaPosService;

    @PostMapping("pruebaDatos/{puertoCom}")
    public DatosRecepcion recibir(@PathVariable String puertoCom,@RequestBody DatosEnvio datosEnvio) throws Exception {
        try {
            return cajaPosService.procesarPago(puertoCom,datosEnvio);
        }catch (Exception e){
            throw new RuntimeException("No se puedo enviar datos el COM",e);
        }
    }

    @GetMapping("/listaPuertosCom")
    public List<String> listaPuertosCom() {
        return cajaPosService.listaPuerto();
    }

    @PostMapping("/anular-pago")
    public ResponseEntity<DatosRecepcion> anularPago(@RequestParam String numReferencia) {
        DatosRecepcion recepcion = cajaPosService.anularPago(numReferencia);
        if (recepcion != null) {
            return ResponseEntity.ok(recepcion);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ultima-transaccion")
    public ResponseEntity<DatosRecepcion> obtenerUltimaTransaccion() {
        DatosRecepcion recepcion = cajaPosService.obtenerUltima();
        if (recepcion != null) {
            return ResponseEntity.ok(recepcion);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
