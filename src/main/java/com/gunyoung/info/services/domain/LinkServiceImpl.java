package com.gunyoung.info.services.domain;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Link;
import com.gunyoung.info.repos.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service("linkService")
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{
	
	private final LinkRepository linkRepository;

	@Override
	public List<Link> saveAll(Iterable<Link> links) {
		return linkRepository.saveAll(links);
	}
}
