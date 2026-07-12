package io.github.juwencheng.fieldtranslate.core.output;

import io.github.juwencheng.fieldtranslate.core.translate.TranslateContext;

/**
 * 将 {@link TranslateContext} 中的追加字段合并到响应对象（反射转为 Map 树）。
 *
 * @author juwencheng
 */
public interface AppendedFieldsMerger {

    /**
     * 合并追加字段到根对象
     *
     * @param root    控制器返回对象
     * @param context 翻译上下文
     * @return 合并后的对象（通常为 Map 或 List）
     */
    Object merge(Object root, TranslateContext context);
}
