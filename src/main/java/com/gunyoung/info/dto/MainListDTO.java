package com.gunyoung.info.dto;

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
	private String personName;
	private String personEmail;
}
