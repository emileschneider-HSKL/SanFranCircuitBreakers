package com.hskl.sanfrancircuitbreakers.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "adress_id")
    private Adress adress;
}
