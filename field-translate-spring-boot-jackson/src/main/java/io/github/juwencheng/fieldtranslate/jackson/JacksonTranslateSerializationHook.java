package io.github.juwencheng.fieldtranslate.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.juwencheng.fieldtranslate.core.output.SerializationHookContext;
import io.github.juwencheng.fieldtranslate.core.output.TranslateSerializationHook;

/**
 * Jackson 实现的序列化 Hook，通过 BeanSerializerModifier 追加翻译字段。
 *
 * @author juwencheng
 */
public class JacksonTranslateSerializationHook implements TranslateSerializationHook {

    public static final String HOOK_ID = "jackson";

    @Override
    public String getId() {
        return HOOK_ID;
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    @Override
    public void register(SerializationHookContext context) {
        ObjectMapper objectMapper = context.getRegistrationTarget(ObjectMapper.class);
        if (objectMapper != null) {
            configureObjectMapper(objectMapper);
        }
    }

    public void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializerFactory(
                objectMapper.getSerializerFactory()
                        .withSerializerModifier(new TranslateBeanSerializerModifier())
        );
    }
}
