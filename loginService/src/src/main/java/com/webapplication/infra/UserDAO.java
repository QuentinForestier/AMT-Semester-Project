package com.webapplication.infra;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

// Il est déconseillé d'utiliser l'annotation de lombok @Data sur des entite JPA :
//      https://dzone.com/articles/lombok-and-jpa-what-may-go-wrong

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", unique = true)
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;
}