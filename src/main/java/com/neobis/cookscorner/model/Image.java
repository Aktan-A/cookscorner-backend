package com.neobis.cookscorner.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Image extends BaseEntity {

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "remote_id", nullable = false)
    private String remoteId;

    public Image(String imageUrl, String remoteId) {
        this.imageUrl = imageUrl;
        this.remoteId = remoteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(imageUrl, image.imageUrl) && Objects.equals(remoteId, image.remoteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl, remoteId);
    }
}
