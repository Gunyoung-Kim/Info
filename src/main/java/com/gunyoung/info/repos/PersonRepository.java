package com.gunyoung.info.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.info.domain.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	
	/**
	 * email로 Person 찾기
	 * @param email 찾으려는 Person email
	 * @author kimgun-yeong
	 */
	public Optional<Person> findByEmail(String email);
	
	/**
	 * id로 Person 찾기 <br>
	 * Space 페치 조인
	 * @param id 찾으려는 Person Id
	 * @author kimgun-yeong
	 */
	@Query("SELECT p FROM Person p "
			+ "INNER JOIN FETCH p.space s "
			+ "WHERE p.id = :personId")
	public Optional<Person> findByIdWithSpace(@Param("personId")Long id);
	
	/**
	 * email로 Person 찾기 <br>
	 * Space 페치 조인
	 * @param email 찾으려는 Person email
	 * @author kimgun-yeong
	 */
	@Query("SELECT p FROM Person p "
			+ "INNER JOIN FETCH p.space s "
			+ "WHERE p.email = :email")
	public Optional<Person> findByEmailWithSpace(@Param("email")String email);
	
	/**
	 * ID로 Person 찾기 <br>
	 * Space, Content들 페치 조인
	 * @param personId 찾으려는 Person ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT p FROM Person p "
			+ "INNER JOIN FETCH p.space s "
			+ "LEFT JOIN FETCH s.contents c "
			+ "WHERE p.id = :personId")
	public Optional<Person> findByIdWithSpaceAndContents(@Param("personId")Long personId);
	
	/**
	 * email로 Person 찾기 <br>
	 * Space, Content들 페치 조인
	 * @param email 찾으려는 Person의 email
	 * @author kimgun-yeong
	 */
	@Query("SELECT p FROM Person p "
			+ "INNER JOIN FETCH p.space s "
			+ "LEFT JOIN FETCH s.contents c "
			+ "WHERE p.email = :email")
	public Optional<Person> findByEmailWithSpaceAndContents(@Param("email")String email);
	
	/**
	 * parameter로 전달된 keyword 값을 포함하는 firstName 이나 lastName 가진 Person 객체들 가져오기 위한 쿼리
	 * @param keyword 검색하려는 firstName 이나 lastName
	 * @return 해당 검색 조건을 만족하는 Person 컬렉션
	 */
	@Query("SELECT p FROM Person p "
			+ "WHERE p.firstName LIKE %:keyword% "
			+ "OR p.lastName LIKE %:keyword% ")
	public Page<Person> findByNameWithKeyword(@Param("keyword") String keyword,Pageable pageable);
	
	/**
	 * 모든 Person 생성 최신순으로 정렬 반환
	 * @author kimgun-yeong
	 */
	public List<Person> findAllByOrderByCreatedAtDesc();
	
	/**
	 * 모든 Person 생성 오래된 순으로 정렬 반환
	 * @author kimgun-yeong
	 */
	public List<Person> findAllByOrderByCreatedAtAsc();
	
	/**
	 * 모든 Person 생성 최신순으로 정렬 페이지 반환 
	 * @author kimgun-yeong
	 */
	public Page<Person> findAllByOrderByCreatedAtDesc(Pageable pageable);
	
	/**
	 * 모든 Person 생성 오래된순으로 정렬 페이지 반환
	 * @author kimgun-yeong
	 */
	public Page<Person> findAllByOrderByCreatedAtAsc(Pageable pageable);
	
	/**
	 * 모든 Person 개수 반환
	 * @author kimgun-yeong
	 */
	public long count();
	
	/**
	 * parameter로 전달된 keyword 값을 포함하는 firstName 이나 lastName 가진 Person 개수를 반환하는 메소드
	 * @param keyword 검색하려는 firstName 이나 lastName
	 * @return
	 */
	@Query("SELECT COUNT(p) FROM Person p "
			+ "WHERE p.firstName LIKE %:keyword% "
			+ "OR p.lastName LIKE %:keyword% ")
	public long countWithNameKeyword(@Param("keyword") String keyword);
	
	/**
	 * email로 Person 존재여부 반환
	 * @param email 존재여부 확인하려는 Person의 email
	 * @author kimgun-yeong
	 */
	public boolean existsByEmail(String email);
}
