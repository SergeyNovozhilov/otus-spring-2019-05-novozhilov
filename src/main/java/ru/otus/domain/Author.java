package ru.otus.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Data
@Table(name = "AUTHORS")
@Entity
public class Author extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "authors")
    private Collection<Book> books;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        Set<String> genres = new HashSet<>();
        System.out.println(" Name: " + this.name);
        System.out.println("Books:");
        if (this.books != null && !this.books.isEmpty()) {
            for (Book book : this.books) {
                genres.add(book.getGenre().getName());
                System.out.println(" Title: " + book.getTitle());
                System.out.println("  Genre: " + Optional.ofNullable(book.getGenre()).orElse(new Genre()).getName());
            }
        }
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

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }
}
