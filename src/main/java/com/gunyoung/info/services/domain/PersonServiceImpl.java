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
	
	private final SpaceService spaceService;
	
	@Override
	@Transactional(readOnly=true)
	public Person findById(Long id) {
		Optional<Person> result = personRepository.findById(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Person findByIdWithSpace(Long id) {
		Optional<Person> result = personRepository.findByIdWithSpace(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Person findByEmail(String email) {
		Optional<Person> result = personRepository.findByEmail(email);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Person findByEmailWithSpace(String email) {
		Optional<Person> result = personRepository.findByEmailWithSpace(email);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Person findByIdWithSpaceAndContents(Long id) {
		Optional<Person> result = personRepository.findByIdWithSpaceAndContents(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Person findByEmailWithSpaceAndContents(String email) {
		Optional<Person> result = personRepository.findByEmailWithSpaceAndContents(email);
		return result.orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Person> findAllOrderByCreatedAtDesc() {
		return personRepository.findAllByOrderByCreatedAtDesc();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Person> findAllInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1 , PAGE_SIZE);
		return personRepository.findAll(pageRequest);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Person> findAllOrderByCreatedAtDescInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PAGE_SIZE);
		return personRepository.findAllByOrderByCreatedAtDesc(pageRequest);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Person> findAllOrderByCreatedAtAscInPage(Integer pageNumber) {
		PageRequest pageRequest = PageRequest.of(pageNumber-1, PAGE_SIZE);
		return personRepository.findAllByOrderByCreatedAtAsc(pageRequest);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<Person> findByNameKeywordInPage(Integer pageNumber, String keyword) {
		PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);
		return personRepository.findByNameWithKeyword(keyword, pageRequest);
	}

	@Override
	public Person save(Person person) {
		spaceService.save(person.getSpace());
		return personRepository.save(person);
	}

	@Override
	public void delete(Person person) {
		personRepository.delete(person);
		Long personIdForRemove = person.getId();
		spaceService.deleteByPerson(personIdForRemove);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsByEmail(String email) {
		return personRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return personRepository.count();
	}
	
	@Override
	@Transactional(readOnly=true)
	public long countWithNameKeyword(String keyword) {
		return personRepository.countWithNameKeyword(keyword);
	}
}
