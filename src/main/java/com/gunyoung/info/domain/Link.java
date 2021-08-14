package com.gunyoung.info.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * URL들을 나타내는 엔티티
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link extends BaseEntity {
	
	@Id
	@Column(name="link_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	/*
	 * URL에 대한 간단한 이름
	 */
	private String tag;
	
	@URL
	private String url;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="content_id")
	private Content content;
}
