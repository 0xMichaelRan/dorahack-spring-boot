package com.magiplatform.dorahack.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.magiplatform.dorahack.configuration.SwaggerApiVersion;
import com.magiplatform.dorahack.configuration.SwaggerApiVersionConstant;
import com.magiplatform.dorahack.constants.ArtworkConstants;
import com.magiplatform.dorahack.constants.AuctionConstants;
import com.magiplatform.dorahack.constants.PaymentConstants;
import com.magiplatform.dorahack.dto.base.ResultDto;
import com.magiplatform.dorahack.entity.Artwork;
import com.magiplatform.dorahack.entity.Auction;
import com.magiplatform.dorahack.entity.TransHistory;
import com.magiplatform.dorahack.mapper.AuctionMapper;
import com.magiplatform.dorahack.service.IArtworkService;
import com.magiplatform.dorahack.service.IAuctionService;
import com.magiplatform.dorahack.service.ITransHistoryService;
import com.magiplatform.dorahack.service.IUserService;
import com.magiplatform.dorahack.utils.CustomIdGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 竞拍出价表 前端控制器
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */

@Slf4j
@Api(tags = "auction")
@ApiOperation(value = "竞拍有关")
@RestController
@RequestMapping("/auction")
public class AuctionController {
    @Autowired
    private IArtworkService artworkService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private ITransHistoryService transHistoryService;

    @Autowired
    private CustomIdGenerator customIdGenerator;

