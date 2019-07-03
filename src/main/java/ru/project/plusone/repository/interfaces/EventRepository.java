package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.plusone.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	Event getById(Long id);
}
