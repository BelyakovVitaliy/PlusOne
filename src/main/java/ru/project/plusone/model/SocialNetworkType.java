package ru.project.plusone.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class SocialNetworkType implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column
	private Long id;

	@NotNull
	@Column(nullable = false, unique = true,length = 10)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "social_network_social_network_type",
			inverseJoinColumns = {@JoinColumn(name = "social_network_id", foreignKey = @ForeignKey(name = "FK_SOCIAL_NETWORK_SOCIAL_NETWORK_TYPE"))},
			joinColumns = {@JoinColumn(name = "social_network_type_id", foreignKey = @ForeignKey(name = "FK_SOCIAL_NETWORK"))})
	private List<SocialNetwork> socialNetworkList;

	@Column
	private String regExp;

	public String getPlaceHolder() {
		return placeHolder;
	}

	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	@Column
	private String placeHolder;

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	public SocialNetworkType() {
	}

	public SocialNetworkType(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public List<SocialNetwork> getSocialNetworkList() {
		return socialNetworkList;
	}

	public void setSocialNetworkList(List<SocialNetwork> socialNetworkList) {
		this.socialNetworkList = socialNetworkList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SocialNetworkType)) return false;

		SocialNetworkType that = (SocialNetworkType) o;

		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (socialNetworkList != null ? socialNetworkList.hashCode() : 0);
		return result;
	}
}
