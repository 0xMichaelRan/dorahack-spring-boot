package com.magiplatform.dorahack.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 藏品表
 * </p>
 *
 * @author Michael Ran
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Artwork implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    private String id;

    private String ethAddress;

    private String name;

    private String listingRound;

    private String userId;

    @ApiModelProperty(hidden = true)
    private String status;

    private String imageUrl;

    private String thumbnailUrl;

    private String properties;

    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;

    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;

}
