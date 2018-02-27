package com.company.samazon;


import com.company.samazon.Models.AppUser;
import com.company.samazon.Models.Cart;
import com.company.samazon.Models.Product;
import com.company.samazon.Repositories.CartRepository;
import com.company.samazon.Repositories.ProductRepository;
import com.company.samazon.Repositories.UserRepository;
import com.company.samazon.Security.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.CheckedOutputStream;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;


//////////////////////////////////////Everyone can view
//////////////////////////////Tested **** WORKS
    @RequestMapping("/")
    public String homePage(Model model){
        model.addAttribute("products", userService.getAllProducts());
        return "Home";
    }

    @RequestMapping("/product/{id}")
    public String productDetails(@PathVariable("id") long id, Model model){
        Product product = userService.getProduct(id);
        model.addAttribute("product", product);
        return "ProductPage";
    }
//////////////////////////////////////// New User, Login(CUSTOMER)
////////////////////////////////TESTED **** WORKS
    @RequestMapping("/login")
    public String login(Model model){
        return "Login";
    }

    @GetMapping("/newuser")
    public String newUser(Model model){
        model.addAttribute("appUser", new AppUser());
        return "UserForm";
    }

    @PostMapping("/newuser")
    public String processUser(@Valid @ModelAttribute("appUser") AppUser appUser){
        userService.saveCustomer(appUser);
        return "redirect:/login";
    }
/////////////////////////////////////////////// (CUSTOMER)
////////////////////////////////TESTED*******WORKS
    @RequestMapping("/addtocart/{id}")
    public String addToCart(@PathVariable("id") long id, Authentication auth, Model model){
        AppUser appUser = userService.findByUsername(auth.getName());
        Product product = userService.getProduct(id);
        Cart activeCart = userService.updateCart(product, appUser);
        model.addAttribute("cart", activeCart);
        return "redirect:/cart";
    }



    /////////////////////////////////////////User's Ordering History
    @RequestMapping("/user")
    public String userDetails(Model model, Authentication auth){
        AppUser appuser = userService.findByUsername(auth.getName());
        model.addAttribute("carts", appuser.getCarts());
        model.addAttribute("appuser", appuser);
        return "UserDetails";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/searchproduct")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        String query = request.getParameter("search");
        model.addAttribute("search", query);
        Iterable<Product> allProducts = userService.getAllProducts();
        Collection<Product> searchedProducts = new HashSet<>();
        for(Product product : allProducts){
            if (product.getName().contains(query)){
                searchedProducts.add(product);
                model.addAttribute("product", product);
            }
        }
        model.addAttribute("products", searchedProducts);
//        model.addAttribute("product", productRepository.findAllByNameContainingIgnoreCase(query));
        return "SearchResult";
    }

    @RequestMapping("/checkout")
    public String checkoutCart(Authentication auth, Model model){
        AppUser appUser = userService.findByUsername(auth.getName());
        ///Switches status of current active cart to 'notactive', creates new active cart
        Cart myCart = userService.CheckoutCart(appUser);
        model.addAttribute("cart", myCart);
        Collection<Product> products = myCart.getProducts();
        for(Product product: products){
            model.addAttribute("product", product);
        }
        model.addAttribute("products", products);
        return "Confirmation";
    }

/////////////////////////////////CUSTOMER VIEWING CART
//////////////////////////////TESTED****WORKS
    @RequestMapping("/cart")
    public String viewCart(Authentication auth, Model model){
        AppUser appUser = userService.findByUsername(auth.getName());
        model.addAttribute("appUser", appUser);
        Cart activeCart = userService.getActiveCart(appUser);
        double total = userService.getTotal(activeCart);
        String message = "You spent over $50, You get Free Shipping!";
        if (total < 50.0){
            total += 5.0;
            message = "You need to spend $50 to get Free Shipping";
        }
        model.addAttribute("total", total);
        model.addAttribute("message", message);

        for (Product product : activeCart.getProducts()){
           model.addAttribute("product", product);}

           model.addAttribute("products", activeCart.getProducts());
        return "Cart";
    }


/////////////////////////CUSTOMER CLICKS REMOVE PRODUCT WHILE LOOKING AT CART
//////////////////////////////////////TESTED ****** WORKS
    @RequestMapping("/remove/{id}")
    public String removeItem(@PathVariable("id") long id, Authentication auth){
        AppUser appUser = userService.findByUsername(auth.getName());
        Product product = userService.getProduct(id);
        userService.removeItem(product, appUser);
        return "redirect:/cart";
    }

//////////////////////////////////////////////////////////(ADMIN) USE TO CREATE NEW PRODUCT or EDIT
/////////////////////////////////////////////////////////TESTED******ALL WORK

    @GetMapping("/newproduct")
    public String newProduct(Model model){
        model.addAttribute("product", new Product());
        return "ProductForm";
    }

    @PostMapping("/newproduct")
    public String processProduct(@Valid @ModelAttribute("product") Product product, BindingResult result){
        if(result.hasErrors()){
            return "ProductForm";
        }
        userService.saveProduct(product);
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") long id, Model model){
        Product product = userService.getProduct(id);
        model.addAttribute("product", product);
        return "ProductForm";
    }


    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id){
        userService.deleteProduct(id);
        ///// Updates all user's carts and removes item
        return "redirect:/";
    }



    public Collection<Product> findProducts(String query){
        Iterable<Product> allProducts = userService.getAllProducts();;
        Collection<Product> searchedProducts = new HashSet<>();
        for(Product product : allProducts){
            if (product.getName().contains(query)){
                searchedProducts.add(product);
            }
        }
        return searchedProducts;
    }

}
