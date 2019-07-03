package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.plusone.model.SocialNetworkType;

public interface SocialNetworkTypeRepository extends JpaRepository<SocialNetworkType, Long> {
	SocialNetworkType getById(Long id);
	SocialNetworkType getByName(String name);
}
