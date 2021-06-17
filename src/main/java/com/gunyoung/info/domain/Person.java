package com.gunyoung.info.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.gunyoung.info.enums.RoleType;

import lombok.Getter;
import lombok.Setter;

/**
 * 본 서비스 이용하는 회원을 의미하는 객체
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Table(name="person")
@EntityListeners(AuditingEntityListener.class)
public class Person {
	
	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="role_type")
	@Enumerated(EnumType.STRING)
	private RoleType role = RoleType.USER; 
	
	@Column(length=50)
	@NotEmpty(message="{email.notEmpty}")
	@Email(message="{email.email}")
	private String email;
	
	@Version
	@Column
	private int version;
	
	@NotEmpty(message="{password.notEmpty}")
	@Column
	//@Password(message="{password.password}")
	private String password;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@NotEmpty(message="{firstName.notEmpty}")
	@Size(max =60, message="{firstName.size}")
	@Column
	private String firstName;
	
	@NotEmpty(message="{lastName.notEmpty}")
	@Size(max=60, message="{lastName.size}")
	@Column
	private String lastName;
	
	@OneToOne(fetch = FetchType.EAGER,cascade= CascadeType.ALL , orphanRemoval = true)
	@JoinColumn(name="space_id")
	private Space space;
	
	public Person() {
		Space space = new Space();
		this.space = space;
	}
	
	public Person(String email, String password, String firstName, String lastName) {
		this();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Transient
	public String getFullName() {
		return this.firstName +" " + this.lastName;
	}
	
	
}
