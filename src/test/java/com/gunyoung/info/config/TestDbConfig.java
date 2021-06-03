package com.gunyoung.info.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TestDbConfig {
	
	@Bean
	public DataSource dataSource() {
		System.out.println("dddddd");
		try {
			EmbeddedDatabaseBuilder dbBuilder = new EmbeddedDatabaseBuilder();
			return dbBuilder.setType(EmbeddedDatabaseType.H2)
					.build();
		} catch(Exception e) {
			return null;
		}
	}
}
