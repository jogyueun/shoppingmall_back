package cloud2.shopingmall.product.controller;


import cloud2.shopingmall.product.dto.*;
import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import cloud2.shopingmall.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/admin/products")
public class ProductAdminController {
//
//    @Value("${app.images.path}")
//    private String uploadDir;
//
//    @Value("${app.images.path}")
//    private String imagePath;

    private final ProductService productService;

    @Autowired
    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(params = "nameOrId")
    public ResponseEntity<List<ProductDTO>> getProductsByNameOrId(@RequestParam(name = "nameOrId") String nameOrId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductsByNameOrIdDTO(nameOrId));
    }
    @PostMapping
    public ResponseEntity<ProductDTO.ProductWithDetailsAndImagesDTO> postProductWithImages(
            @RequestParam("title") String title,
            @RequestParam("price") Integer price,
            @RequestPart("categoryDTO") CategoryDTO categoryDTO,
            @RequestPart("productDetails") List<ProductDetailsDTO> productDetailsDTOList,
            @RequestPart("productImages") List<MultipartFile> productImagesFile,
            @RequestPart("productBodies") List<MultipartFile> productBodiesFile) {

        // 로컬용 이미지 경로 설정
        // String uploadDir = System.getProperty("user.dir") + "/static/images";
        // String imagePath = "/images/";

        // 서버용 이미지 경로 설정
        String uploadDir = "/home/elice/test/static/images";
        String imagePath = "/static/images/";

        // 제목 디렉토리 생성
        File titleDir = new File(uploadDir);
        if (!titleDir.exists()) {
            titleDir.mkdirs();
        }

        List<ProductImageDTO> productImageDTOList = new ArrayList<>();
        List<ProductBodyDTO> productBodyDTOList = new ArrayList<>();

        // 이미지 파일 저장
        for (MultipartFile file : productImagesFile) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            String imageUrl = imagePath + fileName;

            try {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int index = fileName.lastIndexOf(".");
            String ext = fileName.substring(index + 1).toLowerCase();
            Integer sizeKB = (int) file.getSize() / 1000;

            ProductImageDTO productImageDTO = ProductImageDTO.builder()
                    .url(imageUrl)
                    .sizeKB(sizeKB)
                    .sequence(productImageDTOList.size() + 1)
                    .imageFormat(ProductImage.ImageFormat.fromFormat(ext))
                    .build();
            productImageDTOList.add(productImageDTO);
        }

        // 본문 파일 저장
        for (MultipartFile file : productBodiesFile) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            String imageUrl = imagePath + fileName;

            try {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int index = fileName.lastIndexOf(".");
            String ext = fileName.substring(index + 1).toLowerCase();
            Integer sizeKB = (int) file.getSize() / 1000;

            ProductBodyDTO productBodyDTO = ProductBodyDTO.builder()
                    .url(imageUrl)
                    .sizeKB(sizeKB)
                    .sequence(productBodyDTOList.size() + 1)
                    .imageFormat(ProductBody.ImageFormat.fromFormat(ext))
                    .build();
            productBodyDTOList.add(productBodyDTO);
        }

        ProductDTO.ProductWithDetailsAndImagesDTO productDTO = ProductDTO.ProductWithDetailsAndImagesDTO.builder()
                .productBodyDTOList(productBodyDTOList)
                .productImageDTOList(productImageDTOList)
                .productDetailsDTOList(productDetailsDTOList)
                .name(title)
                .price(price)
                .imageUrl(productImageDTOList.get(0).getUrl())
                .build();
        System.out.println(productDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.saveProductWithImagesAndCategoryDTO(productDTO, categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity patchProduct(@RequestBody ProductDTO.ProductWithCategoryDTO productDTO){

        productService.updateProductDTO(productDTO);


        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();

    }



    @PutMapping("/{id}/on")
    public ResponseEntity onProduct(@PathVariable(name = "id") Long id) {
        productService.onProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PutMapping("/{id}/off")
    public ResponseEntity offProduct(@PathVariable(name = "id") Long id) {
        productService.offProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}
