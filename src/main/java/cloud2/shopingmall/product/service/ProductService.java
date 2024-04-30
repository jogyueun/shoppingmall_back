package cloud2.shopingmall.product.service;

import cloud2.shopingmall.common.exception.ProductException;
import cloud2.shopingmall.order.entity.OrderProduct;
import cloud2.shopingmall.order.repository.OrderProductRepository;
import cloud2.shopingmall.product.dto.CategoryDTO;
import cloud2.shopingmall.product.dto.ProductDTO;
import cloud2.shopingmall.product.dto.ProductDetailsDTO;
import cloud2.shopingmall.product.entity.*;
import cloud2.shopingmall.product.mapper.ProductMainMapper;
import cloud2.shopingmall.product.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductBodyRepository productBodyRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final OrderProductRepository orderProductRepository;

    private final ProductMainMapper.ProductMapper productMapper;
    private final ProductMainMapper.CategoryMapper categoryMapper;
    private final ProductMainMapper.ProductWithDetailsAndBodiesMapper productWithDetailsAndBodiesMapper;
    private final CategoryRepository categoryRepository;
    private final CategoryProductRepository categoryProductRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository,
                          ProductBodyRepository productBodyRepository,
                          ProductDetailsRepository productDetailsRepository,
                          OrderProductRepository orderProductRepository,
                          ProductMainMapper.ProductMapper productMapper,
                          ProductMainMapper.ProductWithDetailsAndBodiesMapper productWithDetailsAndBodiesMapper,
                          ProductMainMapper.CategoryMapper categoryMapper,
                          CategoryProductRepository categoryProductRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.productBodyRepository = productBodyRepository;
        this.orderProductRepository = orderProductRepository;
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.productWithDetailsAndBodiesMapper = productWithDetailsAndBodiesMapper;
        this.categoryProductRepository = categoryProductRepository;
        this.categoryRepository = categoryRepository;
    }


    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<ProductDTO> getProductsDTO() {
        return productMapper.toDto(productRepository.findAll());
    }

    public Page<Product> getProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<ProductDTO> getProductsDTO(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(product -> productMapper.toDto(product));
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public List<ProductDTO> getProductsByNameOrIdDTO(String nameOrId) {
        return productMapper.toDto(productRepository.findByNameOrId(nameOrId));
    }


    public ProductDTO.ProductWithDetailsAndImagesDTO getProductWithAllDTO(Long id) {

        Product product = productRepository.findProductWithDetails(id);
        product = productRepository.findProductWithBodies(id);
        product = productRepository.findProductWithImages(id);
        if (product == null) {
            //TO DO
            throw new RuntimeException();
        }
        return productWithDetailsAndBodiesMapper.toDto(product);


    }

    public Product getProductWithDetailsAndBodies(Long id) {
//        Product product = productRepository.findProductWithDetailsAndBodies(id);
        Product product = productRepository.findProductWithDetails(id);
        product = productRepository.findProductWithBodies(id);
        return product;


    }

    public Product saveProduct(Product product) {
        if (productRepository.findByName(product.getName()) != null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }

        return productRepository.save(product);
    }

    public ProductDTO saveProductDTO(ProductDTO productDTO) {
        if (productRepository.findByName(productDTO.getName()) != null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }

        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDTO)));
    }

    public ProductDTO.ProductWithDetailsAndImagesDTO saveProductWithImagesAndCategoryDTO(
            ProductDTO.ProductWithDetailsAndImagesDTO productDTO, CategoryDTO categoryDTO) {

        Product product = productWithDetailsAndBodiesMapper.toEntity(productDTO);

        System.out.println(product);

        for (ProductBody productBody : product.getProductBodies()) {
            productBody.setProduct(product);

        }
        for (ProductImage productImage : product.getProductImages()) {
            productImage.setProduct(product);

        }

        for (ProductDetails productDetails : product.getProductDetails()) {
            productDetails.setProduct(product);

        }

        Product createdProduct = productRepository.save(product);
        productBodyRepository.saveAll(product.getProductBodies());
        productImageRepository.saveAll(product.getProductImages());
        productDetailsRepository.saveAll(product.getProductDetails());
        CategoryProduct categoryProduct = new CategoryProduct();
        Category category = categoryRepository.getReferenceById(categoryDTO.getId());
        categoryProduct.setCategory(category);
        categoryProduct.setProduct(product);
        categoryProductRepository.save(categoryProduct);

        return getProductWithAllDTO(createdProduct.getId());
    }


    public Product updateProduct(Product product) {

        return productRepository.save(product);
    }

    public ProductDTO updateProductDTO(ProductDTO.ProductWithCategoryDTO productDTO) {
        if (productDTO.getId() == null) {
            //TO DO: need new customException
            throw new RuntimeException();
        }
        Product Product = productRepository.getReferenceById(productDTO.getId());
        productMapper.updateFromDto(productDTO, Product);
        if(productDTO.getCategoryDTO() != null){
            CategoryProduct categoryProduct = categoryProductRepository.findByProduct_Id(productDTO.getId());
            Category category = categoryRepository.getReferenceById(productDTO.getCategoryDTO().getId());
            categoryProduct.setCategory(category);
        }



        return productMapper.toDto(Product);
    }


    public void offProduct(Long id) {
        Product product;
        try {
            product = productRepository.getReferenceById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        if (product.getStatus() == Product.ProductDisplayStatus.OFF) {
            //TO DO
            throw new RuntimeException();
        }
        product.setStatus(Product.ProductDisplayStatus.OFF);
    }

    public void onProduct(Long id) {
        Product product;
        try {
            product = productRepository.getReferenceById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
        if (product.getStatus() == Product.ProductDisplayStatus.ON) {
            //TO DO
            throw new RuntimeException();
        }
        product.setStatus(Product.ProductDisplayStatus.ON);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.getReferenceById(id);
        if (!product.getProductDetails().isEmpty()) {
            throw new RuntimeException();
        }
        List<OrderProduct> orderProducts = orderProductRepository.findByProduct_Id(id);
        if (!product.getProductDetails().isEmpty()) {
            throw new ProductException.OrderExistsException();
        }

        List<ProductBody> productBodies = productBodyRepository.findByProduct_Id(id);
        for (ProductBody productBody : productBodies) {
            productBody.setProduct(null);

        }
        product.setProductBodies(new ArrayList<>());
        productBodyRepository.deleteAll(productBodies);

        List<ProductImage> productImages = productImageRepository.findByProduct_Id(id);
        for (ProductImage productImage : productImages) {
            productImage.setProduct(null);

        }
        product.setProductImages(new ArrayList<>());
        productImageRepository.deleteAll(productImages);

        CategoryProduct categoryProduct = categoryProductRepository.findByProduct_Id(id);

        categoryProduct.setProduct(null);


        productImageRepository.deleteAll(productImages);

        productRepository.deleteById(id);


    }

//    @Transactional
//    public List<ProductDTO> searchProducts(String keyword, Pageable pageable) {
//        Page<Product> productPage = productRepository.findProductsByNameContaining(keyword, pageable);
//
//        if (productPage.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return productMapper.toDto(productPage.getContent());
//    }

    @Transactional
    public Page<ProductDTO> searchProducts(String keyword, Pageable pageable, Long categoryId) {

        if(categoryId == 0){
            return productRepository.findProductsByNameContaining(keyword == null ? "" : keyword , pageable).map(product -> productMapper.toDto(product));

        }

        return productRepository.findProductsByNameContainingByCategoryId(keyword == null ? "" : keyword , pageable, categoryId).map(product -> productMapper.toDto(product));
    }
}
