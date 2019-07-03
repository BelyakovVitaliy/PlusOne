package ru.project.plusone.model;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
public class SocialNetwork implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private long id;

	@Column
	private String link;

	@ManyToOne
	private SocialNetworkType socialNetworkType;

	public SocialNetwork() {
	}

	public SocialNetwork(String link, SocialNetworkType socialNetworkType) {
		this.link = link;
		this.socialNetworkType = socialNetworkType;
	}

	@Override
	public String toString() {
		return this.link;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public SocialNetworkType getSocialNetworkType() {
		return socialNetworkType;
	}

	public void setSocialNetworkType(SocialNetworkType socialNetworkType) {
		this.socialNetworkType = socialNetworkType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SocialNetwork)) return false;

		SocialNetwork that = (SocialNetwork) o;

		return link.equals(that.link);
	}

	@Override
	public int hashCode() {
		return link.hashCode();
	}
}
