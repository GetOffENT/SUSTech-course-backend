package edu.sustech.common.advice;

import edu.sustech.common.config.WebMvcConfiguration;
import edu.sustech.common.exception.BadRequestException;
import edu.sustech.common.exception.BaseException;
import edu.sustech.common.result.Result;
import edu.sustech.common.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.NestedServletException;

import java.net.BindException;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-07 0:00
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnClass(WebMvcConfiguration.class)
public class GlobalExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public Object handleBaseException(BaseException e){
        log.error("异常信息：{}", e.getMessage());
        return processResponse(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg);
        log.debug("", e);
        return processResponse(new BadRequestException(msg));
    }
    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e) {
        log.error("请求参数绑定异常 ->BindException， {}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求参数格式错误"));
    }

    @ExceptionHandler(NestedServletException.class)
    public Object handleNestedServletException(NestedServletException e) {
        log.error("参数异常 -> NestedServletException，{}", e.getMessage());
        log.debug("", e);
        return processResponse(new BadRequestException("请求参数处理异常"));
    }

    @ExceptionHandler(Exception.class)
    public Object handleRuntimeException(Exception e) {
        log.error("其他异常 uri : {} -> ", WebUtils.getRequest().getRequestURI(), e);
        return processResponse(new BaseException("服务器内部异常"));
    }

    private Result processResponse(BaseException e){
        return Result.error(e.getMessage());
    }
}
