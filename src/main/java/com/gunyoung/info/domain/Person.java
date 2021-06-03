package com.gunyoung.info.domain;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name="person")
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Person {
	
	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@OneToOne(fetch = FetchType.EAGER,cascade= CascadeType.ALL, orphanRemoval = true)
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}
	
	@Transient
	public String getFullName() {
		return this.firstName +" " + this.lastName;
	}
	
	
}
