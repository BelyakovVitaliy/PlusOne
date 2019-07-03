package ru.project.plusone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.model.Tag;
import ru.project.plusone.repository.interfaces.TagRepository;
import ru.project.plusone.service.interfaces.TagService;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;

	@Autowired
	public TagServiceImpl(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	public Set<Tag> getAll() {
		return new HashSet<>(tagRepository.findAll());
	}

	@Override
	public Tag get(Long id) {
		return tagRepository.getById(id);
	}

	@Override
	public void add(Tag tag) {
		if(!tag.getTitle().substring(0,1).equals("#")) {
			tag.setTitle("#"+tag.getTitle());
		}
		tagRepository.saveAndFlush(tag);
	}

	@Override
	public void delete(Tag tag) {
		tagRepository.delete(tag);
	}

	@Override
	public void update(Tag tag) {
		tagRepository.saveAndFlush(tag);
	}

	@Override
	public void deleteById(Long id) {
		tagRepository.deleteById(id);
	}

	@Override
	public Tag getByTitle(String title) {
		return tagRepository.getByTitle(title);
	}
}
