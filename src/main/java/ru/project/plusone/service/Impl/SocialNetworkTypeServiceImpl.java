package ru.project.plusone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.model.SocialNetworkType;
import ru.project.plusone.repository.interfaces.SocialNetworkTypeRepository;
import ru.project.plusone.service.interfaces.SocialNetworkTypeService;

import java.util.HashSet;
import java.util.Set;

@Service
public class SocialNetworkTypeServiceImpl implements SocialNetworkTypeService {

	private final SocialNetworkTypeRepository socialNetworkTypeRepository;

	@Autowired
	public SocialNetworkTypeServiceImpl(SocialNetworkTypeRepository socialNetworkTypeRepository) {
		this.socialNetworkTypeRepository = socialNetworkTypeRepository;
	}

	@Override
	public Set<SocialNetworkType> getAll() {
		return new HashSet<>(socialNetworkTypeRepository.findAll());
	}

	@Override
	public SocialNetworkType get(Long id) {
		return socialNetworkTypeRepository.getById(id);
	}

	@Override
	public void add(SocialNetworkType e) {
		socialNetworkTypeRepository.saveAndFlush(e);
	}

	@Override
	public void delete(SocialNetworkType e) {
		socialNetworkTypeRepository.delete(e);
	}

	@Override
	public void update(SocialNetworkType e) {
		socialNetworkTypeRepository.saveAndFlush(e);
	}

	@Override
	public void deleteById(Long id) {
		socialNetworkTypeRepository.deleteById(id);
	}

	@Override
	public SocialNetworkType getByName(String name) {
		return socialNetworkTypeRepository.getByName(name);
	}
}
