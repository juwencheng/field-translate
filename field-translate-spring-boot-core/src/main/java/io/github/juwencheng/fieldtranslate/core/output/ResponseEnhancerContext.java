package io.github.juwencheng.fieldtranslate.core.output;

/**
 * ResponseBodyAdvice 增强时的上下文信息。
 *
 * @author juwencheng
 */
public class ResponseEnhancerContext {

    private final Class<?> bodyType;
    private final String contentType;
    private final String outputMode;

    public ResponseEnhancerContext(Class<?> bodyType, String contentType, String outputMode) {
        this.bodyType = bodyType;
        this.contentType = contentType;
        this.outputMode = outputMode;
    }

    public Class<?> getBodyType() {
        return bodyType;
    }

    public String getContentType() {
        return contentType;
    }

    public String getOutputMode() {
        return outputMode;
    }
}
