package com.gunyoung.info.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.info.domain.Content;

import lombok.Data;

/**
 * 콘텐트 내용 수정을 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
public class ContentDTO {
	
	@NotEmpty
	private Long hostId;
	
	@NotEmpty
	private String title;
	
	private String description;
	
	@NotEmpty
	private String contributors;
	
	private String skillstacks;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startedAt;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endAt;
	
	@NotEmpty
	private String contents;
	
	private String links;
	
	public ContentDTO() {
		
	}
	
	public void settingByHostIdAndContent(Long hostId, Content content) {
		this.hostId = hostId;
		this.title = content.getTitle();
		this.description = content.getDescription();
		this.contributors = content.getContributors();
		this.skillstacks = content.getSkillstacks();
		this.startedAt = content.getStartedAt();
		this.endAt = content.getEndAt();
		this.contents = content.getContents();
		this.links = content.getLinks();
	}
}
