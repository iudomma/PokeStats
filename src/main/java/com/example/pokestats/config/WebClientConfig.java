package com.example.pokestats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	String url = "https://pokeapi.co";

	@Bean
	WebClient webClient() {

		final int size = (int) DataSize.ofMegabytes(16).toBytes();
		final ExchangeStrategies strategies = ExchangeStrategies.builder()
				.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size)).build();

		return WebClient.builder().baseUrl(url).exchangeStrategies(strategies).build();
	}

}
