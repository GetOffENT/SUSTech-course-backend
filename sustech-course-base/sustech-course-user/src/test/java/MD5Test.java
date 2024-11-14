import com.anji.captcha.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-15 5:42
 */
public class MD5Test {

    @Test
    public void test() {
        System.out.println(MD5Util.md5("123456"));
        System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
    }
}
