package com.company.samazon.Models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String status;

    @ManyToMany(mappedBy = "carts")
    private Collection<AppUser> appuser;

    @ManyToMany
    private Collection<Product> products;

    public Cart() {
        this.appuser = new HashSet<>();
        this.products = new HashSet<>();
    }

    public Cart(Collection <AppUser> appuser) {
        this.appuser = appuser;
        this.status = "Active";
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public Collection<Product> getProducts() {
        return products;
    }

    public void setProducts(Collection<Product> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Collection<AppUser> getAppuser() {
        return appuser;
    }

    public void setAppuser(Collection<AppUser> appuser) {
        this.appuser = appuser;
    }
}
