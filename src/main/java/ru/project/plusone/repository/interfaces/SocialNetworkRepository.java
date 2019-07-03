package ru.project.plusone.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.project.plusone.model.SocialNetwork;

import java.util.List;

public interface SocialNetworkRepository extends JpaRepository<SocialNetwork, Long> {
	@Query(value = "SELECT link\n" +
			"FROM social_network_social_network_type a\n" +
			"  INNER JOIN (SELECT a.event_id,b.snId, b.link FROM (SELECT event_id, c.id AS \"userId\"\n" +
			"                                                     FROM event_joined_users a\n" +
			"                                                       INNER JOIN event b ON b.id=a.event_id\n" +
			"                                                       INNER JOIN user c ON c.id =a.joined_users_id) a, (SELECT c.id AS \"userId\", b.id AS \"snId\" ,b.link\n" +
			"                                                                                                         FROM user_social_networks a\n" +
			"                                                                                                           INNER JOIN social_network b ON b.id=a.social_networks_id\n" +
			"                                                                                                           INNER JOIN user c ON c.id =a.user_id) b WHERE a.userId=b.userId) b ON b.snId=a.social_network_id\n" +
			"  INNER JOIN social_network_type c ON c.id =a.social_network_type_id WHERE name='VK' AND event_id=?1", nativeQuery = true)
	List<String> getVKLinkFromEventId(Long eventId);

	@Query(value = "SELECT a.link FROM (SELECT  b.id, b.first_name, c.link\n" +
			"               FROM user_social_networks a\n" +
			"                 INNER JOIN user b ON b.id=a.user_id\n" +
			"                 INNER JOIN social_network c ON c.id =a.social_networks_id) a, (SELECT  b.id, b.link, c.name\n" +
			"                                                                                FROM social_network_social_network_type a\n" +
			"                                                                                  INNER JOIN social_network b ON b.id=a.social_network_id\n" +
			"                                                                                  INNER JOIN social_network_type c ON c.id =a.social_network_type_id) b\n" +
			"WHERE a.link = b.link AND b.name='VK' AND a.id=?1", nativeQuery = true)
	String getVKLinkByUserId(Long id);
	SocialNetwork getById(Long id);
}
