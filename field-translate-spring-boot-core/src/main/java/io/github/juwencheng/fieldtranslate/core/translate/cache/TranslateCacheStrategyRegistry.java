package io.github.juwencheng.fieldtranslate.core.translate.cache;

import io.github.juwencheng.fieldtranslate.core.translate.FieldTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存策略注册表。管理所有 {@link TranslateCacheStrategy} 实例，
 * 并通过 {@link TranslatorCacheBinding} 为每种 {@link FieldTranslator} 注册默认策略。
 *
 * @author juwencheng
 */
public class TranslateCacheStrategyRegistry {

    private static final Logger log = LoggerFactory.getLogger(TranslateCacheStrategyRegistry.class);

    private final Map<Class<? extends TranslateCacheStrategy>, TranslateCacheStrategy> strategyByClass = new HashMap<>();
    private final Map<Class<? extends FieldTranslator>, TranslateCacheStrategy> defaultByTranslator = new HashMap<>();

    public TranslateCacheStrategyRegistry(List<TranslateCacheStrategy> strategies,
                                          List<TranslatorCacheBinding> bindings) {
        for (TranslateCacheStrategy strategy : strategies) {
            strategyByClass.put(strategy.getClass(), strategy);
        }
        strategyByClass.putIfAbsent(NoCacheStrategy.class, new NoCacheStrategy());
        strategyByClass.putIfAbsent(DefaultTranslateCacheStrategy.class, new DefaultTranslateCacheStrategy());

        if (bindings != null) {
            for (TranslatorCacheBinding binding : bindings) {
                TranslateCacheStrategy strategy = strategyByClass.get(binding.getStrategyClass());
                if (strategy != null) {
                    defaultByTranslator.put(binding.getTranslatorClass(), strategy);
                } else {
                    log.warn("未找到缓存策略实例: {}，翻译器 {} 将不使用默认缓存",
                            binding.getStrategyClass().getName(),
                            binding.getTranslatorClass().getName());
                }
            }
        }
    }

    /**
     * 解析最终使用的缓存策略
     *
     * @param declaredStrategy 注解上声明的策略类
     * @param translatorClass  翻译器类
     */
    public TranslateCacheStrategy resolve(Class<? extends TranslateCacheStrategy> declaredStrategy,
                                          Class<? extends FieldTranslator> translatorClass) {
        if (declaredStrategy == null || declaredStrategy == DefaultTranslateCacheStrategy.class) {
            TranslateCacheStrategy defaultStrategy = defaultByTranslator.get(translatorClass);
            if (defaultStrategy != null) {
                return defaultStrategy;
            }
            return strategyByClass.getOrDefault(NoCacheStrategy.class, new NoCacheStrategy());
        }
        TranslateCacheStrategy strategy = strategyByClass.get(declaredStrategy);
        if (strategy == null) {
            log.warn("未找到缓存策略实例: {}，该字段将不使用缓存", declaredStrategy.getName());
            return strategyByClass.get(NoCacheStrategy.class);
        }
        return strategy;
    }
}
