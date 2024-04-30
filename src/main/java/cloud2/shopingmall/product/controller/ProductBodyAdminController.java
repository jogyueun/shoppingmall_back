package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.ProductBodyDTO;
import cloud2.shopingmall.product.service.ProductBodyService;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class ProductBodyAdminController {

    private final ProductService productService;
    private final ProductBodyService productBodyService;

    @Autowired
    public ProductBodyAdminController(ProductService productService, ProductBodyService productBodyService){
        this.productService = productService;
        this.productBodyService = productBodyService;
    }



    @DeleteMapping("/{productId}/body")
    public ResponseEntity deleteLastProductBody(@PathVariable(name = "productId") Long productId){
        productBodyService.deleteLastProductBody(productId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }


}
