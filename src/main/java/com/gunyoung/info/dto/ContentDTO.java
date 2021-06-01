package com.gunyoung.info.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.info.domain.Content;

public class ContentDTO {
	private String hostEmail;
	
	private String title;
	
	private String description;
	
	private String contributors;
	
	private String skillstacks;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startedAt;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endAt;
	
	private String contents;
	
	private String links;
	
	public ContentDTO() {
		
	}
	
	public String getHostEmail() {
		return hostEmail;
	}
	public void setHostEmail(String hostEmail) {
		this.hostEmail = hostEmail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContributors() {
		return contributors;
	}
	public void setContributors(String contributors) {
		this.contributors = contributors;
	}
	public String getSkillstacks() {
		return skillstacks;
	}
	public void setSkillstacks(String skillstacks) {
		this.skillstacks = skillstacks;
	}
	public Date getStartedAt() {
		return startedAt;
	}
	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}
	public Date getEndAt() {
		return endAt;
	}
	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
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
