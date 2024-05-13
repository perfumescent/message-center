package com.perfumescent.msg.infra;

import com.alibaba.boot.nacos.discovery.properties.NacosDiscoveryProperties;
import com.alibaba.boot.nacos.discovery.properties.Register;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;

/**
 * NacosDiscoveryConfig
 *
 * @author yibowen
 * @date 2024/5/11
 */
@Configuration
public class NacosDiscoveryConfig {
  @NacosInjected private NamingService namingService;

  @Resource private NacosDiscoveryProperties discoveryProperties;

  public void updateInstance(Instance self) {
    try {
      namingService.registerInstance("msg-agent", self);
    } catch (NacosException e) {
      throw new RuntimeException(e);
    }
  }

  public Instance getSelfService() {
    try {
      List<Instance> allInstances = namingService.getAllInstances("msg-agent");
      Register register = discoveryProperties.getRegister();
      return allInstances.stream()
          .filter(
              instance ->
                  instance.getIp().equals(register.getIp())
                      && instance.getPort() == register.getPort())
          .findFirst()
          .orElseThrow();
    } catch (NacosException e) {
      throw new RuntimeException(e);
    }
  }
}
