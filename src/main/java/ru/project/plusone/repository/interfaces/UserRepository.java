package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.plusone.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User getUserByisFromSocialFalseAndEmail(String email);
	User getUserBySocialId(String socialId);
	User getById(Long id);
	User getUserByEmail(String email);
}
