package org.terracotta.config;

import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.List;

public class TcConfiguration {
  private final TcConfig platformConfiguration;

  private final List<?> serviceConfigurations;

  private final String source;

  public TcConfiguration(TcConfig platformConfiguration, String source ,List<?> serviceConfigurations) {
    this.source = source;
    this.platformConfiguration = platformConfiguration;
    this.serviceConfigurations = serviceConfigurations;
  }

  public TcConfig getPlatformConfiguration() {
    return platformConfiguration;
  }


  public Object getServiceConfiguration(Class<?> serviceType) {
    for (Object o : serviceConfigurations) {
      if(o.getClass().equals(serviceType)) {
        return o;
      }
    }
    try {
      Constructor cons;
      try {
        cons = serviceType.getConstructor(String.class);
        return cons.newInstance(source);
      } catch (NoSuchMethodException ex) {
        cons = serviceType.getConstructor();
        return cons.newInstance();
      }
    } catch (Exception e) {
      throw new TCConfigurationSetupException(e);
    }
  }


  public List<?> getServiceConfigurations() {
    return this.serviceConfigurations;
  }

  @Override
  public String toString() {
    StringWriter sw = new StringWriter();
    JAXB.marshal(platformConfiguration, sw);
    return sw.toString();
  }

}
