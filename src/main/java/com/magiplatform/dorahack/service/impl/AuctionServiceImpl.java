package com.magiplatform.dorahack.service.impl;

import com.magiplatform.dorahack.entity.Auction;
import com.magiplatform.dorahack.mapper.AuctionMapper;
import com.magiplatform.dorahack.service.IAuctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 竞拍出价表 服务实现类
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Service
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper, Auction> implements IAuctionService {

}
