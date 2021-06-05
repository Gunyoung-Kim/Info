package com.gunyoung.info.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Space;

public interface SpaceRepository extends JpaRepository<Space,Long>{
	public boolean existsById(Long id);
}
