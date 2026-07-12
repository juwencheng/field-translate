package io.github.juwencheng.fieldtranslate.dict.autoconfigure;

import io.github.juwencheng.fieldtranslate.core.translate.TranslateStrategy;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslatorCacheBinding;
import io.github.juwencheng.fieldtranslate.dict.DefaultDictDataProvider;
import io.github.juwencheng.fieldtranslate.dict.DictFieldTranslator;
import io.github.juwencheng.fieldtranslate.dict.IDictDataProvider;
import io.github.juwencheng.fieldtranslate.dict.cache.DictTranslateCacheStrategy;
import io.github.juwencheng.fieldtranslate.dict.properties.FieldTranslateDictProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 字典翻译插件自动配置。
 *
 * @author juwencheng
 */
@AutoConfiguration
@AutoConfigureAfter(name = "io.github.juwencheng.fieldtranslate.autoconfigure.FieldTranslateAutoConfiguration")
@ConditionalOnBean(TranslateStrategy.class)
@ConditionalOnProperty(prefix = "field.translate.dict", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(FieldTranslateDictProperties.class)
public class DictAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IDictDataProvider dictDataProvider() {
        return new DefaultDictDataProvider();
    }

    @Bean
    @ConditionalOnMissingBean
    public DictFieldTranslator dictFieldTranslator(IDictDataProvider dictDataProvider) {
        return new DictFieldTranslator(dictDataProvider);
    }

    @Bean
    public DictTranslateCacheStrategy dictTranslateCacheStrategy(FieldTranslateDictProperties properties) {
        return new DictTranslateCacheStrategy(properties);
    }

    @Bean
    public TranslatorCacheBinding dictTranslatorCacheBinding() {
        return new TranslatorCacheBinding(DictFieldTranslator.class, DictTranslateCacheStrategy.class);
    }
}
