package com.company.samazon.Models;

import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<AppUser> appusers;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Collection<AppUser> getAppusers() {
        return appusers;
    }

    public void setAppusers(Collection<AppUser> appusers) {
        this.appusers = appusers;
    }
}
