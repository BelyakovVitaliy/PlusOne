package ru.project.plusone.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class KudaGoEvent implements Serializable {

	@NotNull
	private String title;

	private Set<Tag> tags;

	private String picture;

	@NotNull
	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private KudaGoLocation destination;


	private String description;

	public KudaGoEvent() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof KudaGoEvent)) return false;
		KudaGoEvent that = (KudaGoEvent) o;
		return Objects.equals(title, that.title) &&
				Objects.equals(startDate, that.startDate) &&
				Objects.equals(endDate, that.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, startDate, endDate);
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

	public KudaGoLocation getDestination() {
		return destination;
	}

	public void setDestination(KudaGoLocation destination) {
		this.destination = destination;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
