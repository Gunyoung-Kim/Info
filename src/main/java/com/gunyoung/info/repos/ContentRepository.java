package com.gunyoung.info.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Content;

public interface ContentRepository extends JpaRepository<Content,Long>{
	public long count();
	public boolean existsById(Long id);
}
