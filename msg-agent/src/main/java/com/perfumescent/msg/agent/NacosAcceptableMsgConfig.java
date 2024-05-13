package com.perfumescent.msg.agent;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.perfumescent.msg.domain.MsgType;
import com.perfumescent.msg.infra.NacosDiscoveryConfig;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * NacosAcceptableMsgConfig
 *
 * @author yibowen
 * @date 2024/5/13
 */
@Component
public class NacosAcceptableMsgConfig implements AcceptableMsgConfig {

  public static final String MSG_TYPE_KEY = "accept-msg-type";
  @Resource private NacosDiscoveryConfig nacosDiscoveryConfig;

  @Override
  public void registerAcceptableMsgType(MsgType msgType) {
    Instance selfService = nacosDiscoveryConfig.getSelfService();
    String value = selfService.getMetadata().get(MSG_TYPE_KEY);
    Set<String> msgTypes =
            StrUtil.isBlank(value) ? new HashSet<>(1) : new HashSet<>(JSON.parseArray(value, String.class));
    msgTypes.add(msgType.name());
    selfService.addMetadata(MSG_TYPE_KEY, JSON.toJSONString(msgTypes));
    nacosDiscoveryConfig.updateInstance(selfService);
  }

  @Override
  public void registerAcceptableMsgSource() {}
}
