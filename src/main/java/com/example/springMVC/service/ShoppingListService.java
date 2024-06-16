package com.example.springMVC.service;

import com.example.springMVC.model.Product;
import com.example.springMVC.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {
    private final ShoppingListRepository repository;

    @Autowired
    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllList() {
        List<Product> products = repository.findAll();
        products.sort((p1, p2) -> (int) (p2.getId() - p1.getId()));
        return products;
    }

    public Product getProductById(Long id) {
        Product product = repository.findProductById(id);
        if (product == null){
            throw new IllegalArgumentException();
        }
        return product;
    }

    public long addProductToList(Product product) {
        repository.save(product);
        return repository.findAll().stream().max((x1, x2) -> (int) (x1.getId() - x2.getId())).get().getId();
    }

    public void changeBought(Long productId) {
        var product = repository.findProductById(productId);
        product.setBought(!product.isBought());
        repository.updateProduct(product.isBought(), productId);
    }

    public void deleteProductFromList(Long id) {
        repository.deleteProductById(id);
    }
}
