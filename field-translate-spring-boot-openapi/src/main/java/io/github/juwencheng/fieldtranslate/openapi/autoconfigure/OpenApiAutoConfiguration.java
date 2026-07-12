package io.github.juwencheng.fieldtranslate.openapi.autoconfigure;

import io.github.juwencheng.fieldtranslate.core.translate.TranslateStrategy;
import io.github.juwencheng.fieldtranslate.openapi.TranslateOperationCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * OpenAPI 模块自动配置。仅在 springdoc-openapi 存在时激活。
 * <p>
 * 通过 {@link TranslateOperationCustomizer} 自动为标注了翻译注解的
 * Controller 方法增强其 OpenAPI 文档描述。
 *
 * @author juwencheng
 */
@AutoConfiguration
@ConditionalOnClass(OperationCustomizer.class)
@AutoConfigureAfter(name = "io.github.juwencheng.fieldtranslate.autoconfigure.FieldTranslateAutoConfiguration")
@ConditionalOnBean(TranslateStrategy.class)
public class OpenApiAutoConfiguration {

    @Bean
    public OperationCustomizer translateOperationCustomizer() {
        return new TranslateOperationCustomizer();
    }
}
