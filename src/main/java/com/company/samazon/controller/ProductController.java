package com.company.samazon.controller;

import com.company.samazon.models.Product;
import com.company.samazon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    ////////////////////Everyone can view
    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "home";
    }

    //////////////////////////////////(ADMIN) USE TO CREATE NEW PRODUCT or EDIT
    @GetMapping("/newproduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "productform";
    }

    @PostMapping("/newproduct")
    public String processProduct(@Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "productform";
        }
        productService.addProduct(product);
        return "redirect:/";
    }

    @RequestMapping("/product/{id}")
    public String productDetails(@PathVariable("id") long id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "productpage";
    }

    @PostMapping("/searchproduct")
    public String showSearchResults(HttpServletRequest request, Model model) {
        String query = request.getParameter("search");
        model.addAttribute("search", query);
        model.addAttribute("searchproducts", productService.searchProducts(query));
        return "searchresult";
    }

    @RequestMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") long id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "productform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
