package com.magiplatform.dorahack.constants;

import com.magiplatform.dorahack.dto.base.BaseEnum;

public class AuctionConstants {

    public static final int AUCTION_PERIOD_HOURS = 8;
    public static final int AUCTION_DEFAULT_INITIAL_PRICE = 100;
    public static final double AUCTION_PRICE_CAP_RATIO = 1.2;

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
