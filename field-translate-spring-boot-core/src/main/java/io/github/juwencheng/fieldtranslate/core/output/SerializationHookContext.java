package io.github.juwencheng.fieldtranslate.core.output;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化 Hook 注册时的上下文，承载目标序列化框架的配置对象及扩展属性。
 *
 * @author juwencheng
 */
public class SerializationHookContext {

    private final Object registrationTarget;
    private final Map<String, Object> attributes = new HashMap<>();

    public SerializationHookContext(Object registrationTarget) {
        this.registrationTarget = registrationTarget;
    }

    /**
     * 获取序列化框架的配置对象（如 Jackson ObjectMapper、Gson Builder 等）
     */
    public Object getRegistrationTarget() {
        return registrationTarget;
    }

    /**
     * 按类型获取序列化框架的配置对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getRegistrationTarget(Class<T> type) {
        if (registrationTarget != null && type.isInstance(registrationTarget)) {
            return (T) registrationTarget;
        }
        return null;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
