package com.wisdom.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;
import java.io.Serializable;

/**
 * @author WisdomCR
 * @since 2022-05-23
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MailReq",description = "邮箱对象")
public class MailReq implements Serializable {
    private String from;
    private String to;
    private String subject;
    private String text;
    @JsonIgnore
    private MultipartFile[] multipartFiles;
}
