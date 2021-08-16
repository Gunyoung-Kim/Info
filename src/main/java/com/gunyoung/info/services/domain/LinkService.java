package com.gunyoung.info.services.domain;

import java.util.List;

import com.gunyoung.info.domain.Link;

public interface LinkService {
	
	/**
	 * 모든 Link들 저장
	 * @author kimgun-yeong
	 */
	public List<Link> saveAll(Iterable<Link> links);
}
