package org.apache.http.spring.boot;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ CloseableHttpClient.class })
@EnableConfigurationProperties(HttpclientProperties.class)
public class HttpclientAutoConfiguration {

	
	
	
}
