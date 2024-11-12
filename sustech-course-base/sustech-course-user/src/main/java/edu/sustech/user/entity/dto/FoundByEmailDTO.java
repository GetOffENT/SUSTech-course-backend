package edu.sustech.user.entity.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author Yuxian Wu
 * @version 1.0
 * @since 2024-11-11 1:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "找回密码表单")
public class FoundByEmailDTO implements Serializable {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @Length(min = 6, max = 6)
    private String captcha;
    @NotNull
    private String captchaVerification;
}
