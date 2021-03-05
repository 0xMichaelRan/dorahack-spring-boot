package com.magiplatform.dorahack.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TransHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String artId;

    /**
     * 第几轮拍卖
     */
    private String auctionRound;

    /**
     * 卖出者
     */
    private String sellerId;

    /**
     * 出价人：0代表无人出价
     */
    private String highestBidUserId;

    /**
     * 最高出价：0代表无人出价
     */
    private BigDecimal highestBidPrice;

    /**
     * 支付截止时间
     */
    private LocalDateTime paymentEndTime;

    /**
     * 支付状态：pending，failed，success
     */
    private String paymentStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
