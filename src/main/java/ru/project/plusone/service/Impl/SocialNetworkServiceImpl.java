package ru.project.plusone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.model.SocialNetwork;
import ru.project.plusone.repository.interfaces.SocialNetworkRepository;
import ru.project.plusone.service.interfaces.SocialNetworkService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SocialNetworkServiceImpl implements SocialNetworkService {
	@Autowired
	SocialNetworkRepository socialNetworkRepository;

	@Override
	public String getVKLinkByUserId(Long id) {
		return socialNetworkRepository.getVKLinkByUserId(id);
	}

	@Override
	public List<String> getVKLinkFromEventId(Long eventId) {
		return socialNetworkRepository.getVKLinkFromEventId(eventId);
	}

	@Override
	public Set<SocialNetwork> getAll() {
		return new HashSet<>(socialNetworkRepository.findAll());
	}

	@Override
	public SocialNetwork get(Long id) {
		return socialNetworkRepository.getById(id);
	}

	@Override
	public void add(SocialNetwork e) {
		socialNetworkRepository.saveAndFlush(e);
	}

	@Override
	public void delete(SocialNetwork e) {
		socialNetworkRepository.delete(e);
	}

	@Override
	public void update(SocialNetwork e) {
		socialNetworkRepository.saveAndFlush(e);
	}

	@Override
	public void deleteById(Long id) {
		socialNetworkRepository.deleteById(id);
	}
}
