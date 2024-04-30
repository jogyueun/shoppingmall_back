package cloud2.shopingmall.product.service;

import cloud2.shopingmall.product.dto.ProductBodyDTO;
import cloud2.shopingmall.product.dto.ProductImageDTO;
import cloud2.shopingmall.product.entity.Product;
import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import cloud2.shopingmall.product.mapper.ProductMainMapper;
import cloud2.shopingmall.product.repository.ProductBodyRepository;
import cloud2.shopingmall.product.repository.ProductImageRepository;
import cloud2.shopingmall.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    private final ProductMainMapper.ProductImageMapper productImageMapper;


    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository, ProductRepository productRepository, ProductMainMapper.ProductImageMapper productImageMapper) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.productImageMapper = productImageMapper;
    }

    public List<ProductImageDTO> getProductImagesDTOByProductId(Long productId) {
        return productImageMapper.toDto(productImageRepository.findByProduct_Id(productId));
    }


    public List<ProductImageDTO> saveProductImagesDTO(List<ProductImageDTO> productImageDTOList, Long productId) {
        Product product = productRepository.getReferenceById(productId);
        List<ProductImage> productImages = productImageMapper.toEntity(productImageDTOList);

        for(int i = 0; i < productImages.size(); i++){
            productImages.get(i).setProduct(product);
            productImages.get(i).setSequence(i);
        }
        product.setProductImages(productImages);

        return productImageMapper.toDto(productImageRepository.saveAll(productImages));
    }

    public ProductImageDTO saveProductImageDTO(ProductImageDTO productImageDTO, Long productId) {
        if(productImageDTO.getId() == null){
            Product product = productRepository.getReferenceById(productId);
            ProductImage productImage = productImageMapper.toEntity(productImageDTO);
            product.getProductImages().add(productImage);
            productImage.setProduct(product);


            return productImageMapper.toDto(productImageRepository.save(productImage));
        }
        ProductImage productImage = productImageRepository.getReferenceById(productImageDTO.getId());
        productImageMapper.updateFromDto(productImageDTO, productImage);

        return productImageMapper.toDto(productImage);
    }


    public void deleteLastProductImage(Long productId) {
        Product product = productRepository.getReferenceById(productId);
        ProductImage productImage = product.getProductImages().get(product.getProductImages().size() - 1);
        product.getProductImages().remove(product.getProductImages().size() - 1);
        productImage.setProduct(null);

        productImageRepository.delete(productImage);
    }

    public void deleteProductImages(Long productId) {
        List<ProductImage> productImages = productImageRepository.findByProduct_Id(productId);
        Product product = productRepository.getReferenceById(productId);
        product.setProductImages(null);
        for (ProductImage productImage : productImages) {
            productImage.setProduct(null);
        }
        productImageRepository.deleteAll(productImages);
    }

}
