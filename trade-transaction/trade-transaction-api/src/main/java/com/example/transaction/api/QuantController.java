package com.example.transaction.api;

import com.example.transaction.service.QuantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuantController {

    @Autowired
    QuantService quantService;

    @PostMapping("quant/rolling-window")
    public Object rollingWindowQuant(@RequestParam("json_req") String json) {
        return quantService.rollingWindowQuant(json);
    }

    @PostMapping("quant/in-outside-disc")
    public Object inOutsideQuant(@RequestParam("json_req") String json) {
        return quantService.inOutsideDiscQuant(json);
    }
}
