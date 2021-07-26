package com.gunyoung.info.services.domain;

import java.util.List;

import org.springframework.data.domain.Page;

import com.gunyoung.info.domain.Person;

public interface PersonService {
	public List<Person> getAll();
	public List<Person> getAllOrderByCreatedAtDesc();
	public Person findById(Long id);
	public Person save(Person person);
	public void deletePerson(Person person);
	public Person findByEmail(String email);
	public Person findByEmailWithSpace(String email);
	public boolean existsByEmail(String email);
	public long countAll();
	public long countWithNameKeyword(String keyword);
	public Page<Person> getAllInPage(Integer pageNumber);
	public Page<Person> getAllOrderByCreatedAtDescInPage(Integer pageNumber);
	public Page<Person> getAllOrderByCreatedAtAscInPage(Integer pageNumber);
	public Page<Person> findByNameKeywordInPage(String keyword);
}
