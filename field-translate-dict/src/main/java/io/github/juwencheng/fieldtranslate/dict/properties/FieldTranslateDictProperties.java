package io.github.juwencheng.fieldtranslate.dict.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * 字典翻译插件配置。
 *
 * @author juwencheng
 */
@ConfigurationProperties(prefix = "field.translate.dict")
public class FieldTranslateDictProperties {

    /**
     * 是否启用字典翻译插件
     */
    private boolean enabled = true;

    /**
     * 字典翻译缓存 TTL
     */
    private Duration cacheTtl = Duration.ofHours(1);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Duration getCacheTtl() {
        return cacheTtl;
    }

    public void setCacheTtl(Duration cacheTtl) {
        this.cacheTtl = cacheTtl;
    }
}
