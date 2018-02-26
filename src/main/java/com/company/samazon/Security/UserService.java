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

//    public Cart findByCart(String username){   return cartRepository.findByAppuserByUsername(username); }

//    public Product findByName(String name){
//        return productRepository.findbyName(name);
//    }


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

    public void updateQuantity(Product product){
        product.setQuantity(product.getQuantity() -1);
    }



//    public void findCarts(AppUser appUser){
//        appUser.setCarts(Arrays.asList(cartRepository.findByAppuserByUsername(appUser.getUsername())));
//    }

    public void setActiveCart(AppUser appUser){
        Collection<Cart> carts = appUser.getCarts();
        Cart thisCart = new Cart();
        carts.add(thisCart);
        cartRepository.save(thisCart);
        appUser.setCarts(carts);
    }

    public Cart updateCart(Product product, Cart cart){
        Collection<Product> products = cart.getProducts();
        products.add(productRepository.findByName(product.getName()));
        cart.setProducts(products);
        cartRepository.save(cart);
        return cart;
    }

    public void removeItem(Product product, Cart cart){
        Collection<Product> products = cart.getProducts();
        products.remove(product);
        cart.setProducts(products);
    }

    public double getTotal(Cart cart){
        double total = 0;
        for(Product product : cart.getProducts()){
            total = total + (product.getPrice());
        }
        return total;
    }

    public Cart getActiveCart(String username){
        AppUser appUser=userRepository.findByUsername(username);
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
    public Collection<Cart> getOrders(){
        Collection<Cart> carts = new HashSet<>();
        for (Cart cart : cartRepository.findAll()){
            if(cart.getStatus().equalsIgnoreCase("NotActive")){
                carts.add(cart);
            }
        }
        return carts;
    }

    public void CheckoutCart(Cart cart){
        for (Product product : cart.getProducts()){
            updateQuantity(product);
        }
       // Collection<Cart> carts = userRepo
    }

    public void changeCartStatus(Cart cart){
        cart.setStatus("NotActive");
        cartRepository.save(cart);
    }

}


