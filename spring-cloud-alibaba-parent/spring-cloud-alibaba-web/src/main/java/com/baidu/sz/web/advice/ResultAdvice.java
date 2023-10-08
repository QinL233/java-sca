package com.baidu.sz.web.advice;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.sz.api.entity.ResultInfo;
import com.baidu.sz.api.exception.GlobalError;
import com.baidu.sz.api.utils.ResultGenerator;
import com.baidu.sz.web.anno.NotResult;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果统一处理
 *
 * @author liaoqinzhou_sz
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月12日 14:33:00
 */
@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class ResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        return !returnType.hasMethodAnnotation(NotResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        //如果返回类型是Result类型，则返回
        if (data instanceof ResultInfo) {
            return data;
        }
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                // 将数据包装在BaseResult里后，再转换为json字符串响应给前端
                return JSON.toJSONString(ResultGenerator.success(data));
            } catch (Exception e) {
                throw new IllegalArgumentException("封装resultInfo失败");
            }
        }
        // 将原本的数据包装在ResultVO里
        return ResultGenerator.success(data);
    }

    /**
     * 自定义异常封装
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = GlobalError.CustomException.class)
    @ResponseBody
    public ResponseEntity<ResultInfo> handleCustomException(GlobalError.CustomException exception, HttpServletRequest request,
                                                            HttpServletResponse response) {
        log.error("[自定义异常] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        return new ResponseEntity<>(ResultGenerator.fail(exception.getCode(), exception.getMsg()), HttpStatus.OK);
    }

    /**
     * RPC异常封装：微服务间rpc调用链失败返回
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = RpcException.class)
    @ResponseBody
    public ResponseEntity<ResultInfo> handleRpcException(RpcException exception, HttpServletRequest request,
                                                         HttpServletResponse response) {
        log.error("[RPC链路异常] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        String message = exception.getCause().getMessage();
//        if (StrUtil.startWith(message, "org.apache.dubbo.rpc.StatusRpcException: UNAVAILABLE")) {
//            //链路丢失
//            //UNAVAILABLE : upstream 172.17.130.44:38024 is unavailable
//            return new ResponseEntity<>(ResultGenerator.fail("服务通信异常请等待管理员修复"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        if (StrUtil.startWith(message, "org.apache.dubbo.rpc.StatusRpcException: UNKNOWN") && StrUtil.contains(message, "GlobalError.CustomException")) {
            //自定义异常封装
            //UNKNOWN : com.baidu.sz.kap.exception.RunningError$EndTimeLimitException GlobalError.CustomException(code=500, msg=结束时间需要大于当前时间)
            int begin = message.indexOf("msg=");
            int end = message.indexOf(")");
            if (begin > 0 && end > 0) {
                return new ResponseEntity<>(ResultGenerator.fail(500, message.substring(begin + 4, end)), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(ResultGenerator.fail("服务通信异常请等待管理员修复"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理Controller层参数错误、get、post等方法找不到等异常
     *
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpRequestMethodNotSupportedException.class,
            ServletRequestBindingException.class})
    public ResponseEntity<ResultInfo> handleHttpMsgException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception) {
        log.error("[参数-方法不匹配异常] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        return new ResponseEntity<>(ResultGenerator.fail("请检查参数是否正确"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理Controller层文件上传异常
     *
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {MultipartException.class})
    public ResponseEntity<ResultInfo> handleMultipartException(
            HttpServletRequest request,
            HttpServletResponse response,
            MultipartException exception) {
        log.error("[上传异常] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        return new ResponseEntity<>(ResultGenerator.fail("文件上传失败"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理Controller层参数校验组件springboot validation标记的参数异常
     *
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResultInfo> handleMethodArgumentNotValidException(
            HttpServletRequest request,
            HttpServletResponse response,
            MethodArgumentNotValidException exception) {
        log.error("[参数校验异常] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        BindingResult bindingResult = exception.getBindingResult();
        List<String> errorMsgList = new ArrayList(bindingResult.getFieldErrorCount());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsgList.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(ResultGenerator.fail(errorMsgList.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 文本异常封装
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ResultInfo> illegalArgumentException(Exception exception, HttpServletRequest request,
                                                               HttpServletResponse response) {
        log.error("[文本异常封装] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        return new ResponseEntity<>(ResultGenerator.fail(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 未知异常封装
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ResultInfo> handleUnknownException(Exception exception, HttpServletRequest request,
                                                             HttpServletResponse response) {
        log.error("[未知异常封装] {} {} {}", request.getMethod(), request.getRequestURI(), ThrowableUtil.stackTraceToString(exception));
        return new ResponseEntity<>(ResultGenerator.fail("系统异常请等待管理员修复"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}