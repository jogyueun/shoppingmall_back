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
@RequestMapping("/api/products")
public class ProductBodyController {

    private final ProductService productService;
    private final ProductBodyService productBodyService;

    @Autowired
    public ProductBodyController(ProductService productService, ProductBodyService productBodyService){
        this.productService = productService;
        this.productBodyService = productBodyService;
    }


    @GetMapping("/{productId}/body")
    public ResponseEntity<List<ProductBodyDTO>> getProductBodies(@PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productBodyService.getProductBodiesDTOByProductId(productId));
    }
//
    @PostMapping("/{productId}/body")
    public ResponseEntity<List<ProductBodyDTO>> postProductBodies(@RequestBody List<ProductBodyDTO> productBodiesDTO, @PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productBodyService.saveProductBodiesDTO(productBodiesDTO, productId));

    }

    @PutMapping("/{productId}/body")
    public ResponseEntity<List<ProductBodyDTO>> patchProductBodies(@RequestBody List<ProductBodyDTO> productBodiesDTO, @PathVariable(name = "productId") Long productId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productBodyService.saveProductBodiesDTO(productBodiesDTO, productId));


    }




}
