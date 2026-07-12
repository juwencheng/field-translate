package io.github.juwencheng.fieldtranslate.testapp;

import io.github.juwencheng.fieldtranslate.autoconfigure.annotation.EnableFieldTranslate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 字段翻译框架演示应用。
 *
 * @author juwencheng
 */
@SpringBootApplication
@EnableFieldTranslate
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}
