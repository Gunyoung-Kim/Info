package com.gunyoung.info.services.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.repos.ContentRepository;

import lombok.RequiredArgsConstructor;

@Service("contentService")
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService{

	private final ContentRepository contentRepository;
	
	@Override
	@Transactional(readOnly= true)
	public Content findById(Long id) {
		 Optional<Content> result = contentRepository.findById(id);
		 if(!result.isPresent()) 
			 return null;
		 return result.get();
	}
	
	@Override
	public Content save(Content content) {
		return contentRepository.save(content);
	}

	@Override
	public void delete(Content content) {
		contentRepository.delete(content);
	}

	@Override
	public void deleteById(Long id) {
		Content content = findById(id);
		if(content != null)
			contentRepository.delete(content);
	}

	@Override
	@Transactional(readOnly=true)
	public long countAll() {
		return contentRepository.count();
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsById(Long id) {
		return contentRepository.existsById(id);
	}
	
}
