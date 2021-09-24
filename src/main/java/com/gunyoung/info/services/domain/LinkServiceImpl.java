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
		return result.orElse(null);
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
	public List<Link> updateLinksForContent(Content content, Iterable<LinkDTO> linkDTOs) {
		List<Link> linksForSave = new ArrayList<>();
		Map<Long, LinkDTO> linkDTOMapForModifyingLink = getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(linkDTOs, linksForSave, content);
		
		List<Link> existContentLinks = content.getLinks();
		Map<Long, Link> existLinkMap = getIdAndLinkMapForExistLinks(existContentLinks);
		modifyOrDeleteExistLinks(existLinkMap, linkDTOMapForModifyingLink, linksForSave);
		
		return saveAll(linksForSave);
	}
	
	private Map<Long, LinkDTO> getLinkDTOMapForModifyingLinkAndAddNewContentLinkToSaveLinkList(Iterable<LinkDTO> linkDTOs, List<Link> linksForSave, Content content) {
		Map<Long, LinkDTO> linkDTOMap = new HashMap<>();
		for(LinkDTO linkDTO : linkDTOs) {
			Long linkDTOId = linkDTO.getId();
			if(linkDTOId == null) {
				Link newLink = createLinkFromLinkDTOAndContent(linkDTO, content);
				linksForSave.add(newLink);
			} else {
				linkDTOMap.put(linkDTOId, linkDTO);
			}
		}
		
		return linkDTOMap;
	}
	
	private Link createLinkFromLinkDTOAndContent(LinkDTO linkDTO, Content content) {
		Link newLink = Link.builder()
				.tag(linkDTO.getTag())
				.url(linkDTO.getUrl())
				.content(content)
				.build();
		return newLink;
	}
	
	private Map<Long, Link> getIdAndLinkMapForExistLinks(Iterable<Link> existContentLinks) {
		Map<Long, Link> existLinkMap = new HashMap<>();
		for(Link existLink : existContentLinks) {
			existLinkMap.put(existLink.getId(), existLink);
		}
		return existLinkMap; 
	}
	
	private void modifyOrDeleteExistLinks(Map<Long, Link> existLinkMap, Map<Long, LinkDTO> linkDTOMapForModifyingLink, List<Link> linksForSave) {
		Set<Long> existLinkIds = existLinkMap.keySet();
		for(Long existLinkId: existLinkIds) {
			Link existLink = existLinkMap.get(existLinkId);
			if(linkDTOMapForModifyingLink.containsKey(existLinkId)) {
				LinkDTO linkDTO = linkDTOMapForModifyingLink.get(existLinkId);
				modifyExistLinkByLinkDTO(existLink, linkDTO);
				linksForSave.add(existLink);
			} else {
				delete(existLink);
			}
		}
	}
	
	private void modifyExistLinkByLinkDTO(Link existLink, LinkDTO linkDTO) {
		existLink.setTag(linkDTO.getTag());
		existLink.setUrl(linkDTO.getUrl());
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
