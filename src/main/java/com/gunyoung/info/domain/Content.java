package com.gunyoung.info.domain;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="content")
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Content {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Version
	@Column
	private int version;
	
	@Column
	@NotEmpty
	@Size(max=100)
	private String title;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime modifiedAt;
	
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "space_id")
	private Space space;

	public Content() {
		
	}	
	
}
