package uz.app.sellerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.app.sellerapp.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
