package com.company.samazon.Models;


import javax.persistence.*;
import java.util.Collection;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @Column
    private float price;

    @Column
    private String description;

    @Column
    private int quantity;

    @ManyToMany(fetch =FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="cart_id"))
    private Collection<Cart> carts;

    public Product() {
    }

    public Product(String name, float price, String description, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Collection<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Collection<Cart> carts) {
        this.carts = carts;
    }
}
