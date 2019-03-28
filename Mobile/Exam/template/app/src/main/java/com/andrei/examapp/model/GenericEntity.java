package com.andrei.examapp.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Entities")
public class GenericEntity implements Serializable {
    @PrimaryKey
    private Integer id;
    private String name;
    private String type;
    private String size;
    private String owner;
    private String status;

    public GenericEntity(Integer id, String name, String type, String size, String owner, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.owner = owner;
        this.status = status;
    }

    @Ignore
    public GenericEntity(String name, String type, String size, String owner, String status) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.owner = owner;
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "GenericEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", owner='" + owner + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
