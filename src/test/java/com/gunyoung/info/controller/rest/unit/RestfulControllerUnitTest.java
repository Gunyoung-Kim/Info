package com.gunyoung.info.controller.rest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gunyoung.info.controller.rest.RestfulController;
import com.gunyoung.info.domain.Person;
import com.gunyoung.info.dto.MainListDTO;
import com.gunyoung.info.services.domain.PersonService;
import com.gunyoung.info.util.PersonTest;

/**
 * {@link RestfulController} 에 대한 테스트 클래스 <br>
 * 테스트 범위: (단위 테스트) RestfulController only
 * {@link org.mockito.BDDMockito}를 활용한 컨트롤러 계층에 대한 단위 테스트
 * @author kimgun-yeong
 *
 */
@ExtendWith(MockitoExtension.class)
public class RestfulControllerUnitTest {
	
	@Mock
	PersonService personService;
	
	@InjectMocks
	RestfulController restfulController;
	
	/*
	 * public List<MainListDTO> index() 
	 */
	
	@Test
	@DisplayName("main 화면에서 노출할 리스트를 반환 -> 정상")
	public void indexTest() {
		//Given
		Long givenPersonNum = Long.valueOf(15);
		List<Person> givenPeople = new ArrayList<>();
		
		for(int i=0; i < givenPersonNum; i++) {
			Person person = PersonTest.getPersonInstance();
			givenPeople.add(person);
		}
		
		given(personService.findAll()).willReturn(givenPeople);
		
		//When
		List<MainListDTO> result = restfulController.index();
		
		//Then
		assertEquals(givenPersonNum, result.size());
	}
}
