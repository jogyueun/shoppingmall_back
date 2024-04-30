package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.*;
import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
//
//    @Value("${app.images.path}")
//    private String uploadDir;
//
//    @Value("${app.images.path}")
//    private String imagePath;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping
//    public ResponseEntity<List<ProductDTO>> getProducts(){
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(productService.getProductsDTO());
//    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(name = "page", defaultValue = "1") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductsDTO(page - 1, size));
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.ProductWithDetailsAndImagesDTO> getProductWithDetailsAndBodies(
            @PathVariable(name = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductWithAllDTO(id));

    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam(value = "keyword") String keyword,
//                                                           @PageableDefault(page = 1, size = 6, sort = "id") Pageable pageable) {
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(productService.searchProducts(keyword, pageable));
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                           @RequestParam(value = "categoryId") Long categoryId,
                                                           @PageableDefault(page = 1, size = 6, sort = "id") Pageable pageable) {


        pageable = PageRequest.of(pageable.getPageNumber() - 1, 6, Sort.by("id"));
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.searchProducts(keyword, pageable, categoryId));
    }

//    @PostMapping
//    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductDTO productDTO){
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(productService.saveProductDTO(productDTO));
//
//    }

}
