package com.gunyoung.info.services;

import com.gunyoung.info.domain.Content;

public interface ContentService {
	public Content save(Content content);
	public Content findById(Long id);
	public void deleteContent(Content content);
	public void deleteContentById(Long id);
}
