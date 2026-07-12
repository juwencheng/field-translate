package io.github.juwencheng.fieldtranslate.autoconfigure.output;

import io.github.juwencheng.fieldtranslate.core.output.AppendedFieldsMerger;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContext;
import io.github.juwencheng.fieldtranslate.exception.TranslateProcessingException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认追加字段合并器。将 Bean 反射转为 Map 树，并合并 {@link TranslateContext} 中的追加字段。
 *
 * @author juwencheng
 */
public class DefaultAppendedFieldsMerger implements AppendedFieldsMerger {

    @Override
    public Object merge(Object root, TranslateContext context) {
        if (root == null || context == null) {
            return root;
        }
        return mergeValue(root, context, new IdentityHashMap<>());
    }

    private Object mergeValue(Object value, TranslateContext context, Map<Object, Boolean> visited) {
        if (value == null) {
            return null;
        }
        if (visited.containsKey(value)) {
            return null;
        }

        if (value instanceof Collection<?> collection) {
            visited.put(value, Boolean.TRUE);
            List<Object> merged = new ArrayList<>(collection.size());
            for (Object item : collection) {
                merged.add(mergeValue(item, context, visited));
            }
            return merged;
        }

        if (value instanceof Map<?, ?> mapValue) {
            visited.put(value, Boolean.TRUE);
            Map<String, Object> merged = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                merged.put(String.valueOf(entry.getKey()), mergeValue(entry.getValue(), context, visited));
            }
            appendFields(value, merged, context);
            return merged;
        }

        if (isSimpleValue(value)) {
            return value;
        }

        visited.put(value, Boolean.TRUE);
        Map<String, Object> merged = beanToMap(value);
        appendFields(value, merged, context);
        for (Map.Entry<String, Object> entry : new ArrayList<>(merged.entrySet())) {
            merged.put(entry.getKey(), mergeValue(entry.getValue(), context, visited));
        }
        return merged;
    }

    private void appendFields(Object target, Map<String, Object> merged, TranslateContext context) {
        Map<String, Object> appended = context.getAppendedDataFor(target);
        if (appended != null && !appended.isEmpty()) {
            merged.putAll(appended);
        }
    }

    private Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = new LinkedHashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                Object propertyValue = readMethod.invoke(bean);
                map.put(descriptor.getName(), propertyValue);
            }
        } catch (Exception ex) {
            throw new TranslateProcessingException("反射转换 Bean 为 Map 失败: " + bean.getClass().getName(), ex);
        }
        return map;
    }

    private boolean isSimpleValue(Object value) {
        Class<?> clazz = value.getClass();
        return clazz.isPrimitive()
                || clazz.isEnum()
                || Number.class.isAssignableFrom(clazz)
                || CharSequence.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || clazz.getName().startsWith("java.time.");
    }
}
