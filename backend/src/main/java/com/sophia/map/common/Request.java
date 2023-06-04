package com.sophia.map.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/*
 * @author lizhe
 * @email lizhe@bdstar.com
 * @create 2023/2/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request implements Serializable {
    private Float x1;
    private Float y1;
    private Float x2;
    private Float y2;

}
