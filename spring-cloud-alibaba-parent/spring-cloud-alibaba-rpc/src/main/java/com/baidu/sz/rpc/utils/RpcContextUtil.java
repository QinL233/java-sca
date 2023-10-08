package com.baidu.sz.rpc.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.sz.rpc.entity.CurrentContext;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月01日 17:50:00
 */
public class RpcContextUtil {

    private final static String CURRENT_CONTEXT = "current_context";

    /**
     * 初始化
     */
    public static void init(CurrentContext context) {
        if (Objects.nonNull(context)) {
            MDC.put(CURRENT_CONTEXT, JSON.toJSONString(context));
        }
    }

    /**
     * 从 MDC 中清除当前线程的上下文信息
     */
    public static void clear() {
        MDC.clear();
    }

    /**
     * 从 RpcContext 中获取
     * 给 Dubbo 服务端调用
     *
     * @param context Dubbo 的 RPC
     */
    public static void getByRpc(RpcContext context) {
        String data = context.getAttachment(CURRENT_CONTEXT);
        if (StrUtil.isNotBlank(data)) {
            MDC.put(CURRENT_CONTEXT, data);
        }
    }

    /**
     * 将 token 相关信息 放入 RPC
     * 给 Dubbo 消费端调用
     *
     * @param context Dubbo 的 RPC
     */
    public static void setToRpc(RpcContext context) {
        String data = MDC.get(CURRENT_CONTEXT);
        if (StrUtil.isNotBlank(data)) {
            context.setAttachment(CURRENT_CONTEXT, data);
        }
    }

    /**
     * 从当前 MDC获取
     *
     * @return
     */
    public static CurrentContext get() {
        String data = MDC.get(CURRENT_CONTEXT);
        if (StrUtil.isNotBlank(data)) {
            return JSON.parseObject(data, CurrentContext.class);
        }
        return new CurrentContext();
    }


}
