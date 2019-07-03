package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.plusone.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Tag getById(long id);
	Tag getByTitle(String title);
}
