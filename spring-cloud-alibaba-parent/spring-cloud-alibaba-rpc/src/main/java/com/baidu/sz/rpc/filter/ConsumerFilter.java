package com.baidu.sz.rpc.filter;

import com.baidu.sz.rpc.utils.RpcContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * 服务消费者，当前方法远程调用前执行
 * 1、需要讲本地线程的数据set到rpc管道中
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年05月19日 09:56:00
 */
@Activate
@Slf4j
public class ConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //（消费者）请求端将当前线程中的session放入RPC
        RpcContextUtil.setToRpc(RpcContext.getContext());
        //若方法继续请求第三方服务，这递归进入filter
        return invoker.invoke(invocation);
    }
}
