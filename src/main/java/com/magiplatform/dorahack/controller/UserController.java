package com.magiplatform.dorahack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magiplatform.dorahack.configuration.SwaggerApiVersion;
import com.magiplatform.dorahack.configuration.SwaggerApiVersionConstant;
import com.magiplatform.dorahack.constants.PaymentConstants;
import com.magiplatform.dorahack.dto.base.ResultDto;
import com.magiplatform.dorahack.entity.Auction;
import com.magiplatform.dorahack.entity.TransHistory;
import com.magiplatform.dorahack.entity.User;
import com.magiplatform.dorahack.service.IAuctionService;
import com.magiplatform.dorahack.service.ITransHistoryService;
import com.magiplatform.dorahack.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Slf4j
@Api(tags = "user")
@ApiOperation(value = "个人信息（修改、查询）")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuctionService auctionService;

    @Autowired
    private ITransHistoryService transHistoryService;

    @ApiOperation(value = "添加user id信息")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/add")
    public ResultDto<User> addUser(HttpServletRequest request, @RequestParam String id) {

        User user = new User();
        user.setId(id);
        user.setCreateTime(LocalDateTime.now());
        boolean b = userService.save(user);
        return b ? ResultDto.success("添加成功") : ResultDto.failure("-1", "添加失败");
    }


    @ApiOperation(value = "查询user信息")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id")
    public ResultDto<User> getUser(HttpServletRequest request, @RequestParam String id) {
        User user = userService.getById(id);
        return ResultDto.success(user);
    }

    @ApiOperation(value = "更新User信息")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/id/update")
    public ResultDto<User> updateUser(HttpServletRequest request, @ModelAttribute User user) {
        boolean b = userService.updateById(user);
        return b ? ResultDto.success("更新成功") : ResultDto.failure("-1", "更新失败");
    }

    @ApiOperation(value = "我的全部出价记录")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/bid-history")
    public ResultDto<List<Auction>> getBidHistory(HttpServletRequest request, @RequestParam String id) {
        QueryWrapper<Auction> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Auction::getBidUesrId, id);
        List<Auction> list = auctionService.list(queryWrapper);
        return ResultDto.success(list);

    }

    @ApiOperation(value = "我的现持有藏品 （查询bsc数据，因为区块链上有最新数据）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/arts/bid-holding")
    public ResultDto<User> getArtsBidHolding(HttpServletRequest request, @RequestParam String id) {

        return ResultDto.success("");
    }

    @ApiOperation(value = "我已拍到但没支付的藏品 （查询 交易历史表 trans_history")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/arts/pending-payment")
    public ResultDto<List<TransHistory>> getArtsPendingPayment(HttpServletRequest request, @RequestParam String id) {

        QueryWrapper<TransHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TransHistory::getHighestBidUserId, id)
                .eq(TransHistory::getPaymentStatus, PaymentConstants.StatusEnum.PENDING.getCode());
        List<TransHistory> list = transHistoryService.list(queryWrapper);
        return ResultDto.success(list);
    }

    @ApiOperation(value = "我卖出的记录 （查询 交易历史表 trans_history）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/arts/sell-history ")
    public ResultDto<List<TransHistory>> getArtsSellHistory(HttpServletRequest request, @RequestParam String id) {

        QueryWrapper<TransHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TransHistory::getSellerId, id)
                .eq(TransHistory::getPaymentStatus, PaymentConstants.StatusEnum.SUCCESS.getCode());
        List<TransHistory> list = transHistoryService.list(queryWrapper);
        return ResultDto.success(list);
    }

}
