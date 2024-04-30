// package cloud2.shopingmall.product.service;


// import cloud2.shopingmall.TestConfig;
// import cloud2.shopingmall.product.entity.Product;
// import cloud2.shopingmall.product.repository.ProductRepository;
// import cloud2.shopingmall.product.repository.ProductDetailsRepository;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Import;
// import org.springframework.transaction.annotation.Propagation;
// import org.springframework.transaction.annotation.Transactional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNull;

// @Transactional
// @SpringBootTest
// @Import(TestConfig.class)
// public class ProductDetailsServiceTest {

//     private final ProductRepository productRepository;
//     private final ProductDetailsRepository productDetailsRepository;
//     private final ProductDetailsService productDetailsService;
//     @Autowired
//     public ProductDetailsServiceTest(ProductRepository productRepository, ProductDetailsRepository productDetailsRepository, ProductDetailsService productDetailsService) {

//         this.productRepository = productRepository;
//         this.productDetailsRepository = productDetailsRepository;
//         this.productDetailsService = productDetailsService;
//     }



//     @Test
//     @Transactional(propagation = Propagation.NOT_SUPPORTED)
//     public void deleteProductTest(){
//         Product product = productRepository.findProductWithDetails(1L);
//         assertEquals(product.getProductDetails().size(),2);

//         productDetailsService.deleteProductDetails(2L);
//         product = productRepository.findProductWithDetails(1L);

//         assertNull(productDetailsService.getProduct(2L));
//         assertEquals(product.getProductDetails().size(),1);

//     }




// }
