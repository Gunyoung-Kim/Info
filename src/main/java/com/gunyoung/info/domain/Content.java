package com.gunyoung.info.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 개인의 포트폴리오를 구성하는 '프로젝트'를 의미하는 객체
 * @author kimgun-yeong
 *
 */
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content extends BaseEntity{
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@NotEmpty
	@Size(max=100)
	private String title;
	
	@Column
	private String description;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startedAt;
	
	@Column
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endAt;
	
	@Column
	private String skillstacks;
	
	@Column
	@NotEmpty
	private String contributors;
	
	@Column(columnDefinition="TEXT")
	private String links;
	
	@Column(columnDefinition="TEXT NOT NULL")
	private String contents;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id")
	private Space space;
}
