package com.magiplatform.dorahack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magiplatform.dorahack.configuration.SwaggerApiVersion;
import com.magiplatform.dorahack.configuration.SwaggerApiVersionConstant;
import com.magiplatform.dorahack.constants.ArtworkConstants;
import com.magiplatform.dorahack.dto.base.ResultDto;
import com.magiplatform.dorahack.entity.Artwork;
import com.magiplatform.dorahack.entity.TransHistory;
import com.magiplatform.dorahack.service.IArtworkService;
import com.magiplatform.dorahack.service.IAuctionService;
import com.magiplatform.dorahack.service.ITransHistoryService;
import com.magiplatform.dorahack.service.IUserService;
import com.magiplatform.dorahack.utils.CustomIdGenerator;
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
 * 藏品表 前端控制器
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Slf4j
@Api(tags = "artwork")
@ApiOperation(value = "（查询、上传创建、上架拍卖；藏品不允许修改，不允许删除）")
@RestController
@RequestMapping("/artwork")
public class ArtworkController {

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

    @ApiOperation(value = "查询藏品信息")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id")
    public ResultDto<Artwork> getArt(HttpServletRequest request, @RequestParam String id) {
        Artwork artwork = artworkService.getById(id);
        return ResultDto.success(artwork);
    }

    @ApiOperation(value = "所有的“最新”艺术品（12件）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/all-new-arts")
    public ResultDto<List<Artwork>> getAllNewArts(HttpServletRequest request) {
        QueryWrapper<Artwork> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Artwork::getCreateTime).last("limit 0," + ArtworkConstants.FRONT_PAGE_ARTWORK_COUNT);
        List<Artwork> list = artworkService.list(queryWrapper);
        return ResultDto.success(list);
    }

    @ApiOperation(value = "所有非“最新”艺术品（除了最新的12件以外）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/all-previous-arts")
    public ResultDto<List<Artwork>> getAllPreviousArts(HttpServletRequest request) {
        QueryWrapper<Artwork> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Artwork::getCreateTime).last("limit " + ArtworkConstants.FRONT_PAGE_ARTWORK_COUNT + ",999");
        List<Artwork> list = artworkService.list(queryWrapper);
        return ResultDto.success(list);
    }

    @ApiOperation(value = "创建1件艺术品（*实际上每个创作者需要创建12个作品才能参与投票及上架）")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @PostMapping("/create")
    public ResultDto create(HttpServletRequest request, @ModelAttribute Artwork artwork) {
        artwork.setId(String.valueOf(customIdGenerator.nextId(artwork)));
        artwork.setStatus(ArtworkConstants.StatusEnum.FINISHED.getCode());
        artwork.setCreateTime(LocalDateTime.now());
        boolean save = artworkService.save(artwork);
        return save ? ResultDto.success("创建成功") : ResultDto.failure("-1", "创建失败");
    }

    @ApiOperation(value = "某件艺术品的拍卖和成交记录")
    @SwaggerApiVersion(group = SwaggerApiVersionConstant.WEB_1_0)
    @GetMapping("/id/auction_history")
    public ResultDto<List<TransHistory>> getIdAuctionHistory(HttpServletRequest request, @RequestParam String id) {
        // TODO: some issue with this
        QueryWrapper<TransHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(TransHistory::getArtId, id);
        List<TransHistory> list = transHistoryService.list(queryWrapper);
        return ResultDto.success(list);

    }

}
