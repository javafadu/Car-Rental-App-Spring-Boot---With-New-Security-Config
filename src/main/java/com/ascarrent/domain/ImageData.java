package com.ascarrent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_imagedata")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // large object
    private byte[] data;

    public ImageData(byte[] data) {
        this.data=data;
    }

    public ImageData(Long id) {
        this.id=id;
    }

}
