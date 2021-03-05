package com.magiplatform.dorahack.constants;

import com.magiplatform.dorahack.dto.base.BaseEnum;


public class PaymentConstants {

    public enum StatusEnum implements BaseEnum {

        PENDING("pending", "pending"),
        FAILED("failed", "failed"),
        SUCCESS("success", "success");

        private String code;

        private String desc;

        StatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public String getDesc() {
            return desc;
        }
    }

}
