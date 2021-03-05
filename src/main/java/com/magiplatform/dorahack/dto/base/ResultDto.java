package com.magiplatform.dorahack.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel()
@Data
public class ResultDto<T> implements Serializable {

    @ApiModelProperty(value = "错误码,0代表成功", position = -9998)
    private String code = "0";

    @ApiModelProperty(value = "错误描述", position = -9997)
    private String message;

    @ApiModelProperty
    private T data;

    public boolean isSuccess() {
        return this.code.equals("0");
    }

    public ResultDto() {
    }

    public ResultDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <Object> ResultDto<Object> result(String code, String message, Object data) {
        return new ResultDto<>(code, message, data);
    }

    public static <Object> ResultDto<Object> success() {

        return new ResultDto<>("0", "", null);
    }

    public static <Object> ResultDto<Object> success(String message) {
        return new ResultDto<>("0", message, null);
    }

    public static <Object> ResultDto<Object> success(Object data) {
        return new ResultDto<>("0", "", data);
    }

    public static <Object> ResultDto<Object> failure(String code, String message) {
        return new ResultDto<>(code, message);
    }

    public static <Object> ResultDto<Object> failure(String code, String message, Object data) {
        return new ResultDto<>(code, message, data);
    }

}
