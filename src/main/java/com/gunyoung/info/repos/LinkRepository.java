package com.gunyoung.info.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunyoung.info.domain.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

}
