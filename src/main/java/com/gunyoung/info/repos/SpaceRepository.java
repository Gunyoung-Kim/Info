package com.gunyoung.info.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Space;

public interface SpaceRepository extends JpaRepository<Space,Long>{
	
	/**
	 * ID로 Space 존재 여부 반환
	 * @author kimgun-yeong
	 */
	public boolean existsById(Long id);
}
