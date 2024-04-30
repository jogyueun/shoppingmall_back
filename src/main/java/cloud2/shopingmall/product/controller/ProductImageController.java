package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.ProductBodyDTO;
import cloud2.shopingmall.product.dto.ProductImageDTO;
import cloud2.shopingmall.product.service.ProductBodyService;
import cloud2.shopingmall.product.service.ProductImageService;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @Autowired
    public ProductImageController(ProductService productService, ProductImageService productImageService){
        this.productService = productService;
        this.productImageService = productImageService;
    }


    @GetMapping("/{productId}/image")
    public ResponseEntity<List<ProductImageDTO>> getProductImages(@PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productImageService.getProductImagesDTOByProductId(productId));
    }
//
    @PostMapping("/{productId}/image")
    public ResponseEntity<List<ProductImageDTO>> postProductImages(@RequestBody List<ProductImageDTO> productImagesDTO, @PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productImageService.saveProductImagesDTO(productImagesDTO, productId));

    }

    @PutMapping("/{productId}/image")
    public ResponseEntity<List<ProductImageDTO>> patchProductImages(@RequestBody List<ProductImageDTO> productImagesDTO, @PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productImageService.saveProductImagesDTO(productImagesDTO, productId));


    }



}
