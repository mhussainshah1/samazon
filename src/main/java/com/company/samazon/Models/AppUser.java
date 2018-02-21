package com.company.samazon.Models;


import com.company.samazon.Repositories.CartRepository;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String username;


    @Column
    private String password;

    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Collection<Role> roles;

    @ManyToMany(fetch =FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="cart_id"))
    private Collection<Cart> carts;




    public AppUser() {
        this.roles = new HashSet<>();
        this.carts = new HashSet<>();
        //double check this constructor
    }

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<Cart> getCarts() {
        return carts;
    }



    public void setCarts(Collection<Cart> carts) {
        this.carts = carts;
    }
}
