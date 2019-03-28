package com.andrei.examapp.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Intent;

import java.io.Serializable;

@Entity(tableName = "Entities")
public class GenericEntity implements Serializable {
    @PrimaryKey
    private Integer id;
    private String title;
    private String description;
    private String album;
    private String genre;
    private Integer year;

    public GenericEntity(Integer id, String title, String description, String album, String genre, Integer year) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.album = album;
        this.genre = genre;
        this.year = year;
    }

    @Ignore
    public GenericEntity(String title, String description, String album, String genre, Integer year) {
        this.title = title;
        this.description = description;
        this.album = album;
        this.genre = genre;
        this.year = year;
    }

    @Ignore
    public GenericEntity() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "GenericEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                '}';
    }
}
