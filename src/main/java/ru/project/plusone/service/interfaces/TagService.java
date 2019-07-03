package ru.project.plusone.service.interfaces;

import ru.project.plusone.model.Tag;

public interface TagService extends GeneralService<Tag> {
	Tag getByTitle(String title);
}
