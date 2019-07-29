package ru.otus.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = {"authors", "comments"})
@Table(name = "BOOKS")
@Entity
public class Book extends Base{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String title;
	@ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
	private Collection<Author> authors;
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
	private Genre genre;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "comment_id")
	private Collection<Comment> comments;
	public Book() {
	}

	public Book(String title) {
		this.title = title;
	}

	public void addAuthor(Author author) {
	    if (this.authors == null) {
	        this.authors = new HashSet<>();
        }
	    this.authors.add(author);
    }

	public void addAuthors(Collection<Author> authors) {
		if (this.authors == null) {
			this.authors = new HashSet<>();
		}
		this.authors.addAll(authors);
	}

	@Override
	public void print() {
		System.out.println(" Title: " + this.title);
		System.out.println(" Genre: " + this.genre.getName());
		System.out.println("Authors:");
		if (this.authors != null && !this.authors.isEmpty()) {
			for (Author author : this.authors) {
				System.out.println("   " + author.getName());
			}
		}
		System.out.println("Comments:");
		if (this.comments != null && !this.comments.isEmpty()) {
			for (Comment comment : this.comments) {
				System.out.println("   " + comment.getComment());
			}
		}
	}

	public void addComment(Comment comment) {
		if (this.comments == null) {
			this.comments = new HashSet<>();
		}
		this.comments.add(comment);
	}

	public UUID getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Collection<Author> authors) {
		this.authors = authors;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Book book = (Book) o;
		if (!Objects.equals(id, book.id)) {
			return false;
		}
		if (!Objects.equals(title, book.title)) {
			return false;
		}
		if (!Objects.equals(genre, book.genre)) {
			return false;
		}

		if (authors != null && book.getAuthors() != null) {
			if(authors.size() != book.authors.size()) {
				return false;
			}

			for (Author e : authors) {
				Author a = book.authors.stream().filter(x -> x.getId().equals(e.getId())).findFirst().orElse(null);
				if (a == null) {
					return false;
				}
				if (!StringUtils.equalsIgnoreCase(e.getName(), a.getName())) {
					return false;
				}
			}
			return true;
		} else if (authors == null && book.getAuthors() == null){
			return true;
		}

		return false;
	}

	@Override public int hashCode() {

		return Objects.hash(id, title, authors, genre);
	}
}
