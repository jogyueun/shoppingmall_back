package cloud2.shopingmall.image.controller;


import cloud2.shopingmall.product.dto.*;
import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import cloud2.shopingmall.product.service.ProductBodyService;
import cloud2.shopingmall.product.service.ProductImageService;
import cloud2.shopingmall.product.service.ProductService;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ImageController {

    //    @Value("${app.images.path}")
    //    private String uploadDir;
    //
    //    @Value("${app.images.path}")
    //    private String imagePath;

    private final ProductImageService productImageService;
    private final ProductBodyService productBodyService;

    @Autowired
    public ImageController(ProductImageService productImageService, ProductBodyService productBodyService){
        this.productImageService = productImageService;
        this.productBodyService = productBodyService;
    }

    @GetMapping(value = "/images/{imageName}")
    public ResponseEntity<UrlResource> getImage(@PathVariable("imageName") String imageName) throws IOException {

        // 로컬용 이미지 경로 설정                                                    
        // String uploadDir = System.getProperty("user.dir") + "/static/images";
        // String imagePath = "/images/";

        // 서버용 이미지 경로 설정
        String uploadDir = "/home/elice/test/static/images";
        String imagePath = "/static/images/";

        Path path = Paths.get(uploadDir + imageName);
        UrlResource resource =  new UrlResource(path.toUri());


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(resource);


    }


    @PutMapping("/api/admin/images/image")
    public ResponseEntity<ProductImageDTO> putImage(@RequestPart("productImageFile") MultipartFile productImageFile,
                                                                 @RequestPart("productImageDTO") ProductImageDTO productImageDTO,
                                                    @RequestParam("productId") Long productId) {


        // 로컬용 이미지 경로 설정                                                    
        // String uploadDir = System.getProperty("user.dir") + "/static/images";
        // String imagePath = "/images/";

        // 서버용 이미지 경로 설정
        String uploadDir = "/home/elice/test/static/images";
        String imagePath = "/static/images/";

        // 서버용 이미지 경로 설정
        // String uploadDir = "/home/elice/test/static/images";
        // String imagePath = "/static/images/";

        // 제목 디렉토리 생성
        File titleDir = new File(uploadDir);
        if (!titleDir.exists()) {
            titleDir.mkdirs();
        }



        String fileName = productImageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        String imageUrl = imagePath + fileName;

        try {
            Files.copy(productImageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = fileName.lastIndexOf(".");
        String ext = fileName.substring(index + 1).toLowerCase();
        Integer sizeKB = (int) productImageFile.getSize()/1000;

        productImageDTO.setUrl(imageUrl);
        productImageDTO.setSizeKB(sizeKB);
        productImageDTO.setImageFormat(ProductImage.ImageFormat.fromFormat(ext));

        ProductImageDTO newProductImageDTO = productImageService.saveProductImageDTO(productImageDTO, productId);


        return ResponseEntity.status(HttpStatus.OK)
                .body(newProductImageDTO);

    }



    @PutMapping("/api/admin/images/body")
    public ResponseEntity<ProductBodyDTO> putBody(@RequestPart("productImageFile") MultipartFile productImageFile,
                                                                 @RequestPart("productBodyDTO") ProductBodyDTO productBodyDTO,
                                                  @RequestParam("productId") Long productId) {


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



        String fileName = productImageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        String imageUrl = imagePath + fileName;

        try {
            Files.copy(productImageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = fileName.lastIndexOf(".");
        String ext = fileName.substring(index + 1).toLowerCase();
        Integer sizeKB = (int) productImageFile.getSize()/1000;

        productBodyDTO.setUrl(imageUrl);
        productBodyDTO.setSizeKB(sizeKB);
        productBodyDTO.setImageFormat(ProductBody.ImageFormat.fromFormat(ext));

        ProductBodyDTO newProductBodyDTO = productBodyService.saveProductBodyDTO(productBodyDTO, productId);


        return ResponseEntity.status(HttpStatus.OK)
                .body(newProductBodyDTO);

    }




}
