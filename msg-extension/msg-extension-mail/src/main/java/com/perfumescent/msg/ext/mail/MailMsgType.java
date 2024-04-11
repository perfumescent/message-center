package com.perfumescent.msg.ext.mail;

import com.perfumescent.msg.domain.MsgType;
import lombok.Data;

/**
 * MailMsgType
 *
 * @author yibowen
 * @date 2024/4/11
 */
@Data
public class MailMsgType implements MsgType {
    @Override
    public String name() {
        return "mail";
    }
}
