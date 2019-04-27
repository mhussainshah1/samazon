package com.company.samazon.service;

import com.company.samazon.models.Cart;
import com.company.samazon.models.Product;
import com.company.samazon.models.User;
import com.company.samazon.models.repositories.CartRepository;
import com.company.samazon.models.repositories.ProductRepository;
import com.company.samazon.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    public void setActiveCart(User user) {
        Set<Cart> carts = user.getCarts();
        Cart thisCart = new Cart();
        carts.add(thisCart);
        cartRepository.save(thisCart);
        user.setCarts(carts);
    }

    public Cart getActiveCart(User user) {
        Set<Cart> carts = user.getCarts();
        Cart thisCart = new Cart();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("Active")) {
                thisCart = cart;
            }
        }
        return thisCart;
    }

    public Cart updateCart(Product product, User user) {
        Set<Cart> carts = user.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("Active")) {
                activeCart = cart;
            }
        }
        Set<Product> products = activeCart.getProducts();
        products.add(productRepository.findByName(product.getName()));
        activeCart.setProducts(products);
        cartRepository.save(activeCart);
        carts.add(activeCart);
        user.setCarts(carts);
        userRepository.save(user);
        return activeCart;
    }

    public double getTotal(Cart cart) {
        double total = 0.00;
        for (Product product : cart.getProducts()) {
            total = total + (Double.parseDouble(product.getPrice()));
        }
        return total;
    }

    public Cart CheckoutCart(User user) {
        Set<Cart> carts = user.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("Active")) {
                activeCart = cart;
            }
        }
        activeCart.setStatus("NotActive");
        cartRepository.save(activeCart);
        Cart newCart = new Cart();
        cartRepository.save(newCart);
        carts.add(activeCart);
        carts.add(newCart);
        user.setCarts(carts);
        userRepository.save(user);
        return activeCart;
    }

    public Cart getOrderDetails(long id) {
        return cartRepository.findById(id).get();
    }

    //this method looks for all inactive carts- order history
    public Set<Cart> getOrders(User user) {
        Set<Cart> carts = user.getCarts();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("NotActive")) {
                carts.add(cart);
            }
        }
        return carts;
    }

    public Set<Cart> getAllCarts(User user) {
        return user.getCarts();
    }

    public void removeItem(Product product, User user) {
        Set<Cart> carts = user.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("Active")) {
                activeCart = cart;
            }
        }
        Set<Product> products = activeCart.getProducts();
        products.remove(product);
        activeCart.setProducts(products);
        carts.add(activeCart);
        cartRepository.save(activeCart);
        user.setCarts(carts);
        userRepository.save(user);
    }

    public Set<Product> getActiveCartProducts(String username) {
        User user = userRepository.findByUsername(username);
        Set<Cart> carts = user.getCarts();
        Cart thisCart = new Cart();
        for (Cart cart : carts) {
            if (cart.getStatus().equalsIgnoreCase("Active")) {
                thisCart = cart;
            }
        }
        return thisCart.getProducts();
    }
}
