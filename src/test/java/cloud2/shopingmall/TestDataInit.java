// package cloud2.shopingmall;

// import cloud2.shopingmall.product.entity.ProductDetails;
// import cloud2.shopingmall.product.entity.Product;
// import cloud2.shopingmall.product.entity.ProductBody;
// import cloud2.shopingmall.product.repository.ProductBodyRepository;
// import cloud2.shopingmall.product.repository.ProductRepository;
// import cloud2.shopingmall.product.repository.ProductDetailsRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.ApplicationArguments;
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.stereotype.Component;


// @Component
// public class TestDataInit implements ApplicationRunner {

//     @Autowired
//     ProductDetailsRepository productDetailsRepository;
//     @Autowired
//     ProductRepository productRepository;
//     @Autowired
//     ProductBodyRepository productBodyRepository;

//     @Override
//     public void run(ApplicationArguments args) {
//         initData();
//     }
//     public void initData(){
//         Product pd = new Product("elice", "description", "url");
//         ProductDetails productDetails = new ProductDetails("elice", ProductDetails.Size.ONE, ProductDetails.Color.ONE, 100, ProductDetails.ProductStatus.ONE);
//         ProductDetails productDetails2 = new ProductDetails("elice2", ProductDetails.Size.TWO, ProductDetails.Color.TWO, 200, ProductDetails.ProductStatus.TWO);

//         ProductBody productBody = new ProductBody("eliceImage", "description");
//         ProductBody productBody2 = new ProductBody("eliceImage2", "description");


//         pd.getProductDetails().add(productDetails);
//         pd.getProductDetails().add(productDetails2);
//         pd.getProductBodies().add(productBody);
//         pd.getProductBodies().add(productBody2);
//         productDetails.setProduct(pd);
//         productDetails2.setProduct(pd);
//         productBody.setProduct(pd);
//         productBody2.setProduct(pd);

//         productRepository.save(pd);
//         productDetailsRepository.save(productDetails);
//         productDetailsRepository.save(productDetails2);
//         productBodyRepository.save(productBody);
//         productBodyRepository.save(productBody2);


//         Product pd2 = new Product("elice", "description", "url");
//         ProductBody productBody3 = new ProductBody("eliceImage", "description");
//         ProductBody productBody4 = new ProductBody("eliceImage2", "description");

//         pd2.getProductBodies().add(productBody3);
//         pd2.getProductBodies().add(productBody4);
//         productBody3.setProduct(pd2);
//         productBody4.setProduct(pd2);

//         productRepository.save(pd2);
//         productBodyRepository.save(productBody3);
//         productBodyRepository.save(productBody4);



//     }
// }
