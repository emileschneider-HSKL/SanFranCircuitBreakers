package com.hskl.sanfrancircuitbreakers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adress")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Adress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    @OneToMany(mappedBy = "adress")
    @JsonIgnore
    private List<User> users = new ArrayList<>();
}
