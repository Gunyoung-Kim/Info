package com.gunyoung.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트와 Link 엔티티 값 주고 받기 위한 DTO 객체 
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
	private Long id;
	private String tag;
	private String url;
}
