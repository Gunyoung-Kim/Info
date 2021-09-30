package com.gunyoung.info.services.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.SpaceRepository;

import lombok.RequiredArgsConstructor;

@Service("spaceService")
@Transactional
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {
	
	private final SpaceRepository spaceRepository;
	
	private final ContentService contentService;
	
	@Override
	@Transactional(readOnly=true)
	public Space findById(Long id) {
		Optional<Space> result = spaceRepository.findById(id);
		return result.orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Space> findAll() {
		return spaceRepository.findAll();
	}

	@Override
	public Space save(Space space) {
		return spaceRepository.save(space);
	}
	
	@Override
	public void delete(Space space) {
		Objects.requireNonNull(space);
		Long spaceId = space.getId();
		contentService.deleteAllBySpaceId(spaceId);
		spaceRepository.deleteByIdInQuery(spaceId);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean existsById(Long id) {
		return spaceRepository.existsById(id);
	}

	@Override
	public void addContent(Space space, Content content) {
		content.setSpace(space);
		contentService.save(content);
		
		space.getContents().add(content);
	}
}
