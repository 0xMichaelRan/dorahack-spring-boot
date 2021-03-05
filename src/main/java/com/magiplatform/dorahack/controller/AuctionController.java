package com.magiplatform.dorahack.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magiplatform.dorahack.configuration.SwaggerApiVersion;
import com.magiplatform.dorahack.configuration.SwaggerApiVersionConstant;
import com.magiplatform.dorahack.constants.ArtworkConstants;
import com.magiplatform.dorahack.constants.AuctionConstants;
import com.magiplatform.dorahack.dto.base.ResultDto;
import com.magiplatform.dorahack.entity.Artwork;
import com.magiplatform.dorahack.entity.Auction;
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
    public ResultDto<List<Auction>> getIdRoundId(HttpServletRequest request, @RequestParam String artId, @RequestParam String auctionRound) {
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
//        UpdateWrapper<Artwork> wrapper = new UpdateWrapper<>();
//        wrapper.lambda().eq(Artwork::getId, artId);
        Artwork artwork = new Artwork();
        artwork.setId(artId);
        artwork.setStatus(ArtworkConstants.StatusEnum.ON_AUCTION.getCode());
        artwork.setUpdateTime(LocalDateTime.now());
        boolean b = artworkService.updateById(artwork);

        // 查询藏品id最大轮次的竞拍记录
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Auction::getArtId, artId).eq(Auction::getIsHighestBid, "true").orderByDesc(Auction::getAuctionRound).last("limit 0,1");
        List<Auction> list = auctionService.list(queryWrapper);

        // 初始化竞拍
        LocalDateTime now = LocalDateTime.now();

        Auction auction = new Auction();
        auction.setId(String.valueOf(customIdGenerator.nextUUID(auction)));
        auction.setArtId(artId);
        auction.setStartTime(now);
        auction.setEndTime(now.plusHours(8L));
        auction.setStatus(AuctionConstants.StatusEnum.HAPPENING.getCode());

        auction.setCreateTime(now);
        if (CollectionUtils.isEmpty(list)) {
            auction.setAuctionRound("1");
            auction.setStartBidPrice(new BigDecimal("100"));
            auction.setBidCapPrice(new BigDecimal("120"));
        } else {
            Auction maxAuction = list.get(0);
            auction.setAuctionRound(maxAuction.getAuctionRound() + 1);
            auction.setStartBidPrice(maxAuction.getBidPrice());
            auction.setBidCapPrice(maxAuction.getBidPrice().multiply(new BigDecimal("1.2")));
        }
        auction.setBidUesrId("0");
        auction.setBidPrice(new BigDecimal("0"));
//            auction.setBidTime(now);
        auction.setIsHighestBid("true");

        auctionService.save(auction);

        return b ? ResultDto.success("上架成功") : ResultDto.failure("-1", "上架失败");
    }

    @ApiOperation(value = "给拍卖品出价")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/round/id/bid")
    public ResultDto<List<Auction>> getIdRoundId(HttpServletRequest request, @RequestParam String artId, @RequestParam String auctionRound, @RequestParam String price, @RequestParam String bidUserId) {

        // 查询藏品id最大轮次的竞拍记录
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Auction::getArtId, artId).eq(Auction::getAuctionRound, auctionRound).eq(Auction::getIsHighestBid, "true");
        Auction one = auctionService.getOne(queryWrapper);

        if (AuctionConstants.StatusEnum.FINISHED.getCode().equals(one.getStatus()) || one.getBidPrice().compareTo(new BigDecimal(price)) >= 0) {
            return ResultDto.failure("-1", "出价无效");
        }

        // 新增竞拍出价
        Auction two = new Auction();
        BeanUtils.copyProperties(one, two);
        two.setId(String.valueOf(customIdGenerator.nextUUID(two)));
        two.setBidUesrId(bidUserId);
        two.setBidPrice(new BigDecimal(price));
        two.setBidTime(LocalDateTime.now());
        auctionService.save(two);

        // 更新前一次竞拍出价false
        one.setIsHighestBid("false");
        auctionService.updateById(one);

        return ResultDto.success("出价成功");
    }

    @ApiOperation(value = "给拍卖品支付（需要调用bsc接口）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/pay")
    public ResultDto<List<Auction>> idPay(HttpServletRequest request) {





        // todo-lichen
        return ResultDto.success("支付成功");
    }

}
