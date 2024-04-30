package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.ProductDetailsDTO;
import cloud2.shopingmall.product.service.ProductDetailsService;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductDetailsAdminController {

    private final ProductService productService;
    private final ProductDetailsService productDetailsService;

    @Autowired
    public ProductDetailsAdminController(ProductService productService, ProductDetailsService productDetailsService){
        this.productService = productService;
        this.productDetailsService = productDetailsService;
    }



    @PostMapping("/{productId}/details")
    public ResponseEntity<ProductDetailsDTO> postProductDetails(@RequestBody ProductDetailsDTO productDetailsDTO, @PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productDetailsService.saveProductDetailsDTO(productDetailsDTO, productId));

    }



    @PutMapping("/{productId}/details")
    public ResponseEntity<ProductDetailsDTO> patchProductDetails(@RequestBody ProductDetailsDTO productDetailsDTO, @PathVariable(name = "productId") Long productId){
        productDetailsDTO.setId(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productDetailsService.updateProductDetailsDTO(productDetailsDTO));

    }

    @DeleteMapping("/{productId}/details/{id}")
    public ResponseEntity deleteProductDetails(@PathVariable(name = "id") Long id){
        productDetailsService.deleteProductDetails(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
