package com.gunyoung.info.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.repos.ContentRepository;

@Service("contentService")
@Transactional
public class ContentServiceImpl implements ContentService{

	@Autowired
	ContentRepository contentRepository;
	
	@Override
	public Content save(Content content) {
		return contentRepository.save(content);
	}
	
}
