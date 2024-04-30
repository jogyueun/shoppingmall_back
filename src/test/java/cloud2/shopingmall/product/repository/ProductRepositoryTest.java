//package cloud2.shopingmall.product.repository;
//
//
//import cloud2.shopingmall.TestConfig;
//import cloud2.shopingmall.product.entity.Product;
//import cloud2.shopingmall.product.entity.ProductDetails;
//import org.hibernate.Hibernate;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Import(TestConfig.class)
//public class ProductRepositoryTest {
//
//    private final ProductRepository productRepository;
//    private final ProductBodyRepository productBodyRepository;
//    private final ProductDetailsRepository productDetailsRepository;
//
//
//    @Autowired
//    public ProductRepositoryTest(ProductRepository productRepository, ProductDetailsRepository productDetailsRepository, ProductBodyRepository productBodyRepository) {
//
//        this.productRepository = productRepository;
//        this.productDetailsRepository = productDetailsRepository;
//        this.productBodyRepository = productBodyRepository;
//
//    }
//
//    @BeforeEach
//    public void beforeTest(){
//    }
//
//
//
//    @Test
//    public void findProductWithOptionsTest(){
//
//        Product pd3 = productRepository.findProductWithDetails(1L);
//
//        assertTrue(Hibernate.isInitialized(pd3.getProductDetails()));
//        assertFalse(Hibernate.isInitialized(pd3.getProductBodies()));
//
//        assertEquals(pd3.getProductBodies().get(0).getImageUrl(), "eliceImage");
//        assertEquals(pd3.getProductBodies().get(1).getImageUrl(), "eliceImage2");
//        assertEquals(pd3.getProductDetails().get(0).getColor(), ProductDetails.Color.ONE);
//        assertEquals(pd3.getProductDetails().get(1).getColor(), ProductDetails.Color.TWO);
//
//
//    }
//
//
//
//}
