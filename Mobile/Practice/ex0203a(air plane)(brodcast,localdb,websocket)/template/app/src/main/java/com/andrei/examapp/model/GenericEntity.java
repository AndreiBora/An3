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
    private String manufacturer;
    private String status;
    private Integer year;
    private Integer miles;
    private Boolean inSync;

    public GenericEntity(Integer id, String name, String manufacturer, String status, Integer year, Integer miles) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.status = status;
        this.year = year;
        this.miles = miles;
    }

    @Ignore
    public GenericEntity() {
    }

    public Boolean getInSync() {
        return inSync;
    }

    public void setInSync(Boolean inSync) {
        this.inSync = inSync;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    @Override
    public String toString() {
        return "GenericEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", status='" + status + '\'' +
                ", year=" + year +
                ", miles=" + miles +
                '}';
    }
}
