package com.gunyoung.info.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gunyoung.info.domain.Space;

public interface SpaceRepository extends JpaRepository<Space,Long> {
	
	/**
	 * Person Id로 Space 찾기
	 * @param personId 찾으려는 Space의 Person ID
	 * @author kimgun-yeong
	 */
	@Query("SELECT s FROM Space s "
			+ "INNER JOIN s.person p "
			+ "WHERE p.id = :personId")
	public Optional<Space> findByPersonIdInQuery(@Param("personId") Long personId);
	
	/**
	 * ID로 Space 삭제하기
	 * @param spaceId 삭제하려는 Space ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Space s "
			+ "WHERE s.id = :spaceId")
	public void deleteByIdInQuery(@Param("spaceId") Long spaceId);
	
	/**
	 * Person Id 로 Space 삭제
	 * @param personId 삭제하려는 Space의 Person ID
	 * @author kimgun-yeong
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE FROM Space s "
			+ "WHERE s.person.id = :personId")
	public void deleteByPersonIdInQuery(@Param("personId") Long personId);
	
	/**
	 * ID로 Space 존재 여부 반환
	 * @author kimgun-yeong
	 */
	public boolean existsById(Long id);
}
