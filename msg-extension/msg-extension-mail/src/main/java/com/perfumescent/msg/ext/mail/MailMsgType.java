package com.perfumescent.msg.ext.mail;

import com.perfumescent.msg.domain.MsgType;
import org.springframework.stereotype.Component;

/**
 * MailMsgType
 *
 * @author yibowen
 * @date 2024/4/11
 */
@Component
public class MailMsgType implements MsgType {
    @Override
    public String name() {
        return "mail";
    }
}
