package io.github.juwencheng.fieldtranslate.testapp.controller;

import io.github.juwencheng.fieldtranslate.core.translate.TranslateResponse;
import io.github.juwencheng.fieldtranslate.testapp.dto.OrderWithDict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示控制器。
 *
 * @author juwencheng
 */
@RestController
public class TestController {

    @GetMapping("/test/orderWithDict")
    @TranslateResponse
    public OrderWithDict getOrderWithDict() {
        return new OrderWithDict();
    }
}
