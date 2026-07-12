package io.github.juwencheng.fieldtranslate.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * 字段翻译框架配置属性。
 *
 * @author juwencheng
 */
@ConfigurationProperties(prefix = "field.translate")
public class FieldTranslateProperties {

    /**
     * 是否启用字段翻译框架
     */
    private boolean enabled = true;

    /**
     * 切面的排序值（越大优先级越小），默认是 LOWEST_PRECEDENCE
     */
    private Integer aspectOrder = Ordered.LOWEST_PRECEDENCE;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private final TranslateCache translateCache = new TranslateCache();

    private final Output output = new Output();

    public Integer getAspectOrder() {
        return aspectOrder;
    }

    public void setAspectOrder(Integer aspectOrder) {
        this.aspectOrder = aspectOrder;
    }

    public TranslateCache getTranslateCache() {
        return translateCache;
    }

    public Output getOutput() {
        return output;
    }

    /**
     * 翻译结果缓存配置
     */
    public static class TranslateCache {

        /**
         * 是否启用翻译结果缓存
         */
        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * 输出层配置
     */
    public static class Output {

        /**
         * 输出模式：jackson-hook（默认）或 response-body-advice
         */
        private String mode = "jackson-hook";

        /**
         * 多序列化 Hook 时的选择顺序，逗号分隔，如 "jackson,gson"
         */
        private String preferred = "jackson";

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getPreferred() {
            return preferred;
        }

        public void setPreferred(String preferred) {
            this.preferred = preferred;
        }
    }
}
