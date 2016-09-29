package com.example;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.server.CacheServer;
import com.gemstone.gemfire.internal.DistributionLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.util.StringUtils;

import java.util.Properties;

@SpringBootApplication
@EnableGemfireRepositories
public class GemfireApplication {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(GemfireApplication.class, args);
	}

	protected static final int DEFAULT_LOCATOR_PORT = DistributionLocator.DEFAULT_LOCATOR_PORT;
	protected static final int DEFAULT_MANAGER_PORT = 1199;

	protected static final String DEFAULT_LOG_LEVEL = "config";

	@Value("${myapp.gemfire.region.name}") String regionName;

	@Value("${myapp.gemfire.region.asyncEventQueueId}") String queueId;

	@Bean
	Properties gemfireProperties(@Value("${spring.gemfire.log-level:"+DEFAULT_LOG_LEVEL+"}") String logLevel,
								 @Value("${spring.gemfire.locators:localhost["+DEFAULT_LOCATOR_PORT+"]}") String locators,
								 @Value("${spring.gemfire.manager.port:"+DEFAULT_MANAGER_PORT+"}") int managerPort,
								 @Value("${spring.gemfire.manager.start:false}") boolean jmxManagerStart) {

		logger.warn("spring.gemfire.log-level is [{}]", logLevel);

		Properties gemfireProperties = new Properties();

		gemfireProperties.setProperty("name", "Gemfire");
		gemfireProperties.setProperty("mcast-port", "0");
		gemfireProperties.setProperty("log-level", logLevel);
		gemfireProperties.setProperty("locators", locators);
		gemfireProperties.setProperty("jmx-manager", "true");
		gemfireProperties.setProperty("jmx-manager-port", String.valueOf(managerPort));
		gemfireProperties.setProperty("jmx-manager-start", String.valueOf(jmxManagerStart));

		return gemfireProperties;
	}

	@Bean
	CacheFactoryBean gemfireCache(@Qualifier("gemfireProperties") Properties gemfireProperties) {
		CacheFactoryBean gemfireCache = new CacheFactoryBean();
		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties);
		return gemfireCache;
	}

	@Bean
	PartitionedRegionFactoryBean<String, FileContents> fileSyncRegion(final GemFireCache cache,
				  @Qualifier("regionAttributes") RegionAttributes<String, FileContents> regionAttributes) {
		PartitionedRegionFactoryBean<String, FileContents> fileSyncRegion = new PartitionedRegionFactoryBean<>();
		fileSyncRegion.setAttributes(regionAttributes);
		fileSyncRegion.setCache(cache);
		fileSyncRegion.setClose(false);
		fileSyncRegion.setName(regionName);
		fileSyncRegion.setPersistent(false);
		return fileSyncRegion;
	}

	@Bean
	RegionAttributesFactoryBean regionAttributes() {
		RegionAttributesFactoryBean attributes = new RegionAttributesFactoryBean();
		attributes.addAsyncEventQueueId(queueId);
		attributes.setKeyConstraint(String.class);
		attributes.setValueConstraint(FileContents.class);
		return attributes;
	}
}
