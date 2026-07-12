package io.github.juwencheng.fieldtranslate.testapp.dto;

import io.github.juwencheng.fieldtranslate.dict.DictFieldTranslator;
import io.github.juwencheng.fieldtranslate.core.translate.TranslateField;

/**
 * 演示 DTO：字典翻译。
 *
 * @author juwencheng
 */
public class OrderWithDict {

    private String orderId = "ORDER-100";

    @TranslateField(value = "statusText", translator = DictFieldTranslator.class, args = "order_status")
    private Integer status = 1;

    @TranslateField(value = "paymentTypeText", translator = DictFieldTranslator.class, args = "payment_type")
    private String paymentType = "ALIPAY";

    public String getOrderId() {
        return orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getPaymentType() {
        return paymentType;
    }
}
