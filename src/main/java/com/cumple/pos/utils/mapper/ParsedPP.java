package com.cumple.pos.utils.mapper;

import com.cumple.pos.models.PagoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedPP {

    private String raw;
    private String sinHash;
    private String hash;
    private boolean hashOk;
    private Map<String, String> tlv;
    private PagoResponse pago;
}
