package com.magiplatform.dorahack.constants;

import com.magiplatform.dorahack.dto.base.BaseEnum;

public class AuctionConstants {

    public enum StatusEnum implements BaseEnum {

        FINISHED("finished", "finished"),
        HAPPENING("happening", "happening");

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
