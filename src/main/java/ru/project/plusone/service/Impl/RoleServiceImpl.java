package ru.project.plusone.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.plusone.model.Role;
import ru.project.plusone.repository.interfaces.RoleRepository;
import ru.project.plusone.service.interfaces.RoleService;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {


	private final RoleRepository roleRepository;

	@Autowired
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Set<Role> getAll() {
		return new HashSet<>(roleRepository.findAll());
	}

	@Override
	public Role getByAuthority(String title) {
		return roleRepository.getByAuthority(title);
	}

	@Override
	public Role get(Long id) {
		return roleRepository.getById(id);
	}

	@Override
	public void add(Role role) {
		if (roleRepository.getByAuthority(role.getAuthority()) == null) {
			roleRepository.saveAndFlush(role);
		}
	}

	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}

	@Override
	public void update(Role role) {
		roleRepository.saveAndFlush(role);
	}

	@Override
	public void deleteById(Long id) {
		roleRepository.deleteById(id);
	}
}
