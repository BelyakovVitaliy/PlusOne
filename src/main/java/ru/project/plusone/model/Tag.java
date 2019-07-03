package ru.project.plusone.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Tag implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@NotNull
	@Column(nullable = false, unique = true, length = 10)
	private String title;

	public Tag() {
	}

	public Tag(String title) {
		this.title = title;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tag)) return false;

		Tag tag = (Tag) o;

		return title.equals(tag.title);
	}

	@Override
	public int hashCode() {
		return title.hashCode();
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
}
