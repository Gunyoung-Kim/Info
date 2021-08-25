package com.gunyoung.info.dto;

import java.util.LinkedList;
import java.util.List;

import com.gunyoung.info.domain.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 메인 화면에 나타나는 리스트의 내용을 전달하기 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainListDTO {
	private Long personId;
	private String personName;
	private String personEmail;
	
	/**
	 * Person 컬렉션을 통해 MainListDTO 리스트 반환
	 * @author kimgun-yeong
	 */
	public static List<MainListDTO> of(Iterable<Person> people) {
		List<MainListDTO> dtos = new LinkedList<>();
		for(Person p : people) {
			MainListDTO mainListDTO = MainListDTO.builder()
					.personId(p.getId())
					.personName(p.getFullName())
					.personEmail(p.getEmail())
					.build();
			dtos.add(mainListDTO);
		}
		return dtos;
	}
}
