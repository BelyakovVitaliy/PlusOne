package ru.project.plusone.service.interfaces;

import ru.project.plusone.model.User;

import java.util.Set;

public interface UserService {
	User getLocalUserByEmail(String email);

	Set<User> getAll();

	User get(Long id);

	User getUserBySocialId(String id);

	void addLocalUser(User e);

	void addSocialUser(User e);

	void delete(User e);

	void updateLocalUser(User e);

	void updateSocialUser(User e);

	void deleteById(Long id);

	boolean isRequiredFieldsValid (User user);

	User getUserByEmail(String email);


}
