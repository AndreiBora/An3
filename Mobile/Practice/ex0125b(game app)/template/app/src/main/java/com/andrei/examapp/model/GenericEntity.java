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
    private Integer quantity;
    private String type;
    private String status;


    public GenericEntity(Integer id, String name, Integer quantity, String type, String status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }

    @Ignore
    public GenericEntity(String name, Integer quantity, String type, String status) {
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
