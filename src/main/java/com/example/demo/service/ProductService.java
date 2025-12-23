public interface ProductService {
    Product createProduct(Product product);
    void deactivateProduct(Long id);
}
@Service
public class ProductServiceImpl implements ProductService {

    public Product createProduct(Product product) {
        return product;
    }

    public void deactivateProduct(Long id) {
    }
}
