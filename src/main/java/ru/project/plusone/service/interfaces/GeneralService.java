package ru.project.plusone.service.interfaces;

import java.util.Set;

public interface GeneralService<T> {
	Set<T> getAll();

	T get(Long id);

	void add(T e);

	void delete(T e);

	void update(T e);

	void deleteById(Long id);
}
