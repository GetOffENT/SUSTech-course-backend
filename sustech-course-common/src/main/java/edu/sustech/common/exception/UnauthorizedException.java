package edu.sustech.common.exception;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-07 7:05
 */
public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
