package com.gunyoung.info.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.info.domain.Content;

import lombok.Data;

@Data
public class ContentDTO {
	@NotEmpty
	@Email
	@Size(max=50)
	private String hostEmail;
	
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
	
	public void settingByEmailAndContent(String email, Content content) {
		this.hostEmail = email;
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
