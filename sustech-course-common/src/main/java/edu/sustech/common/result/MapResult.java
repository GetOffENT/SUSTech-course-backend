package edu.sustech.common.result;

import edu.sustech.common.enums.ResultCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 后端统一返回结果(data是Map的形式)
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-06 18:05
 */
@Data
public class MapResult {

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    private MapResult() {
    }

    public static MapResult success() {
        MapResult r = new MapResult();
        r.setCode(ResultCode.SUCCESS.code());
        r.setMessage(ResultCode.SUCCESS.message());
        return r;
    }

    public static MapResult error() {
        MapResult r = new MapResult();
        r.setCode(ResultCode.ERROR.code());
        r.setMessage(ResultCode.ERROR.message());
        return r;
    }

    public static MapResult error(String message) {
        MapResult r = new MapResult();
        r.setCode(ResultCode.ERROR.code());
        r.setMessage(message);
        return r;
    }

    public MapResult message(String message) {
        this.setMessage(message);
        return this;
    }

    public MapResult code(Integer code) {
        this.setCode(code);
        return this;
    }

    public MapResult data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public MapResult data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
