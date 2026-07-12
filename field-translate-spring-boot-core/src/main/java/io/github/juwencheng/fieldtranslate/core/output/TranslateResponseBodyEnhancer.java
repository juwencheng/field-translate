package io.github.juwencheng.fieldtranslate.core.output;

import io.github.juwencheng.fieldtranslate.core.translate.TranslateContext;

/**
 * Spring MVC 层通用响应增强器，在不依赖 Jackson Hook 时将追加字段写入 JSON 响应。
 *
 * @author juwencheng
 */
public interface TranslateResponseBodyEnhancer {

    /**
     * 是否支持对当前响应进行增强
     */
    boolean supports(ResponseEnhancerContext context);

    /**
     * 增强响应体
     *
     * @param body    原始响应体
     * @param context 翻译上下文
     * @return 增强后的响应体
     */
    Object enhance(Object body, TranslateContext context);
}
