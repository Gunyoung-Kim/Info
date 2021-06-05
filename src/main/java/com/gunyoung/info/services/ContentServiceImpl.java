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

	@Override
	@Transactional(readOnly= true)
	public Content findById(Long id) {
		return contentRepository.getById(id);
	}

	@Override
	public void deleteContent(Content content) {
		Long id = content.getId();
		if(id == null) 
			return;
		if(!contentRepository.existsById(id)) {
			return;
		}
		contentRepository.delete(content);
		content.getSpace().getContents().remove(content);
	}

	@Override
	public void deleteContentById(Long id) {
		if(!contentRepository.existsById(id)) {
			return;
		}
		Content content = contentRepository.getById(id);
		content.getSpace().getContents().remove(content);
		contentRepository.delete(content);
		
	}

	@Override
	public long countAll() {
		return contentRepository.count();
	}

	@Override
	public boolean existsById(Long id) {
		return contentRepository.existsById(id);
	}
	
}
