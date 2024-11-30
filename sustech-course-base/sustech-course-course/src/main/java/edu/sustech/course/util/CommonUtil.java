package edu.sustech.course.util;

import edu.sustech.api.client.UserClient;
import edu.sustech.api.entity.dto.UserDTO;
import edu.sustech.common.constant.MessageConstant;
import edu.sustech.common.exception.CourseException;
import edu.sustech.common.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-29 8:29
 */
@Component
@RequiredArgsConstructor
public class CommonUtil {

    private final UserClient userClient;

    public Long checkTeacher() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }

        // TODO: redis缓存用户
        UserDTO user = userClient.getUserById(userId).getData();
        if (user.getRole() != 2) {
            throw new CourseException(MessageConstant.NOT_TEACHER);
        }
        return userId;
    }

    public Long checkUser() {
        Long userId = UserContext.getUser();
        if (userId == null) {
            throw new CourseException(MessageConstant.NOT_LOGIN);
        }
        return userId;
    }
}
