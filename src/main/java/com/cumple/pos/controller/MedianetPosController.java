package com.cumple.pos.controller;

import com.cumple.pos.models.DatosEnvioPP;
import com.cumple.pos.models.PagoResponse;
import com.cumple.pos.service.MedianetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pos")
@CrossOrigin("*")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class MedianetPosController {

    private final MedianetService service;

    @PostMapping("/medianet/transaccion/{puerto}/{ip}")
    public ResponseEntity<PagoResponse> recibir(@PathVariable int puerto,
                                                @PathVariable String ip,
                                                @RequestBody DatosEnvioPP datosEnvio) {
            PagoResponse p = service.procesarTransaccion(datosEnvio, ip, puerto);
            return ResponseEntity.ok(p);
    }

}