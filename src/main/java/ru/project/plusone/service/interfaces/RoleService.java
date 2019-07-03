package ru.project.plusone.service.interfaces;

import ru.project.plusone.model.Role;

public interface RoleService extends GeneralService<Role> {
	Role getByAuthority(String title);
}
