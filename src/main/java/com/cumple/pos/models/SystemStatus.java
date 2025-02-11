package com.cumple.pos.models;


public record SystemStatus(String operatingSystem, double cpuLoad, Long availableMemory, Long totalMemory) {
}
