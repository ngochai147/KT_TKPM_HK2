package iuh.fit.se.bai3;

import iuh.fit.se.bai3.technical.model.Product;
import iuh.fit.se.bai3.technical.service.ProductService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TechnicalApproachTest {

    @Test
    void testAddAndGetProduct() {
        ProductService service = new ProductService();
        service.addProduct(new Product(1L, "Laptop", 1500.0));
        service.addProduct(new Product(2L, "Phone", 800.0));

        Optional<Product> found = service.getProduct(1L);
        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
    }

    @Test
    void testGetAllProducts() {
        ProductService service = new ProductService();
        service.addProduct(new Product(1L, "Laptop", 1500.0));
        service.addProduct(new Product(2L, "Phone", 800.0));

        List<Product> all = service.getAllProducts();
        assertEquals(2, all.size());
    }

    @Test
    void testProductNotFound() {
        ProductService service = new ProductService();
        Optional<Product> found = service.getProduct(99L);
        assertFalse(found.isPresent());
    }
}

