package com.gunyoung.info.services;

import java.util.List;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Space;

public interface SpaceService {
	public List<Space> getAll();
	public Space findById(Long id);
	public Space save(Space space);
	public boolean existsById(Long id);
	public void addContent(Space space, Content content);
}
