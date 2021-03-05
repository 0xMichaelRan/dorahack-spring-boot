package com.magiplatform.dorahack.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 竞拍出价表
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String artId;

    /**
     * 第几轮拍卖
     */
    private String auctionRound;

    /**
     * 拍卖开始时间
     */
    private LocalDateTime startTime;

    /**
     * 拍卖结束时间
     */
    private LocalDateTime endTime;

    /**
     * 竞拍已结束(finished)，竞拍进行中(happening)
     */
    private String status;

    private BigDecimal startBidPrice;

    private BigDecimal bidCapPrice;

    private String bidUesrId;

    /**
     * 出价价格
     */
    private BigDecimal bidPrice;

    /**
     * 出价时间
     */
    private LocalDateTime bidTime;

    /**
     * 被超过(false)，当前最高价(true)
     */
    private String isHighestBid;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
