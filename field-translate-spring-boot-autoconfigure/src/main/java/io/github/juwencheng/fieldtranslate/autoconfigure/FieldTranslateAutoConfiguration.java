package io.github.juwencheng.fieldtranslate.autoconfigure;

import io.github.juwencheng.fieldtranslate.autoconfigure.condition.ConditionalOnResponseBodyAdviceEnabled;
import io.github.juwencheng.fieldtranslate.autoconfigure.output.DefaultAppendedFieldsMerger;
import io.github.juwencheng.fieldtranslate.autoconfigure.output.DefaultTranslateResponseBodyEnhancer;
import io.github.juwencheng.fieldtranslate.autoconfigure.output.TranslateResponseBodyAdvice;
import io.github.juwencheng.fieldtranslate.core.output.AppendedFieldsMerger;
import io.github.juwencheng.fieldtranslate.core.output.TranslateResponseBodyEnhancer;
import io.github.juwencheng.fieldtranslate.core.properties.FieldTranslateProperties;
import io.github.juwencheng.fieldtranslate.core.translate.FieldTranslator;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateAspect;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContextContributor;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateStrategy;
import io.github.juwencheng.fieldtranslate.core.translate.cache.InMemoryTranslateCacheStore;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheManager;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheStore;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheStrategy;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslateCacheStrategyRegistry;
import io.github.juwencheng.fieldtranslate.core.translate.cache.TranslatorCacheBinding;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 字段翻译框架核心自动配置。仅通过 {@link io.github.juwencheng.fieldtranslate.autoconfigure.annotation.EnableFieldTranslate} 导入。
 *
 * @author juwencheng
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(RestController.class)
@ConditionalOnProperty(prefix = "field.translate", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(FieldTranslateProperties.class)
public class FieldTranslateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InMemoryTranslateCacheStore inMemoryTranslateCacheStore() {
        return new InMemoryTranslateCacheStore();
    }

    @Bean
    public TranslateCacheStrategyRegistry translateCacheStrategyRegistry(
            List<TranslateCacheStrategy> strategies,
            List<TranslatorCacheBinding> bindings) {
        return new TranslateCacheStrategyRegistry(strategies, bindings);
    }

    @Bean
    public TranslateCacheManager translateCacheManager(
            TranslateCacheStrategyRegistry strategyRegistry,
            List<TranslateCacheStore> stores,
            FieldTranslateProperties properties) {
        return new TranslateCacheManager(strategyRegistry, stores, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TranslateStrategy translateStrategy(List<FieldTranslator> translators,
                                               TranslateCacheManager cacheManager) {
        return new TranslateStrategy(translators, cacheManager);
    }

    @Bean
    public TranslateAspect translateAspect(TranslateStrategy translateStrategy,
                                           FieldTranslateProperties properties) {
        return new TranslateAspect(translateStrategy, properties.getAspectOrder() - 1);
    }

    @Bean
    public TranslateInterceptor translateInterceptor(List<TranslateContextContributor> contributors) {
        return new TranslateInterceptor(contributors);
    }

    @Bean
    public WebMvcConfigurer fieldTranslateWebMvcConfigurer(TranslateInterceptor translateInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(translateInterceptor);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public AppendedFieldsMerger appendedFieldsMerger() {
        return new DefaultAppendedFieldsMerger();
    }

    @Configuration
    @ConditionalOnResponseBodyAdviceEnabled
    static class ResponseBodyAdviceConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public TranslateResponseBodyEnhancer translateResponseBodyEnhancer(AppendedFieldsMerger appendedFieldsMerger) {
            return new DefaultTranslateResponseBodyEnhancer(appendedFieldsMerger);
        }

        @Bean
        public TranslateResponseBodyAdvice translateResponseBodyAdvice(
                TranslateResponseBodyEnhancer responseBodyEnhancer) {
            return new TranslateResponseBodyAdvice(responseBodyEnhancer);
        }
    }
}
