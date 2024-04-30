package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.ProductImageDTO;
import cloud2.shopingmall.product.service.ProductImageService;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductImageAdminController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @Autowired
    public ProductImageAdminController(ProductService productService, ProductImageService productImageService){
        this.productService = productService;
        this.productImageService = productImageService;
    }


    @DeleteMapping("/{productId}/image")
    public ResponseEntity deleteLastProductImage(@PathVariable(name = "productId") Long productId){
        productImageService.deleteLastProductImage(productId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }


}
