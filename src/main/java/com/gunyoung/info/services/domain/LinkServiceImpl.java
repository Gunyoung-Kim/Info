package com.gunyoung.info.services.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunyoung.info.domain.Content;
import com.gunyoung.info.domain.Link;
import com.gunyoung.info.dto.LinkDTO;
import com.gunyoung.info.repos.LinkRepository;

import lombok.RequiredArgsConstructor;

@Service("linkService")
@Transactional
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{
	
	private final LinkRepository linkRepository;
	
	@Override
	@Transactional(readOnly=true)
	public Link findById(Long id) {
		Optional<Link> result = linkRepository.findById(id);
		if(!result.isPresent()) {
			return null;
		}
		return result.get();
	}

	@Override
	@Transactional(readOnly=true)
	public List<Link> findAllByContentId(Long contentId) {
		return linkRepository.findAllByContentId(contentId);
	}
	
	@Override
	public Link save(Link link) {
		return linkRepository.save(link);
	}
	
	@Override
	public List<Link> saveByLinkDTOsAndExistContentLinks(Content content,Iterable<LinkDTO> linkDTOs, Iterable<Link> existContentLinks) {
		List<Link> saveLinkList = new ArrayList<>();
		Map<Long, Link> existLinkMap = new HashMap<>();
		for(Link existLink : existContentLinks) {
			existLinkMap.put(existLink.getId(), existLink);
		}
		
		Map<Long, LinkDTO> linkDTOMap = new HashMap<>();
		for(LinkDTO linkDTO : linkDTOs) {
			Long linkDTOId = linkDTO.getId();
			
			if(linkDTOId == null) {
				Link newLink = Link.builder()
						.tag(linkDTO.getTag())
						.url(linkDTO.getUrl())
						.content(content)
						.build();
				
				saveLinkList.add(newLink);
			} else {
				linkDTOMap.put(linkDTOId, linkDTO);
			}
		}
		
		// 기존의 Link들 삭제 또는 업데이트
		Set<Long> existLinkIds = existLinkMap.keySet();
		for(Long existLinkId: existLinkIds) {
			Link existLink = existLinkMap.get(existLinkId);
			if(!linkDTOMap.containsKey(existLinkId)) {
				delete(existLink);
			} else {
				LinkDTO linkDTO = linkDTOMap.get(existLinkId);
				existLink.setTag(linkDTO.getTag());
				existLink.setUrl(linkDTO.getUrl());
				saveLinkList.add(existLink);
			}
		}
		
		
		return saveAll(saveLinkList);
	}
	
	@Override
	public List<Link> saveAll(Iterable<Link> links) {
		return linkRepository.saveAll(links);
	}

	@Override
	public void delete(Link link) {
		linkRepository.delete(link);
	}

	@Override
	public void deleteById(Long id) {
		Link link = findById(id);
		if(link != null)
			delete(link);
	}
}
