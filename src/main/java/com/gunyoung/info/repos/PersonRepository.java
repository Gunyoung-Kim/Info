package com.gunyoung.info.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	public Person getByEmail(String email);
	public List<Person> findAllByOrderByCreatedAtDesc();
	public boolean existsByEmail(String email);
	public long count();
}
