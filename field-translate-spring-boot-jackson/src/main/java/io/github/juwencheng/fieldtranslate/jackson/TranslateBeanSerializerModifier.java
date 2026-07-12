package io.github.juwencheng.fieldtranslate.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

/**
 * 字段翻译框架的 Jackson BeanSerializerModifier。
 * 将所有 Bean 序列化器包装为 {@link TranslateAppendingBeanSerializer}，以追加翻译字段。
 *
 * @author juwencheng
 */
public class TranslateBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (serializer instanceof BeanSerializerBase) {
            return new TranslateAppendingBeanSerializer((BeanSerializerBase) serializer);
        }
        return serializer;
    }
}
