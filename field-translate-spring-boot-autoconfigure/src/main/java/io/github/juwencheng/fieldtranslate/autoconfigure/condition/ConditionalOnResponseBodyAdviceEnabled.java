package io.github.juwencheng.fieldtranslate.autoconfigure.condition;

import io.github.juwencheng.fieldtranslate.core.output.TranslateSerializationHook;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在 output.mode=response-body-advice 或缺少序列化 Hook 时启用 ResponseBodyAdvice 降级路径。
 *
 * @author juwencheng
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnResponseBodyAdviceCondition.class)
public @interface ConditionalOnResponseBodyAdviceEnabled {
}

class OnResponseBodyAdviceCondition extends AnyNestedCondition {

    OnResponseBodyAdviceCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "field.translate.output", name = "mode", havingValue = "response-body-advice")
    static class OnAdviceMode {
    }

    @ConditionalOnMissingBean(TranslateSerializationHook.class)
    static class OnMissingSerializationHook {
    }
}
