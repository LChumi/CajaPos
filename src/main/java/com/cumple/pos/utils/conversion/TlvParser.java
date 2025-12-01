package com.cumple.pos.utils.conversion;

import java.util.LinkedHashMap;
import java.util.Map;

public class TlvParser {

    public Map<String, String> parse(String data){
        Map<String, String> out = new LinkedHashMap<>();
        int i = 0;

        while(i + 5 < data.length()){
            String cc = data.substring(i , i + 2);
            String lenStr = data.substring(i + 2 , i + 5);

            if (esNumero(cc) || esNumero(lenStr)){
                break;
            }

            int len = Integer.parseInt(lenStr);
            int start = i + 5 ;
            int end = start + len;

            if (end > data.length()) break;

            String value = data.substring(start, end);
            out.put(cc, value);

            i = end;
        }

        return out;
    }

    private boolean esNumero(String data){
        for (char c : data.toCharArray()){
            if (!Character.isDigit(c)) return true;
        }
        return false;
    }

}
