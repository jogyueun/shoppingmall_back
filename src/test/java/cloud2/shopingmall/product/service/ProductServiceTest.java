// package cloud2.shopingmall.product.service;


// import cloud2.shopingmall.TestConfig;
// import cloud2.shopingmall.product.entity.Product;
// import cloud2.shopingmall.product.entity.ProductDetails;
// import cloud2.shopingmall.product.repository.ProductRepository;
// import cloud2.shopingmall.product.repository.ProductDetailsRepository;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.transaction.annotation.Propagation;
// import org.springframework.transaction.annotation.Transactional;

// import static org.junit.jupiter.api.Assertions.*;

// @Transactional
// @SpringBootTest
// @Import(TestConfig.class)
// public class ProductServiceTest {

//     private final ProductRepository productRepository;
//     private final ProductDetailsRepository productDetailsRepository;
//     private final ProductService productService;

//     @Autowired
//     public ProductServiceTest(ProductRepository productRepository, ProductDetailsRepository productDetailsRepository, ProductService productService) {

//         this.productRepository = productRepository;
//         this.productDetailsRepository = productDetailsRepository;
//         this.productService = productService;
//     }

//     @Test
//     @Transactional(propagation = Propagation.NOT_SUPPORTED)
//     public void findProductWithOptionsAndImagesTest() {

//         Product pd3 = productService.getProductWithDetailsAndBodies(1L);
//         assertEquals(pd3.getProductDetails().get(0).getSize(), ProductDetails.Size.ONE);
//         assertEquals(pd3.getProductDetails().get(1).getSize(), ProductDetails.Size.TWO);
//         assertEquals(pd3.getProductBodies().get(0).getImageUrl(), "eliceImage");
//         assertEquals(pd3.getProductBodies().get(1).getImageUrl(), "eliceImage2");
//     }


//     @Test
//     @Transactional(propagation = Propagation.NOT_SUPPORTED)
//     public void deleteProductTest() {
//         assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
//         productService.deleteProduct(2L);
//         assertNull(productService.getProduct(2L));
//     }


// }
