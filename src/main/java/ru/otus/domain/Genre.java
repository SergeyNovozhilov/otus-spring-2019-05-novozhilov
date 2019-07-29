package ru.otus.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Data
@Table(name = "GENRES")
@Entity
public class Genre extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String name;
	@OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "book_id")
	private Collection<Book> book;
	public Genre() {
	}

	public Genre(String name) {
		this.name = name;
	}

	public Genre(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public void print() {
		System.out.println("Name: " + this.name);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Genre genre = (Genre) o;
		return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
	}

	@Override public int hashCode() {

		return Objects.hash(id, name);
	}
}
