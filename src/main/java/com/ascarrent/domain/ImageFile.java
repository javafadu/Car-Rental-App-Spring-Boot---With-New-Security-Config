package com.ascarrent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_imagefile")
public class ImageFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;

    private String name;
    private String type;
    private long length;

    @OneToOne(cascade = CascadeType.ALL)
    // if we want to delete imageFile, system will delete also imageData
    private ImageData imageData;


    // to set length (from imageData), we need to create a new constructor
    public ImageFile(String name, String type, ImageData imageData) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
        this.length = imageData.getData().length;
    }

}
