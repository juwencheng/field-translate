package io.github.juwencheng.fieldtranslate.exception;

/**
 * 字段翻译处理过程中的运行时异常。
 *
 * @author juwencheng
 */
public class TranslateProcessingException extends RuntimeException {

    public TranslateProcessingException(String message) {
        super(message);
    }

    public TranslateProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
