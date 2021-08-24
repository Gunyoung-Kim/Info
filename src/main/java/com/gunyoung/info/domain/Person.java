package com.gunyoung.info.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.gunyoung.info.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 본 서비스 이용하는 회원을 의미하는 객체
 * @author kimgun-yeong
 *
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends BaseEntity{
	
	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="role_type")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private RoleType role = RoleType.USER; 
	
	@Column(length=50)
	@NotEmpty(message="{email.notEmpty}")
	@Email(message="{email.email}")
	private String email;
	
	@NotEmpty(message="{password.notEmpty}")
	@Column
	private String password;
	
	@NotEmpty(message="{firstName.notEmpty}")
	@Size(max =60, message="{firstName.size}")
	@Column
	private String firstName;
	
	@NotEmpty(message="{lastName.notEmpty}")
	@Size(max=60, message="{lastName.size}")
	@Column
	private String lastName;
	
	@OneToOne(fetch = FetchType.LAZY,cascade= CascadeType.ALL , orphanRemoval = true)
	@JoinColumn(name="space_id")
	@Builder.Default
	private Space space = Space.builder().build();
	
	@Transient
	public String getFullName() {
		return this.firstName +" " + this.lastName;
	}
}
