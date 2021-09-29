package com.gunyoung.info.services.domain;

import java.util.List;
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
	
	private final LinkService linkService;
	
	@Override
	@Transactional(readOnly= true)
	public Content findById(Long id) {
		 Optional<Content> result = contentRepository.findById(id);
		 return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Content findByIdWithSpaceAndPerson(Long id) {
		Optional<Content> result = contentRepository.findByIdWithSpaceAndPerson(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Content findByIdWithLinks(Long id) {
		Optional<Content> result = contentRepository.findByIdWithLinks(id);
		return result.orElse(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Content> findAllBySpaceIdWithLinks(Long spaceId) {
		return contentRepository.findAllBySpaceIdWithLinks(spaceId);
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
	public void deleteAllBySpaceId(Long spaceId) {
		List<Content> contentsForSpace = contentRepository.findAllBySpaceIdInQuery(spaceId);
		deleteAllLinksForSpaceContents(contentsForSpace);
		contentRepository.deleteAllBySpaceIdInQuery(spaceId);
	}
	
	private void deleteAllLinksForSpaceContents(Iterable<Content> contentsForSpace) {
		for(Content content: contentsForSpace) {
			deleteAllLinksForContent(content);
		}
	}
	
	private void deleteAllLinksForContent(Content content) {
		Long contentId = content.getId();
		linkService.deleteAllByContentId(contentId);
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
