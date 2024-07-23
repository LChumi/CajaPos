package com.cumple.pos.controller;

import com.cumple.pos.models.SystemStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

@RestController
@RequestMapping("/client")
@Slf4j
public class ClientController {

    private final SystemInfo systemInfo = new SystemInfo();
    private long[] prevTicks = new long[CentralProcessor.TickType.values().length];

    @GetMapping("/status")
    public ResponseEntity<SystemStatus> getSystemStatus() {
        try {
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            GlobalMemory memory = systemInfo.getHardware().getMemory();
            OperatingSystem os = systemInfo.getOperatingSystem();

            double cpuLoad ;
            try {
                cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
                prevTicks = processor.getSystemCpuLoadTicks();
            } catch (Exception e) {
                cpuLoad = -1;
            }

            SystemStatus sys= new SystemStatus(
                    os.toString(),
                    cpuLoad,
                    memory.getAvailable() / (1024 * 1024),
                    memory.getTotal() / (1024 * 1024)
            );
            return ResponseEntity.ok(sys);
        }catch (Exception e){
            log.error("Error en el servicio message: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
