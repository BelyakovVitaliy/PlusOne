package ru.project.plusone.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Event implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false, length = 50)
	private String title;

	@ManyToMany
	private Set<Tag> tags;

	@NotNull
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(nullable = false)
	private User ownerUser;

	@ManyToMany
	private Set<User> joinedUsers;

	@Column
	private String picture;

	@NotNull
	@Column(nullable = false)
	private LocalDateTime startDate;

	@Column
	private LocalDateTime endDate;

	@Embedded
	private Location destination;

	@Column(length = 150)
	private String description;

	@Column
	private boolean isChatEnabled = false;

	@Column
	private Integer chatId = 0;

	public Event() {

	}

	public Event(String title, LocalDateTime startDate) {
		this.title = title;
		this.startDate = startDate;
	}

	public boolean isChatEnabled() {
		return isChatEnabled;
	}

	public void setChatEnabled(boolean chatEnabled) {
		isChatEnabled = chatEnabled;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public Set<User> getJoinedUsers() {
		return joinedUsers;
	}

	public void setJoinedUsers(Set<User> joinedUsers) {
		this.joinedUsers = joinedUsers;
	}

	public User getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(User ownerUser) {
		this.ownerUser = ownerUser;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Event)) return false;
		Event event = (Event) o;
		return (id != null ? id.equals(event.id) : event.id == null) && title.equals(event.title);
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + title.hashCode();
		return result;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

}
