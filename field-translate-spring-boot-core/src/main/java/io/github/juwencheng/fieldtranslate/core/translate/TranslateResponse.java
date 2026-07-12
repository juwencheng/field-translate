package io.github.juwencheng.fieldtranslate.core.translate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用翻译响应注解。标注在 Controller 方法上，触发通用翻译框架处理。
 * <p>
 * 标注此注解的方法返回值将被遍历，对其中 {@link TranslateField} 标注的字段执行翻译，
 * 翻译结果追加到 JSON 输出中。
 *
 * @author juwencheng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TranslateResponse {
}
