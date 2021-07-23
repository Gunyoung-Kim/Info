package com.gunyoung.info.services.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Person;
import com.gunyoung.info.repos.PersonRepository;

import lombok.RequiredArgsConstructor;

@Service("personService")
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
	
	private static final int PAGE_SIZE = 10;
	
	private final PersonRepository personRepository;

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
	@Transactional(readOnly=true)
	public Person findByEmail(String email) {
		Optional<Person> result = personRepository.findByEmail(email);
		if(!result.isPresent()) 
			return null;
		return result.get();
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
	
	@Override
	public long countWithNameKeyword(String keyword) {
		return personRepository.countWithNameKeyword(keyword);
	}
	

	@Override
	public Page<Person> getAllInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1 , PAGE_SIZE);
		return personRepository.findAll(pageRequest);
	}

	@Override
	public Page<Person> getAllOrderByCreatedAtDescInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PAGE_SIZE);
		return personRepository.findAllByOrderByCreatedAtDesc(pageRequest);
	}

	@Override
	public Page<Person> getAllOrderByCreatedAtAscInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PAGE_SIZE);
		return personRepository.findAllByOrderByCreatedAtDesc(pageRequest);
	}

	@Override
	public Page<Person> findByNameKeywordInPage(String keyword) {
		PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
		return personRepository.findByNameWithKeyword(keyword, pageRequest);
	}

	
}
