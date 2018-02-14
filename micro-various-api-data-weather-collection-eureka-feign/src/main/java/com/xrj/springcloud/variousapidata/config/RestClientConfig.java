package com.xrj.springcloud.variousapidata.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 通过设置连接池的方式创建RestTemplate
 * @author crixus
 *
 */
@Configuration
public class RestClientConfig {
	
	private static final int SOCKET_TIME_OUT = 6000;
	private static final int CONNECTION_TIME_OUT = 6000;
	private static final int CONNECTION_REQUEST_TIME_OUT = 6000;
	

	/**
	 * PoolingHttpClientConnectionManager 可以为多线程的请求连接创建一个连接池，
	 * 连接是按照路径来合并的，连建池是持久化的连接，每次连接是从池中获取一个已经存在的连接，而不是重新创建一个。
	 * requestConfig 来设置自定义的连接参数
	 * @return
	 */
	@Bean
	public HttpClient httpClient() {
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory())
				.build();
		
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(5);
		connectionManager.setDefaultMaxPerRoute(5);
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIME_OUT)
				.setConnectTimeout(CONNECTION_TIME_OUT)
				.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
				.build();
		
		HttpClient httpClient = HttpClientBuilder.create()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(connectionManager)
				.build();
		return httpClient;
	}
	
	@Bean
	public ClientHttpRequestFactory httpRequestFactory(){
		return new HttpComponentsClientHttpRequestFactory(httpClient());
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(httpRequestFactory());
	}
	
}
