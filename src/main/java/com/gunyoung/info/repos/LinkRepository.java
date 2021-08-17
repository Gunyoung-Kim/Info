package com.gunyoung.info.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.info.domain.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
	
	/**
	 * Content ID로 Link들 찾기
	 * @param contentId 찾으려는 Link들의 Content Id
	 * @author kimgun-yeong
	 */
	@Query("SELECT l FROM Link l "
			+ "INNER JOIN l.content c "
			+ "WHERE c.id = :contentId")
	public List<Link> findAllByContentId(@Param("contentId") Long contentId);
}
