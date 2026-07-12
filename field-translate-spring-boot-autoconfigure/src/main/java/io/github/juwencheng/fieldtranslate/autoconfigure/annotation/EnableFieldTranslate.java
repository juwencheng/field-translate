package io.github.juwencheng.fieldtranslate.autoconfigure.annotation;

import io.github.juwencheng.fieldtranslate.autoconfigure.FieldTranslateAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 显式启用字段翻译框架。
 *
 * @author juwencheng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FieldTranslateAutoConfiguration.class)
public @interface EnableFieldTranslate {
}
