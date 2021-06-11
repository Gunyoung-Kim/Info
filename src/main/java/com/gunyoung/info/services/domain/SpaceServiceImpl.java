package com.gunyoung.info.services.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Space;
import com.gunyoung.info.repos.SpaceRepository;

@Service("spaceService")
@Transactional
public class SpaceServiceImpl implements SpaceService {
	
	@Autowired
	SpaceRepository spaceRepository;
	
	@Autowired 
	ContentService contentService;

	@Override
	@Transactional(readOnly=true)
	public List<Space> getAll() {
		return spaceRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Space findById(Long id) {
		return spaceRepository.getById(id);
	}

	@Override
	public Space save(Space space) {
		return spaceRepository.save(space);
	}

	@Override
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
