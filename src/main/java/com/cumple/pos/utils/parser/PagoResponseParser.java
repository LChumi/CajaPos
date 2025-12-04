package com.cumple.pos.utils.parser;

import java.util.Map;

public class PagoResponseParser {

    public static final Map<Integer, Integer> LONGITUDES_FIJAS = Map.ofEntries(
            Map.entry(2, 2),
            Map.entry(13, 6),
            Map.entry(14, 6),
            Map.entry(15, 6),
            Map.entry(16, 8),
            Map.entry(17, 6),
            Map.entry(19, 15),
            Map.entry(20, 8),
            Map.entry(24, 4),
            Map.entry(26, 2),
            Map.entry(30, 3),
            Map.entry(31, 3),
            Map.entry(51, 2),
            Map.entry(53, 12),
            Map.entry(54, 20),
            Map.entry(59, 4)
    );
}
