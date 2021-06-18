package com.gunyoung.info.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.info.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	public Person getByEmail(String email);
	public List<Person> findAllByOrderByCreatedAtDesc();
	public List<Person> findAllByOrderByCreatedAtAsc();
	public boolean existsByEmail(String email);
	public long count();
	public Page<Person> findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<Person> findAllByOrderByCreatedAtAsc(Pageable pageable);
	
	/**
	 * parameter로 전달된 keyword 값을 포함하는 firstName 이나 lastName 가진 Person 객체들 가져오기 위한 메소드 
	 * 
	 * @param keyword 검색하려는 firstName 이나 lastName
	 * @param pageable
	 * @return 해당 검색 조건을 만족하는 Person 컬렉션
	 */
	@Query(value="select p from Person p "
			+ "where p.firstName like %:keyword% "
			+ "or p.lastName like %:keyword% ")
	public Page<Person> getByNameWithKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	
	/**
	 * parameter로 전달된 keyword 값을 포함하는 firstName 이나 lastName 가진 Person 개수를 반환하는 메소드
	 * @param keyword 검색하려는 firstName 이나 lastName
	 * @return
	 */
	@Query(value="select count(p) from Person p "
			+ "where p.firstName like %:keyword% "
			+ "or p.lastName like %:keyword% ")
	public long countWithNameKeyword(@Param("keyword") String keyword);
}
