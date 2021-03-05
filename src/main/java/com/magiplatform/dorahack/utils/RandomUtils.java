/**
 * $Id: RandomUtils.java 6145 2016-04-21 05:29:39Z huxiaowei $
 * Copyright(C) 2014-2020 easegame, All Rights Reserved.
 */
package com.magiplatform.dorahack.utils;

import java.security.SecureRandom;
import java.util.stream.IntStream;

/**
 * @version V1.0
 * @Description: 随机数生成工具
 * @version 1.0 2016年03月21日 5:54 PM:00
 */
public class RandomUtils {
    /**
     * 生成随机4位数字
     *
     * @return a int.
     */
    public static int secureRandomNumber4() {
        SecureRandom sr = new SecureRandom();
        return sr.nextInt(900_0) + 100_0;
    }

    /**
     * 生成随机6位数字
     *
     * @return a int.
     */
    public static int secureRandomNumber6() {
        SecureRandom sr = new SecureRandom();
        return sr.nextInt(900_000) + 100_000;
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link String} objects.
     */
    public static void main(String [] args) {
        IntStream.range(1, 10).forEach(i -> System.out.println(secureRandomNumber4()));
        IntStream.range(1, 10).forEach(i -> System.out.println(secureRandomNumber6()));
    }
}