    @ApiOperation(value = "某一轮竞拍的历史和当前数据")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/round/id")
    public ResultDto<List<Auction>> getIdRoundId(
            HttpServletRequest request,
            @RequestParam String artId,
            @RequestParam String auctionRound
    ) {
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Auction::getArtId, artId)
                .eq(Auction::getAuctionRound, auctionRound);
        List<Auction> list = auctionService.list(queryWrapper);
        return ResultDto.success(list);
    }

    @ApiOperation(value = "开始上架拍卖")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/start-auction")
    public ResultDto idStartAuction(HttpServletRequest request, @RequestParam String artId) {

        // 更新状态
        Artwork artwork = new Artwork();
        artwork.setId(artId);
        artwork.setStatus(ArtworkConstants.StatusEnum.ON_AUCTION.getCode());
        artwork.setUpdateTime(LocalDateTime.now());
        boolean b = artworkService.updateById(artwork);

        // 查询藏品id最大轮次的竞拍记录
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Auction::getArtId, artId)
                .eq(Auction::getIsHighestBid, "true")
                .orderByDesc(Auction::getAuctionRound)
                .last("limit 0,1");
        List<Auction> list = auctionService.list(queryWrapper);

        // 初始化竞拍
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction();
        auction.setId(String.valueOf(customIdGenerator.nextUUID(auction)));
        auction.setArtId(artId);
        auction.setStartTime(now);
        auction.setEndTime(now.plusHours(AuctionConstants.AUCTION_PERIOD_HOURS));
        auction.setStatus(AuctionConstants.StatusEnum.HAPPENING.getCode());

        auction.setCreateTime(now);
        if (CollectionUtils.isEmpty(list)) {
            auction.setAuctionRound("1");
            auction.setStartBidPrice(new BigDecimal(AuctionConstants.AUCTION_DEFAULT_INITIAL_PRICE));
            auction.setBidCapPrice(new BigDecimal(AuctionConstants.AUCTION_DEFAULT_INITIAL_PRICE * AuctionConstants.AUCTION_PRICE_CAP_RATIO));
        } else {
            Auction maxAuction = list.get(0);
            auction.setAuctionRound(String.valueOf(Integer.parseInt(maxAuction.getAuctionRound()) + 1));
            auction.setStartBidPrice(maxAuction.getBidPrice());
            auction.setBidCapPrice(maxAuction.getBidPrice().multiply(new BigDecimal(AuctionConstants.AUCTION_PRICE_CAP_RATIO)));
        }
        auction.setBidUesrId("0");
        auction.setBidPrice(new BigDecimal("0"));
        auction.setIsHighestBid("true");

        auctionService.save(auction);

        return b ? ResultDto.success("上架成功") : ResultDto.failure("-1", "上架失败");
    }

    @ApiOperation(value = "给拍卖品出价")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/round/id/bid")
    public ResultDto<List<Auction>> bidPriceForArt(
            HttpServletRequest request,
            @RequestParam String artId,
            @RequestParam String auctionRound,
            @RequestParam String bidPrice,
            @RequestParam String bidUserId
    ) {
        BigDecimal bidPriceBig = new BigDecimal(bidPrice);

        // 查询藏品id最大轮次的竞拍记录
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Auction::getArtId, artId)
                .eq(Auction::getAuctionRound, auctionRound)
                .eq(Auction::getIsHighestBid, "true")
                .last("limit 1");
        Auction currentHighestBid = auctionService.getOne(queryWrapper);

        if (AuctionConstants.StatusEnum.FINISHED.getCode().equals(currentHighestBid.getStatus())) {
            return ResultDto.failure("-1", "This round is already closed.");
        }
        else if (bidPriceBig.compareTo(currentHighestBid.getBidCapPrice()) > 0) {
            // user's bid > price cap
            return ResultDto.failure("-1", "Price cap for round " + currentHighestBid.getAuctionRound()
                    + " is " + currentHighestBid.getBidCapPrice());
        }
        else if (currentHighestBid.getBidPrice().compareTo(bidPriceBig) > 0 ||
                (currentHighestBid.getBidPrice().compareTo(bidPriceBig) == 0 && bidPriceBig.compareTo(currentHighestBid.getBidCapPrice()) < 0)) {
            // current highest bid > user's bid
            // Or current highest bid = user's bid < price cap
            return ResultDto.failure("-1", "Your bid price is too low. ");
        }

        // 新增竞拍出价
        Auction newHighestBid = new Auction();
        BeanUtils.copyProperties(currentHighestBid, newHighestBid);
        newHighestBid.setId(String.valueOf(customIdGenerator.nextUUID(newHighestBid)));
        newHighestBid.setBidUesrId(bidUserId);
        newHighestBid.setBidPrice(bidPriceBig);
        newHighestBid.setBidTime(LocalDateTime.now());
        newHighestBid.setCreateTime(LocalDateTime.now());
        auctionService.save(newHighestBid);

        // 更新前一次竞拍出价false
        if (currentHighestBid.getBidPrice().compareTo(bidPriceBig) < 0) {
            // if previous highest bid < user's bid, previous bid is no longer the highest bid
            currentHighestBid.setIsHighestBid("false");
            auctionService.updateById(currentHighestBid);
        } else {
            // in this, previous highest bid = user's bid = price cap
            // there are multiple rows with isHighestBid flat = true
        }

        return ResultDto.success("出价成功");
    }

    @ApiOperation(value = "给拍卖品支付（需要调用bsc接口）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/pay")
    public ResultDto<List<Auction>> idPay(
            HttpServletRequest request,
            @RequestParam String artId,
            @RequestParam String auctionRound,
            @RequestParam String paidPrice,
            @RequestParam String bidUserId
    ) {
        // TODO: call BSC to process the payment
        // Assuming payment is successful, here.

        BigDecimal paidPriceBig = new BigDecimal(paidPrice);

        // 1. update owner_id, status of artwork_table
        Artwork artwork = artworkService.getById(artId);
        System.out.println("artId is ");
        System.out.println(artId);

        String currentOwner = artwork.getUserId();
        artwork.setUserId(bidUserId); // the bid user ID is user ID in artwork table
        artwork.setStatus(ArtworkConstants.StatusEnum.FINISHED.getCode());
        boolean save = artworkService.updateById(artwork);
        if (!save) {
            ResultDto.failure("-1", "写入artwork_table失败");
        }

        // 2。 update auction_status of auction_table (happening -> finished)
        LambdaUpdateWrapper<Auction> lambda = new UpdateWrapper().lambda();
        lambda
                .eq(Auction::getArtId, artId)
                .eq(Auction::getAuctionRound, auctionRound)
                .eq(Auction::getStatus, AuctionConstants.StatusEnum.HAPPENING.getCode())
                .set(Auction::getStatus, AuctionConstants.StatusEnum.FINISHED.getCode());
        save = auctionService.update(lambda);
        if (!save) {
            ResultDto.failure("-1", "写入auction_table失败");
        }

        // 3. add new record in trans_history
        TransHistory transHistory = new TransHistory();
        transHistory.setId(String.valueOf(customIdGenerator.nextId(paidPrice)));
        transHistory.setArtId(artId);
        transHistory.setAuctionRound(auctionRound);
        transHistory.setSellerId(currentOwner);
        transHistory.setHighestBidUserId(bidUserId);
        transHistory.setHighestBidPrice(new BigDecimal(paidPrice));
        transHistory.setPaymentEndTime(LocalDateTime.now());
        transHistory.setPaymentStatus(PaymentConstants.StatusEnum.SUCCESS.getCode());

        save = transHistoryService.save(transHistory);
        return save
                ? ResultDto.success("支付流水记录完成")
                : ResultDto.failure("-1", "写入trans_history失败");
    }

}
