package com.sellosmonterrey.administrador.models;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "computer")
public class ComputerModel {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean active;

    @Column(unique = true)
    private String service_tag;

    @Column(unique = true)
    private String ip;

    private LocalDate adquisition_date;
    private String comment;

    public String getService_tag() {
        return service_tag;
    }

    public void setService_tag(String service_tag) {
        this.service_tag = service_tag;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getAdquisition_date() {
        return adquisition_date;
    }

    public void setAdquisition_date(LocalDate adquisition_date) {
        this.adquisition_date = adquisition_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
