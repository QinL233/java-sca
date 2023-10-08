package com.baidu.sz.rpc.filter;

import com.baidu.sz.rpc.utils.RpcContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * 服务提供者（本地方法被对方调用时前执行，即程序运行到本地线程之前）
 * 1、从rpc管道中获取context
 * 2、执行完成清空内存资源
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年05月19日 09:56:00
 */
@Activate
@Slf4j
public class ProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            //（提供者）接收端从RPC中获取session存放到当前线程中
            RpcContextUtil.getByRpc(RpcContext.getContext());
            //若方法继续请求第三方服务，这递归进入filter
            return invoker.invoke(invocation);
        } finally {
            //（提供者）接收端执行完方法后，必须清空当前线程的资源以防内存溢出
            RpcContextUtil.clear();
        }
    }
}
