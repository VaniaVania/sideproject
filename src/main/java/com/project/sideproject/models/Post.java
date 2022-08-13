package com.project.sideproject.models;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title, anons, fullText;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_images_link", joinColumns = @JoinColumn(name = "post_id"))
    private List<String> images;

    @JoinColumn(name = "post_data")
    private Date data;

    public Post() {
    }

    public Post(String title, String anons, String fullText, List<String> images, Date data) {
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.images = images;
        this.data = data;
    }

    public Post(String title, String anons, String fullText) {
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String full_text) {
        this.fullText = full_text;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images_link) {
        this.images = images_link;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date post_data) {
        this.data = post_data;
    }


    @Override
    public String toString() {
        return "Posted: " + data.getDay() +
                "." + data.getMonth() +
                "." + data.getYear() +
                "in " + data.getHours() +
                ":" + data.getMinutes();
    }




}
