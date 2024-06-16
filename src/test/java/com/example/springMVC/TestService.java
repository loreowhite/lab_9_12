package com.example.springMVC;

import com.example.springMVC.model.Product;
import com.example.springMVC.repository.ShoppingListRepository;
import com.example.springMVC.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class TestService {

    @InjectMocks
    private ShoppingListService service;

    @Mock
    private ShoppingListRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public List<Product> getProductList() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(1L, "product 1", false));
        list.add(new Product(2L, "product 2", false));
        list.add(new Product(3L, "product 3", true));
        list.add(new Product(4L, "product 4", false));

        return list;
    }

    @Test
    public void testGetListProduct() {
        Mockito.when(service.getAllList()).thenReturn(getProductList());
        var resultList = service.getAllList();
        Assert.assertEquals(4, resultList.size());

        Mockito.verify(repository).findAll();
    }

    @Test
    public void testGetProduct() {
        Mockito.doReturn(getProductList().get(0)).when(repository).findProductById(1L);
        Mockito.when(service.getProductById(1L)).thenReturn(getProductList().get(0));

        Assert.assertEquals("product 1", service.getProductById(1L).getName());

        Mockito.verify(repository).findProductById(1L);
    }
}
