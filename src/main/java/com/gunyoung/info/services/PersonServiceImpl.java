package com.gunyoung.info.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.PersonRepository;

@Service("personService")
@Transactional
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	PersonRepository personRepository;
	
	@Autowired 
	SpaceService spaceService;

	@Override
	@Transactional(readOnly=true)
	public List<Person> getAll() {
		return personRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Person findById(Long id) {
		return personRepository.getById(id);
	}

	@Override
	public Person save(Person person) {
		return personRepository.save(person);
	}

	@Override
	public void deletePerson(Person person) {
		personRepository.delete(person);
	}

	@Override
	public Space makeSpace(Person person) {
		Space space = new Space();
		
		return space;
	}

	@Override
	public void deleteSpace(Person person) {
		Space space = person.getSpace();
		if(space != null)
			spaceService.deleteSpace(space);
	}

	@Override
	@Transactional(readOnly=true)
	public Person findByEmail(String email) {
		return personRepository.getByEmail(email);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Person> getAllOrderByCreatedAtDesc() {
		return personRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public boolean existsByEmail(String email) {
		return personRepository.existsByEmail(email);
	}

	@Override
	public long countAll() {
		return personRepository.count();
	}
	
	
}
