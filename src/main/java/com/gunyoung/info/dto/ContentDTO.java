package com.gunyoung.info.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 콘텐트 내용 수정을 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	
	@Builder.Default
	private List<LinkDTO> links = new ArrayList<>();
	
	public void settingByHostIdAndContent(Long hostId, Content content) {
		this.hostId = hostId;
		this.title = content.getTitle();
		this.description = content.getDescription();
		this.contributors = content.getContributors();
		this.skillstacks = content.getSkillstacks();
		this.startedAt = content.getStartedAt();
		this.endAt = content.getEndAt();
		this.contents = content.getContents();
		
		for(Link link: content.getLinks()) {
			LinkDTO linkDTO = LinkDTO.builder()
					.tag(link.getTag())
					.url(link.getUrl())
					.build();
			this.links.add(linkDTO);
		}
	}
	
	/**
	 * ContentDTO에 담긴 정보로 Content 업데이트 
	 * @param content 업데이트하려는 Content
	 * @author kimgun-yeong
	 */
	public void updateContent(Content content) {
		content.setTitle(this.title);
		content.setDescription(this.description);
		content.setContributors(this.contributors);
		content.setSkillstacks(this.skillstacks);
		content.setStartedAt(this.startedAt);
		content.setEndAt(this.endAt);
		content.setContents(this.contents);
		 
		List<Link> newContentLinks = new ArrayList<>();
		
		for(LinkDTO linkDTO: this.links) {
			Link link = Link.builder()
					.tag(linkDTO.getTag())
					.url(linkDTO.getUrl())
					.build();
			
			newContentLinks.add(link);
		}
		
		content.setLinks(newContentLinks);
	}
}
