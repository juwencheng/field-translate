package io.github.juwencheng.fieldtranslate.dict.cache;

import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheKeyContext;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheStrategy;
import io.github.juwencheng.fieldtranslate.dict.properties.FieldTranslateDictProperties;

import java.time.Duration;

/**
 * 字典翻译默认缓存策略。
 * <p>
 * Key 格式：{@code dict:{dictType}:{key}}
 *
 * @author juwencheng
 */
public class DictTranslateCacheStrategy implements TranslateCacheStrategy {

    private final FieldTranslateDictProperties properties;

    public DictTranslateCacheStrategy(FieldTranslateDictProperties properties) {
        this.properties = properties;
    }

    @Override
    public Duration ttl() {
        return properties.getCacheTtl();
    }

    @Override
    public String buildKey(TranslateCacheKeyContext keyContext) {
        String[] args = keyContext.getArgs();
        String dictType = (args != null && args.length > 0) ? args[0] : "unknown";
        Object fieldValue = keyContext.getFieldValue();
        String key = fieldValue == null ? "null" : String.valueOf(fieldValue);
        return "dict:" + dictType + ":" + key;
    }
}
