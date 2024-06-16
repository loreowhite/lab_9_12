package com.example.springMVC;

import com.example.springMVC.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-product-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-product-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProductList() throws Exception {
        mockMvc.perform(get("/api/product/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4));
    }

    @Test
    public void testProductListSorting() throws Exception {
        mockMvc.perform(get("/api/product/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(4))
                .andExpect(jsonPath("$.[0].name").value("product 4"));

        mockMvc.perform(get("/api/product/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[3].id").value(1))
                .andExpect(jsonPath("$.[3].name").value("product 1"));
    }

    @Test
    public void testProductById() throws Exception {
        mockMvc.perform(get("/api/product/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("product 2"));
    }

    @Test
    public void testBuyProduct() throws Exception {
        mockMvc.perform(post("/api/product/1/buy"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/product/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bought").value(true));
    }

    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/api/product/123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testNewQuery() throws Exception {
        Product product = new Product(5L, "product 5", true);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writer().writeValueAsString(product);
        var id = mockMvc.perform(post("/api/product/new").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(get("/api/product/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("product 5"))
                .andExpect(jsonPath("$.bought").value(true));
    }

    @Test
    public void testDeleteQuery() throws Exception {

        mockMvc.perform(delete("/api/product/delete/4"))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/product/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
