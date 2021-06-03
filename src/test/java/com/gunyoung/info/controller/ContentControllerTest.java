package com.gunyoung.info.controller;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.gunyoung.info.config.TestDbConfig;
import com.gunyoung.info.services.PersonService;

@SpringBootTest(
		classes= {TestDbConfig.class}
)
@AutoConfigureMockMvc
public class ContentControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	DataSource datasource;
	
	@Autowired
	PersonService personService;
	
	@Test
	public void test() {
		try {
			System.out.println(datasource.getConnection().getSchema());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(personService.getAll().size());
		System.out.println("hello");
	}
}
