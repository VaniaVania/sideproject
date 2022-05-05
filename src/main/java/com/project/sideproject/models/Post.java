package com.project.sideproject.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title, anons, full_text;
    private String fileLink;


    public Post(Long id, String title, String anons, String full_text, String fileLink) {
        this.id = id;
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.fileLink = fileLink;
    }

    public Post() {
    }

    public Post(String title, String anons, String full_text, String fileLink) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.fileLink = fileLink;
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}
