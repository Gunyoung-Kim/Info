package com.gunyoung.info.services;

import java.util.List;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;

public interface PersonService {
	public List<Person> getAll();
	public List<Person> getAllOrderByCreatedAtDesc();
	public Person findById(Long id);
	public Person save(Person person);
	public void deletePerson(Person person);
	public Space makeSpace(Person person);
	public void deleteSpace(Person person);
	public Person findByEmail(String email);
}
