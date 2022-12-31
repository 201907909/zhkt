package com.wisdom.user.service;

import com.wisdom.base.util.R;
import com.wisdom.user.domain.MailReq;

/**
 * @author WisdomCR
 * @since 2022-10-17
 */
public interface MailService {
    R sendMail(MailReq mailReq);
}
