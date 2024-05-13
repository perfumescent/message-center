package com.perfumescent.msg.agent;

import com.perfumescent.msg.domain.MsgType;

public interface AcceptableMsgConfig {
    
    void registerAcceptableMsgType(MsgType msgType);
    void registerAcceptableMsgSource();
}