package com.baidu.sz.rpc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年07月04日 11:12:00
 */
@Data
@NoArgsConstructor
public class CurrentContext implements Serializable {

    private String token;

    private String uuapLoginUser;

    private String uri;

}
