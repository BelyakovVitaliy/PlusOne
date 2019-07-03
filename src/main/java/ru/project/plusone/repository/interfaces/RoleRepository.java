package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.plusone.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role getByAuthority(String authority);

	Role getById(Long id);
}
