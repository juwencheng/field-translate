package io.github.juwencheng.fieldtranslate.autoconfigure.output;

import io.github.juwencheng.fieldtranslate.core.output.ResponseEnhancerContext;
import io.github.juwencheng.fieldtranslate.core.output.TranslateResponseBodyEnhancer;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContextHolder;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 在 response-body-advice 模式下将翻译追加字段合并到响应体。
 *
 * @author juwencheng
 */
@ControllerAdvice
public class TranslateResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final TranslateResponseBodyEnhancer responseBodyEnhancer;

    public TranslateResponseBodyAdvice(TranslateResponseBodyEnhancer responseBodyEnhancer) {
        this.responseBodyEnhancer = responseBodyEnhancer;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (!returnType.hasMethodAnnotation(TranslateResponse.class)) {
            return false;
        }
        if (TranslateContextHolder.getContext() == null) {
            return false;
        }
        ResponseEnhancerContext context = new ResponseEnhancerContext(
                returnType.getParameterType(),
                resolveContentType(converterType),
                null);
        return responseBodyEnhancer.supports(context);
    }

    private String resolveContentType(Class<? extends HttpMessageConverter<?>> converterType) {
        if (converterType.getName().toLowerCase().contains("json")) {
            return "application/json";
        }
        return "application/octet-stream";
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        return responseBodyEnhancer.enhance(body, TranslateContextHolder.getContext());
    }
}
