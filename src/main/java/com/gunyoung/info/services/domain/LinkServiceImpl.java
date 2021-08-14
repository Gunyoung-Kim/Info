package com.gunyoung.info.services.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.repos.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service("linkService")
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{
	
	private final LinkRepository linkRepository;
}
