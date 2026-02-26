package iuh.fit.se.bai3.domain.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    public void save(Product product) {
        products.add(product);
    }

    public Optional<Product> findById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }
}

