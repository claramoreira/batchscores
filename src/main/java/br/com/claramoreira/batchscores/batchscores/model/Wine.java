package br.com.claramoreira.batchscores.batchscores.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Wine {

	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;

	@JsonProperty("image")
	private String image;

	@JsonProperty("id")
	private Long id;

	public Wine() {

	}

	public Wine(String title, String description, String image, Long id) {
		super();
		this.title = title;
		this.description = description;
		this.image = image;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wine other = (Wine) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Wine [title=" + title + ", description=" + description + ", image=" + image + ", id=" + id + "]";
	}

}
