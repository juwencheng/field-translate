package io.github.juwencheng.fieldtranslate.dict;

import io.github.juwencheng.fieldtranslate.core.translate.FieldTranslator;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字典翻译器。将字典表中的 key 值翻译成对应的 value（如 status: 1 → statusText: "已完成"）。
 *
 * @author juwencheng
 */
public class DictFieldTranslator implements FieldTranslator {

    private static final Logger log = LoggerFactory.getLogger(DictFieldTranslator.class);

    private final IDictDataProvider dictDataProvider;

    public DictFieldTranslator(IDictDataProvider dictDataProvider) {
        this.dictDataProvider = dictDataProvider;
    }

    @Override
    public Object translate(Object fieldValue, TranslateContext context) {
        String[] args = context.getAttribute("_fieldArgs");
        if (args == null || args.length == 0) {
            log.warn("DictFieldTranslator 需要 args[0] 指定字典类型名");
            return null;
        }

        String dictType = args[0];
        if (fieldValue == null) {
            return null;
        }

        String key = String.valueOf(fieldValue);
        return dictDataProvider.getDictValue(dictType, key);
    }
}
