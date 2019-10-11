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

    public Authority() {}

    public Authority(AuthorityType name) {
        this.name = name;
    }
}
