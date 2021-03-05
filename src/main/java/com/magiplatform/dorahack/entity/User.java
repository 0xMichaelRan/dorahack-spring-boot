package com.magiplatform.dorahack.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String fullName;

    /**
     * 以太坊地址
     */
    private String matamaskAddress;

    /**
     * 头像链接
     */
    private String profileUrl;

    /**
     * 个人描述
     */
    private String bio;

    /**
     * 邮箱
     */
    private String emailAddress;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

}
