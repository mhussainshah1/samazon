package com.company.samazon.Security;


import com.company.samazon.Models.AppUser;
import com.company.samazon.Models.Cart;
import com.company.samazon.Models.Product;
import com.company.samazon.Repositories.CartRepository;
import com.company.samazon.Repositories.ProductRepository;
import com.company.samazon.Repositories.RoleRepository;
import com.company.samazon.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository){
        this.userRepository= userRepository;
        this.cartRepository= cartRepository;
        this.productRepository = productRepository;
    }


    public UserService() {
    }


    public AppUser findById(Long id){   return userRepository.findById(id); }

    public AppUser findByUsername(String username){   return userRepository.findByUsername(username); }


    public void saveCustomer(AppUser appuser){
        appuser.setRoles(Arrays.asList(roleRepository.findByRoleName("CUSTOMER")));
        userRepository.save(appuser);
    }

    public void saveAdmin(AppUser appuser){
        appuser.setRoles(Arrays.asList(roleRepository.findByRoleName("ADMIN")));
        userRepository.save(appuser);
    }

    public void saveProduct(Product product){
        productRepository.save(product);
    }


    public void setActiveCart(AppUser appUser){
        Collection<Cart> carts = appUser.getCarts();
        Cart thisCart = new Cart();
        carts.add(thisCart);
        cartRepository.save(thisCart);
        appUser.setCarts(carts);
    }

    public Cart updateCart(Product product, AppUser user){
        Collection<Cart> carts = user.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("Active")){
                activeCart = cart;
            }
        }
        Collection<Product> products = activeCart.getProducts();
        products.add(productRepository.findByName(product.getName()));
        activeCart.setProducts(products);
        cartRepository.save(activeCart);
        carts.add(activeCart);
        user.setCarts(carts);
        userRepository.save(user);
        return activeCart;
    }


    public void removeItem(Product product, AppUser appUser){
        Collection<Cart> carts = appUser.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("Active")){
                activeCart = cart;
            }
        }
        Collection<Product> products = activeCart.getProducts();
        products.remove(product);
        activeCart.setProducts(products);
        carts.add(activeCart);
        cartRepository.save(activeCart);
        appUser.setCarts(carts);
        userRepository.save(appUser);
    }

    public double getTotal(Cart cart){
        double total = 0.00;
        for(Product product : cart.getProducts()){
            total = total + (Double.parseDouble(product.getPrice()));
        }
        return total;
    }

    public Cart getActiveCart(AppUser appUser){
        Collection<Cart> carts= appUser.getCarts();
        Cart thisCart = new Cart();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("Active")){
                thisCart = cart;
            }
        }
        return thisCart;
    }

    public Collection<Product> getActiveCartProducts(String username){
        AppUser appUser=userRepository.findByUsername(username);
        Collection<Cart> carts= appUser.getCarts();
        Cart thisCart = new Cart();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("Active")){
                thisCart = cart;
            }
        }
        return thisCart.getProducts();
    }

    //this method looks for all inactive carts- order history
    public Collection<Cart> getOrders(AppUser appUser){
        Collection<Cart> carts = appUser.getCarts();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("NotActive")){
                carts.add(cart);
            }
        }
        return carts;
    }

    public Collection<Cart> getAllCarts(AppUser appUser){
        return appUser.getCarts();
    }

    public Cart CheckoutCart(AppUser appUser){
        Collection<Cart> carts= appUser.getCarts();
        Cart activeCart = new Cart();
        for (Cart cart : carts){
            if(cart.getStatus().equalsIgnoreCase("Active")){
                activeCart = cart;
            }
        }
        activeCart.setStatus("NotActive");
        cartRepository.save(activeCart);
        Cart newCart = new Cart();
        cartRepository.save(newCart);
        carts.add(activeCart);
        carts.add(newCart);
        appUser.setCarts(carts);
        userRepository.save(appUser);
        return activeCart;
    }

    public void updateQuantity(Product product){
        product.setQuantity(product.getQuantity() -1);
    }

    public Iterable<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProduct(long id){
        return productRepository.findOne(id);
    }

    public void deleteProduct(long id){
        productRepository.delete(id);
    }

    public Cart getOrderDetails(long id){
        return cartRepository.findOne(id);
    }

    public Iterable<Product> searchProducts(String query){
        return productRepository.findAllByNameContainingIgnoreCase(query);
    }


}


