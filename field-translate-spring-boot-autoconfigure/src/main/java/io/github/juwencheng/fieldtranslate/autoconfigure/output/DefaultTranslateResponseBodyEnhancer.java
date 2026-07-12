package io.github.juwencheng.fieldtranslate.autoconfigure.output;

import io.github.juwencheng.fieldtranslate.core.output.AppendedFieldsMerger;
import io.github.juwencheng.fieldtranslate.core.output.ResponseEnhancerContext;
import io.github.juwencheng.fieldtranslate.core.output.TranslateResponseBodyEnhancer;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContext;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContextHolder;

/**
 * 默认响应体增强器，通过 {@link AppendedFieldsMerger} 将追加字段合并到 JSON 响应。
 *
 * @author juwencheng
 */
public class DefaultTranslateResponseBodyEnhancer implements TranslateResponseBodyEnhancer {

    private final AppendedFieldsMerger appendedFieldsMerger;

    public DefaultTranslateResponseBodyEnhancer(AppendedFieldsMerger appendedFieldsMerger) {
        this.appendedFieldsMerger = appendedFieldsMerger;
    }

    @Override
    public boolean supports(ResponseEnhancerContext context) {
        String contentType = context.getContentType();
        if (contentType != null && !contentType.toLowerCase().contains("json")) {
            return false;
        }
        Class<?> bodyType = context.getBodyType();
        return bodyType != null && bodyType != Void.TYPE && bodyType != Void.class;
    }

    @Override
    public Object enhance(Object body, TranslateContext context) {
        if (body == null || context == null) {
            return body;
        }
        return appendedFieldsMerger.merge(body, context);
    }

    /**
     * 当前请求是否存在可消费的翻译上下文。
     */
    public boolean hasActiveContext() {
        return TranslateContextHolder.getContext() != null;
    }
}
