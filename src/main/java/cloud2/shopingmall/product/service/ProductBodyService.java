package cloud2.shopingmall.product.service;

import cloud2.shopingmall.product.dto.ProductBodyDTO;
import cloud2.shopingmall.product.dto.ProductImageDTO;
import cloud2.shopingmall.product.entity.Product;
import cloud2.shopingmall.product.entity.ProductBody;
import cloud2.shopingmall.product.entity.ProductImage;
import cloud2.shopingmall.product.mapper.ProductMainMapper;
import cloud2.shopingmall.product.repository.ProductBodyRepository;
import cloud2.shopingmall.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductBodyService {

    private final ProductBodyRepository productBodyRepository;
    private final ProductRepository productRepository;

    private final ProductMainMapper.ProductBodyMapper productBodyMapper;


    @Autowired
    public ProductBodyService(ProductBodyRepository productBodyRepository, ProductRepository productRepository, ProductMainMapper.ProductBodyMapper productBodyMapper) {
        this.productBodyRepository = productBodyRepository;
        this.productRepository = productRepository;
        this.productBodyMapper = productBodyMapper;
    }

    public ProductBody getProductBody(Long id) {
        return productBodyRepository.findById(id).orElse(null);
    }

    public List<ProductBody> getProductBodiesByProductId(Long productId) {
        return productBodyRepository.findByProduct_Id(productId);
    }
    public List<ProductBodyDTO> getProductBodiesDTOByProductId(Long productId) {
        return productBodyMapper.toDto(productBodyRepository.findByProduct_Id(productId));
    }

    public List<ProductBody> saveProductBodies(List<ProductBody> productBodies, Long productId) {
        Product product = productRepository.getReferenceById(productId);
        for (ProductBody productBody : productBodies) {
            productBody.setProduct(product);

        }
        product.setProductBodies(productBodies);

        return productBodyRepository.saveAll(productBodies);
    }

    public ProductBodyDTO saveProductBodyDTO(ProductBodyDTO productBodyDTO, Long productId) {
        if(productBodyDTO.getId() == null){
            Product product = productRepository.getReferenceById(productId);
            ProductBody productBody = productBodyMapper.toEntity(productBodyDTO);
            product.getProductBodies().add(productBody);
            productBody.setProduct(product);


            return productBodyMapper.toDto(productBodyRepository.save(productBody));
        }
        ProductBody productBody = productBodyRepository.getReferenceById(productBodyDTO.getId());
        productBodyMapper.updateFromDto(productBodyDTO, productBody);

        return productBodyMapper.toDto(productBody);
    }

    public List<ProductBodyDTO> saveProductBodiesDTO(List<ProductBodyDTO> productBodiesDTO, Long productId) {
        Product product = productRepository.getReferenceById(productId);
        List<ProductBody> productBodies = productBodyMapper.toEntity(productBodiesDTO);

        for(int i = 0; i < productBodies.size(); i++){
            productBodies.get(i).setProduct(product);
            productBodies.get(i).setSequence(i);
        }
        product.setProductBodies(productBodies);

        return productBodyMapper.toDto(productBodyRepository.saveAll(productBodies));
    }


    public List<ProductBody> updateProductBodies(List<ProductBody> productBodies) {
        if (productBodies.get(0).getId() == null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }

        return productBodyRepository.saveAll(productBodies);
    }

    public void deleteLastProductBody(Long productId) {
        Product product = productRepository.getReferenceById(productId);
        ProductBody productBody = product.getProductBodies().get(product.getProductBodies().size() - 1);
        product.getProductBodies().remove(product.getProductBodies().size() - 1);
        productBody.setProduct(null);

        productBodyRepository.delete(productBody);
    }

    public void deleteProductBodies(Long productId) {
        List<ProductBody> productBodies = productBodyRepository.findByProduct_Id(productId);
        Product product = productRepository.getReferenceById(productId);
        product.setProductBodies(null);
        for (ProductBody productBody : productBodies) {
            productBody.setProduct(null);
        }
        productBodyRepository.deleteAll(productBodies);
    }

}
