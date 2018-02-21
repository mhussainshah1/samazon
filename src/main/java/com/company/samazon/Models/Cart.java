package com.company.samazon.Models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser appuser;

    @ManyToMany(fetch =FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name="cart_id"),
            inverseJoinColumns = @JoinColumn(name="product_id"))
    private Collection<Product> products;

    public Cart() {
    }

    public Cart(AppUser appuser) {
        this.appuser = appuser;
        this.status = "Active";
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppUser getAppuser() {
        return appuser;
    }

    public void setAppuser(AppUser appuser) {
        this.appuser = appuser;
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
}
