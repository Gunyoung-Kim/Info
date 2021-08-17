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
 * 콘텐트 내용 수정,열람, 저장을 위한 DTO 객체
 * @author kimgun-yeong
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {
	
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
	
	/**
	 * Content Host Person Id, Content 필드들로 ContentDTO 필드 값 설정 <br>
	 * links는 따로 설정하지 않음
	 * @author kimgun-yeong
	 */
	public void settingHostIdAndContentField(Long hostId, Content content) {
		this.hostId = hostId;
		this.title = content.getTitle();
		this.description = content.getDescription();
		this.contributors = content.getContributors();
		this.skillstacks = content.getSkillstacks();
		this.startedAt = content.getStartedAt();
		this.endAt = content.getEndAt();
		this.contents = content.getContents();
	}
	
	/**
	 * Link 리스트를 통해 links 필드 설정
	 * @author kimgun-yeong
	 */
	public void settingLinks(List<Link> links) {
		for(int i=0; i < links.size() ; i++) {
			Link link = links.get(i);
			LinkDTO linkDTO = LinkDTO.builder()
					.id(link.getId())
					.tag(link.getTag())
					.url(link.getUrl())
					.build();
			
			this.links.add(linkDTO);
		}
	}
	
	/**
	 * ContentDTO에 담긴 정보로 Content만 업데이트 <br>
	 * Link는 업데이트하지 않음 
	 * @param content 업데이트하려는 Content
	 * @author kimgun-yeong
	 */
	public void updateContentOnly(Content content) {
		content.setTitle(this.title);
		content.setDescription(this.description);
		content.setContributors(this.contributors);
		content.setSkillstacks(this.skillstacks);
		content.setStartedAt(this.startedAt);
		content.setEndAt(this.endAt);
		content.setContents(this.contents);
	}
	
	/**
	 * ContentDTO에 담긴 정보로 Content 생성 후 반환 <br>
	 * Link는 따로 추가하지 않는다
	 * @author kimgun-yeong
	 */
	public Content createContentOnly() {
		Content newContent = Content.builder()
				.title(this.title)
				.description(this.description)
				.contributors(this.contributors)
				.skillstacks(this.skillstacks)
				.startedAt(this.startedAt)
				.endAt(this.endAt)
				.contents(this.contents)
				.build();
		
		return newContent;
	}
	
	/**
	 * ContentDTO에 담긴 정보로 Link 리스트 생성 후 반환 <br>
	 * Link에 인자로 입력된 Content와 연관관계 설정
	 * @author kimgun-yeong
	 */
	public List<Link> createLinkListOnlyWithContent(Content content) {
		List<Link> linkList = new ArrayList<>();
		
		for(LinkDTO linkDTO: this.links) {
			Link link = Link.builder()
					.tag(linkDTO.getTag())
					.url(linkDTO.getUrl())
					.content(content)
					.build();
			
			linkList.add(link);
		}
		
		return linkList;
	}
}
