package io.ilozano.lab.managementcodecmaxsize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;

@AutoConfiguration
@ManagementContextConfiguration(value = ManagementContextType.CHILD, proxyBeanMethods = false)
@EnableConfigurationProperties(ManagementCodecProperties.class)
public class ManagementCodecWebFluxConfiguration extends WebFluxConfigurationSupport {

	@Autowired
	ManagementCodecProperties managementCodecProperties;

	@Override
	protected void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		if (managementCodecProperties.getMaxInMemorySize() != null) {
			configurer.defaultCodecs().maxInMemorySize((int) managementCodecProperties.getMaxInMemorySize().toBytes());
		}
		super.configureHttpMessageCodecs(configurer);
	}
}
