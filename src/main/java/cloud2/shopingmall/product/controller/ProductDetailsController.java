package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.ProductDTO;
import cloud2.shopingmall.product.dto.ProductDetailsDTO;
import cloud2.shopingmall.product.service.ProductDetailsService;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductDetailsController {

    private final ProductService productService;
    private final ProductDetailsService productDetailsService;

    @Autowired
    public ProductDetailsController(ProductService productService, ProductDetailsService productDetailsService){
        this.productService = productService;
        this.productDetailsService = productDetailsService;
    }


    @GetMapping("/{productId}/details")
    public ResponseEntity<List<ProductDetailsDTO>> getProductDetails(@PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productDetailsService.getProductDetailsDTOByProductId(productId));
    }


}
