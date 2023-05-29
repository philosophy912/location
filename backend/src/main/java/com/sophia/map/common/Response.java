package com.sophia.map.common;
/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/1/28
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response implements Serializable {
    // 响应代码
    private Integer code = Constant.OK;
    // 响应消息
    private String message;
    // 响应内容
    private Object data;
    // 错误信息
    private String errorInfo;

}
