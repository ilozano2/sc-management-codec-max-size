package io.ilozano.lab.managementcodecmaxsize;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "management.codec")
public class ManagementCodecProperties {
	private DataSize maxInMemorySize;

	public DataSize getMaxInMemorySize() {
		return this.maxInMemorySize;
	}

	public void setMaxInMemorySize(DataSize maxInMemorySize) {
		this.maxInMemorySize = maxInMemorySize;
	}
}