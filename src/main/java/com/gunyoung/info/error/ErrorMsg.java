package com.gunyoung.info.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsg {
	private String errorCode;
	private String description;
}
