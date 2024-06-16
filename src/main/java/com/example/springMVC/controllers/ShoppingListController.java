package com.example.springMVC.controllers;

import com.example.springMVC.model.Product;
import com.example.springMVC.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ShoppingListController {
    private final ShoppingListService service;

    @Autowired
    public ShoppingListController(ShoppingListService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllItem() {
        return ResponseEntity.ok(service.getAllList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            var product = service.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        long id = service.addProductToList(product);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<?> changeBoughtFlag(@PathVariable("id") Long productId) {
        service.changeBought(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        service.deleteProductFromList(id);
        return ResponseEntity.ok().build();
    }
}
