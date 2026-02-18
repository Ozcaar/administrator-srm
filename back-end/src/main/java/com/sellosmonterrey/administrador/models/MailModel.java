package com.sellosmonterrey.administrador.models;

import jakarta.persistence.*;

@Entity
@Table(name = "mail")
public class MailModel {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String Service;

    @Column(nullable = false)
    private String app_user;

    @Column(nullable = false)
    private String mail;

    @Column(nullable = false)
    private String password;

    private String recovery_mail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getUser() {
        return app_user;
    }

    public void setUser(String app_user) {
        this.app_user = app_user;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecovery_mail() {
        return recovery_mail;
    }

    public void setRecovery_mail(String recovery_mail) {
        this.recovery_mail = recovery_mail;
    }

}
