package com.guitarshop.model;
import jakarta.persistence.*;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String name;
}
