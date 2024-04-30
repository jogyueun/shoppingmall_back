package cloud2.shopingmall.product.service;

import cloud2.shopingmall.product.dto.ProductDetailsDTO;
import cloud2.shopingmall.product.entity.Product;
import cloud2.shopingmall.product.entity.ProductDetails;
import cloud2.shopingmall.product.mapper.ProductMainMapper;
import cloud2.shopingmall.product.repository.ProductRepository;
import cloud2.shopingmall.product.repository.ProductDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final ProductRepository productRepository;

    private final ProductMainMapper.ProductDetailsMapper productDetailsMapper;

    @Autowired
    public ProductDetailsService(ProductDetailsRepository productDetailsRepository, ProductRepository productRepository, ProductMainMapper.ProductDetailsMapper productDetailsMapper) {
        this.productDetailsRepository = productDetailsRepository;
        this.productRepository = productRepository;
        this.productDetailsMapper = productDetailsMapper;
    }

    public ProductDetails getProduct(Long id) {
        return productDetailsRepository.findById(id).orElse(null);
    }

    public List<ProductDetails> getProductDetailsByProductId(Long productId) {
        return productDetailsRepository.findByProduct_Id(productId);
    }

    public List<ProductDetailsDTO> getProductDetailsDTOByProductId(Long productId) {
        return productDetailsMapper.toDto(productDetailsRepository.findByProduct_Id(productId));
    }

    public ProductDetails saveProductDetails(ProductDetails productDetails, Long productId) {
        Product product = productRepository.getReferenceById(productId);
        productDetails.setProduct(product);
        product.getProductDetails().add(productDetails);

        return productDetailsRepository.save(productDetails);
    }

    public ProductDetailsDTO saveProductDetailsDTO(ProductDetailsDTO productDetailsDTO, Long productId) {
        Product product = productRepository.getReferenceById(productId);
        ProductDetails productDetails = productDetailsMapper.toEntity(productDetailsDTO);
        productDetails.setProduct(product);
        product.getProductDetails().add(productDetails);

        ProductDetails createdProductDetails = productDetailsRepository.save(productDetails);

        return productDetailsMapper.toDto(createdProductDetails);
    }


    public ProductDetails updateProductDetails(ProductDetails productDetails) {
        if (productDetails.getId() == null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }

        return productDetailsRepository.save(productDetails);
    }

    public ProductDetailsDTO updateProductDetailsDTO(ProductDetailsDTO productDetailsDTO) {
        if (productDetailsDTO.getId() == null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }
        ProductDetails productDetails = productDetailsRepository.getReferenceById(productDetailsDTO.getId());
        productDetailsMapper.updateFromDto(productDetailsDTO, productDetails);

        return productDetailsMapper.toDto(productDetails);
    }


    public void offProductDetails(Long id) {
        try {
            ProductDetails productDetails = productDetailsRepository.getReferenceById(id);
            productDetails.setStatus(ProductDetails.ProductStatus.OFF);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void onProductDetails(Long id) {
        try {
            ProductDetails productDetails = productDetailsRepository.getReferenceById(id);
            productDetails.setStatus(ProductDetails.ProductStatus.ON);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteProductDetails(Long id) {
        ProductDetails productDetails = productDetailsRepository.getReferenceById(id);
        Product product = productDetails.getProduct();
        product.getProductDetails().remove(productDetails);
        productDetailsRepository.delete(productDetails);


    }

}
