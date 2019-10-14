package ru.otus.domain;

import lombok.Data;
import ru.otus.security.AuthorityType;

import javax.persistence.*;
import java.util.UUID;

@Data
@Table(name = "AUTHORITIES")
@Entity
public class Authority extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AuthorityType name;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    private User user;

    public Authority() {}

    public Authority(AuthorityType name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AuthorityType getName() {
        return name;
    }

    public void setName(AuthorityType name) {
        this.name = name;
    }
}
