package com.magiplatform.dorahack.utils;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 自定义ID生成器
 */
@Slf4j
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Value("${node.index:1}")
    private int nodeIndex;

    @Override
    public Long nextId(Object entity) {

        Random random = new Random();
        Sequence sequence = new Sequence(random.nextInt(31), nodeIndex);
        return sequence.nextId();
    }

}
