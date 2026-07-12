package io.github.juwencheng.fieldtranslate.core.translate.cache;

import io.github.juwencheng.fieldtranslate.core.translate.FieldTranslator;

/**
 * 翻译器与缓存策略的绑定关系。
 * 各插件通过注册此 Bean 将默认缓存策略关联到对应的 {@link FieldTranslator}。
 *
 * @author juwencheng
 */
public class TranslatorCacheBinding {

    private final Class<? extends FieldTranslator> translatorClass;
    private final Class<? extends TranslateCacheStrategy> strategyClass;

    public TranslatorCacheBinding(Class<? extends FieldTranslator> translatorClass,
                                  Class<? extends TranslateCacheStrategy> strategyClass) {
        this.translatorClass = translatorClass;
        this.strategyClass = strategyClass;
    }

    public Class<? extends FieldTranslator> getTranslatorClass() {
        return translatorClass;
    }

    public Class<? extends TranslateCacheStrategy> getStrategyClass() {
        return strategyClass;
    }
}
