package com.company.samazon.controller;

import com.company.samazon.models.Cart;
import com.company.samazon.models.Product;
import com.company.samazon.models.User;
import com.company.samazon.service.CartService;
import com.company.samazon.service.ProductService;
import com.company.samazon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.HashSet;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /////////////////////////////////////////////// (USER)
    @RequestMapping("/addtocart/{id}")
    public String addToCart(@PathVariable("id") long id, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        Product product = productService.getProduct(id);
        Cart activeCart = cartService.updateCart(product, user);
        model.addAttribute("cart", activeCart);
        return "redirect:/cart";
    }

    /////////////////////////////////////////User's Ordering History
    @RequestMapping("/user")
    public String userDetails(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Collection<Cart> carts = new HashSet<>();
        for (Cart cart : cartService.getAllCarts(user)) {
            if (cart.getStatus().equalsIgnoreCase("NotActive")) {
                carts.add(cart);
                model.addAttribute("cart", cart);
            }
        }
        model.addAttribute("carts", carts);
        model.addAttribute("user", user);
        return "userdetails";
    }

    @RequestMapping("/order/{id}")
    public String viewOrder(Model model, @PathVariable("id") long id) {
        Cart cart = cartService.getOrderDetails(id);
        model.addAttribute("products", cart.getProducts());
        for (Product product : cart.getProducts()) {
            model.addAttribute("product", product);
        }
        double total = cartService.getTotal(cart);
        String message = "You spent over $50, You got Free Shipping";
        if (total < 50.0) {
            total += 5.0;
            message = "$5 for Shipping";
        }
        model.addAttribute("message", message);
        model.addAttribute("total", total);
        model.addAttribute("Order", cart);
        return "orderdetails";
    }

    @RequestMapping("/checkout")
    public String checkoutCart(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        Cart myCart = cartService.CheckoutCart(user);
        model.addAttribute("cart", myCart);
        Collection<Product> products = myCart.getProducts();
        for (Product product : products) {
            model.addAttribute("product", product);
        }
        double total = cartService.getTotal(myCart);
        String message = "You spent over $50, You got Free Shipping";
        if (total < 50.0) {
            total += 5.0;
            message = "$5 charged for Shipping";
        }
        model.addAttribute("message", message);
        model.addAttribute("total", total);
        model.addAttribute("products", products);
        return "confirmation";
    }

    /////////////////////////////////CUSTOMER VIEWING CART
    @RequestMapping("/cart")
    public String viewCart(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("user", user);
        Cart activeCart = cartService.getActiveCart(user);
        double total = cartService.getTotal(activeCart);
        String message = "You spent over $50, You get Free Shipping!";
        if (total < 50.0) {
            total += 5.0;
            message = "You need to spend $50 to get Free Shipping";
        }
        model.addAttribute("total", total);
        model.addAttribute("message", message);

        for (Product product : activeCart.getProducts()) {
            model.addAttribute("product", product);
        }

        model.addAttribute("products", activeCart.getProducts());
        return "cart";
    }

    /////////////////////////CUSTOMER CLICKS REMOVE PRODUCT WHILE LOOKING AT CART
    @RequestMapping("/remove/{id}")
    public String removeItem(@PathVariable("id") long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        Product product = productService.getProduct(id);
        cartService.removeItem(product, user);
        return "redirect:/cart";
    }
}
