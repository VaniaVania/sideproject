package com.project.sideproject.models;
import javax.persistence.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title, anons, full_text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_images_link", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> images_link;

    @JoinColumn(name = "post_data")
    private Date post_data;

    public Post() {
    }

    public Post(String title, String anons, String full_text, List<String> images_link, Date post_data) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.images_link = images_link;
        this.post_data = post_data;
    }

    public Post(String title, String anons, String full_text) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
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

    public List<String> getImages_link() {
        return images_link;
    }

    public void setImages_link(List<String> images_link) {
        this.images_link = images_link;
    }

    public Date getPost_data() {
        return post_data;
    }

    public void setPost_data(Date post_data) {
        this.post_data = post_data;
    }


    @Override
    public String toString() {
        return "Posted: " + post_data.getDay() +
                "." + post_data.getMonth() +
                "." + post_data.getYear() +
                "in " + post_data.getHours() +
                ":" + post_data.getMinutes();
    }




}
