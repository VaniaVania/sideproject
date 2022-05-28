package com.project.sideproject.models;

import javax.persistence.*;

@Entity(name = "postImage")
@Table(name = "post_image")
public class Image {


    @Column(name = "images_id")
    private Long images_id;


    @Column(name = "image_link")
    private String imageLink;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "post_id")
    private Long post_id;

    public Image() {
    }


    public Image(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getImages_id() {
        return images_id;
    }

    public void setImages_id(Long images_id) {
        this.images_id = images_id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }
}
