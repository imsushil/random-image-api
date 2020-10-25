package com.zetatest.randomimage.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String imageId;
    private String url;

    public Image(String imageId, String url) {
        this.imageId = imageId;
        this.url = url;
    }
}
