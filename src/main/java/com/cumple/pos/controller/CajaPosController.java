package com.cumple.pos.controller;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.cumple.pos.service.CajaPosService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
