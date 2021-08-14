package com.gunyoung.info.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 개인의 포트폴리오 정보를 구성하는 객체
 * @author kimgun-yeong
 *
 */
@Entity
@Table(name = "space")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Space {
	
	@Id
	@Column(name= "space_id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column
	private int version;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime modifiedAt;
	
	@Column(columnDefinition="TEXT")
	private String description = "";
	
	@Column
	private String github = "";
	
	@Column 
	private String instagram = "";
	
	@Column
	private String tweeter = "";
	
	@Column
	private String facebook = ""; 
	
	@OneToMany(mappedBy="space",cascade=CascadeType.REMOVE)
	private List<Content> contents = new ArrayList<>();
	
	@OneToOne(mappedBy="space")
	private Person person;
	
	public Space() {
		
	}
	
}
