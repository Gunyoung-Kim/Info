package com.gunyoung.info.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 프로젝트 관련 URL을 나타내는 객체
 * @author kimgun-yeong
 *
 */
@Entity
@Table(name="link")
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Link {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Version
	private int version;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime modifiedAt;
	
	private String description;
	
	private String url;
}
