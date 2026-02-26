package iuh.fit.se.bai3.technical.service;

import iuh.fit.se.bai3.technical.model.Product;
import iuh.fit.se.bai3.technical.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repository = new ProductRepository();

    public void addProduct(Product product) {
        repository.save(product);
    }

    public Optional<Product> getProduct(Long id) {
        return repository.findById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}

