package com.magiplatform.dorahack.constants;

import com.magiplatform.dorahack.dto.base.BaseEnum;

public class ArtworkConstants {

    public static final int FRONT_PAGE_ARTWORK_COUNT = 12;

    public enum StatusEnum implements BaseEnum {

        ON_AUCTION("on_auction", "on_auction"),
        PENDING_PAYMENT("pending_payment", "pending_payment"),
        FINISHED("finished", "finished");

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
