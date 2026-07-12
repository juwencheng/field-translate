package io.github.juwencheng.fieldtranslate.core.translate;

import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheStrategy;

import java.lang.reflect.Field;

/**
 * 翻译字段的元数据，缓存注解解析结果以避免重复反射。
 *
 * @author juwencheng
 */
public class TranslateFieldMeta {

    private final Field field;
    private final String outputFieldName;
    private final Class<? extends FieldTranslator> translatorClass;
    private final String[] args;
    private final Class<? extends TranslateCacheStrategy> cacheStrategyClass;

    public TranslateFieldMeta(Field field, String outputFieldName,
                              Class<? extends FieldTranslator> translatorClass, String[] args,
                              Class<? extends TranslateCacheStrategy> cacheStrategyClass) {
        this.field = field;
        this.outputFieldName = outputFieldName;
        this.translatorClass = translatorClass;
        this.args = args;
        this.cacheStrategyClass = cacheStrategyClass;
    }

    public Field getField() {
        return field;
    }

    public String getOutputFieldName() {
        return outputFieldName;
    }

    public Class<? extends FieldTranslator> getTranslatorClass() {
        return translatorClass;
    }

    public String[] getArgs() {
        return args;
    }

    public Class<? extends TranslateCacheStrategy> getCacheStrategyClass() {
        return cacheStrategyClass;
    }
}
