package com.cumple.pos.controller;

import com.DF.COM.obj.DatosEnvio;
import com.DF.COM.obj.DatosRecepcion;
import com.cumple.pos.service.CajaPosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/pos")
@CrossOrigin("*")
public class CajaPosController {

    @Autowired
    private CajaPosService cajaPosService;

    @PostMapping("/procesarPago/{puertoCom}")
    public ResponseEntity<?> recibir(@PathVariable String puertoCom, @RequestBody DatosEnvio datosEnvio) throws Exception {
        try {
            DatosRecepcion recepcion = cajaPosService.procesarPago(puertoCom,datosEnvio);
            return ResponseEntity.ok(recepcion);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listaPuertosCom")
    public ResponseEntity<Map<String,String>> listaPuertosCom() {
        try {
            Map<String,String> puertos =  cajaPosService.listarPuertos();
            return ResponseEntity.ok(puertos);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/anular-pago/{puertoCom}")
    public ResponseEntity<DatosRecepcion> anularPago(@PathVariable String puertoCom ,@RequestParam String numReferencia) {
        try {
            DatosRecepcion recepcion = cajaPosService.anularPago(puertoCom, numReferencia);
            return ResponseEntity.ok(recepcion);
        }catch (Exception e){
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
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/logs/dowload")
    public ResponseEntity<Resource> dowloadLogs() {
        try {
            Path file = Paths.get("C:\\Pos\\logs\\pos.log");
            Resource resource = new UrlResource(file.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachemen; filename=\""+ file.getFileName().toString()+ "\"")
                    .body(resource);
        }catch (MalformedURLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
