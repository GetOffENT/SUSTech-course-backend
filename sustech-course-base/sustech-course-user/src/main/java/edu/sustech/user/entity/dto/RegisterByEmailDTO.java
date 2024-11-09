package edu.sustech.user.entity.dto;

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
 * @since 2024-11-09 15:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterByEmailDTO implements Serializable{
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    @Length(min = 6, max = 6)
    private String captcha;
    @NotNull
    @Max(2)
    @Min(0)
    private Integer role;
    @NotNull
    private String captchaVerification;
}
