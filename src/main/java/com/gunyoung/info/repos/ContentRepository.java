package com.gunyoung.info.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.info.domain.Content;

public interface ContentRepository extends JpaRepository<Content,Long>{
	
	/**
	 * ID로 Content 찾기 <br>
	 * Space, Person 페치 조인
	 * @param contentId 찾으려는 Content의 ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT c FROM Content c "
			+ "INNER JOIN FETCH c.space s "
			+ "INNER JOIN FETCH s.person p "
			+ "WHERE c.id = :contentId")
	public Optional<Content> findByIdWithSpaceAndPerson(@Param("contentId") Long contentId);
	
	public long count();
	public boolean existsById(Long id);
}
