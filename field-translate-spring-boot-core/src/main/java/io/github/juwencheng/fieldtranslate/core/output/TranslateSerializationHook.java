package io.github.juwencheng.fieldtranslate.core.output;

/**
 * 框架原生序列化扩展钩子。各序列化框架（Jackson、Gson 等）通过实现此接口
 * 在序列化阶段将 {@link io.github.juwencheng.fieldtranslate.core.translate.TranslateContext}
 * 中的追加字段写入输出。
 *
 * @author juwencheng
 */
public interface TranslateSerializationHook {

    /**
     * Hook 标识，如 "jackson"、"gson"
     */
    String getId();

    /**
     * 当前运行时是否可用（如 classpath 中是否存在对应序列化框架）
     */
    boolean isAvailable();

    /**
     * 向序列化框架注册追加字段逻辑
     *
     * @param context 注册上下文，包含目标框架的配置对象
     */
    void register(SerializationHookContext context);
}
