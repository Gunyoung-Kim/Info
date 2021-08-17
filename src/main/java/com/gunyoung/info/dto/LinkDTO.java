package com.gunyoung.info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkDTO {
	
	private Long id;
	
	private String tag;
	
	private String url;
}
