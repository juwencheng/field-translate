package io.github.juwencheng.fieldtranslate.jackson.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.juwencheng.fieldtranslate.core.output.SerializationHookContext;
import io.github.juwencheng.fieldtranslate.core.output.TranslateSerializationHook;
import io.github.juwencheng.fieldtranslate.jackson.JacksonTranslateSerializationHook;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Jackson 序列化扩展自动配置。
 *
 * @author juwencheng
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureAfter(name = "io.github.juwencheng.fieldtranslate.autoconfigure.FieldTranslateAutoConfiguration")
public class JacksonTranslateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(TranslateSerializationHook.class)
    public JacksonTranslateSerializationHook jacksonTranslateSerializationHook() {
        return new JacksonTranslateSerializationHook();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonTranslateObjectMapperCustomizer(
            JacksonTranslateSerializationHook hook) {
        return builder -> builder.postConfigurer(objectMapper ->
                hook.register(new SerializationHookContext(objectMapper)));
    }
}
