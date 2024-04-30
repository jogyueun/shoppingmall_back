package cloud2.shopingmall.product.service;

import cloud2.shopingmall.common.exception.CategoryException;
import cloud2.shopingmall.product.dto.CategoryDTO;
import cloud2.shopingmall.product.dto.CategoryProductDTO;
import cloud2.shopingmall.product.entity.Category;
import cloud2.shopingmall.product.entity.CategoryProduct;
import cloud2.shopingmall.product.entity.Product;
import cloud2.shopingmall.product.mapper.ProductMainMapper;
import cloud2.shopingmall.product.mapper.ProductMainMapper.ProductMapper;
import cloud2.shopingmall.product.repository.CategoryProductRepository;
import cloud2.shopingmall.product.repository.CategoryRepository;
import cloud2.shopingmall.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductMainMapper.CategoryMapper categoryMapper;
    private final CategoryProductRepository categoryProductRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private Category foundCategory;

    public CategoryService(CategoryRepository categoryRepository, ProductMainMapper.CategoryMapper categoryMapper,
                           CategoryProductRepository categoryProductRepository, ProductRepository productRepository,
                           ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.categoryProductRepository = categoryProductRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<CategoryDTO> findCategories() {
        return categoryMapper.toDto(categoryRepository.findAll());
    }
    public List<CategoryDTO> findCategoriesBySuperId(Long superId) {
        return categoryMapper.toDto(categoryRepository.findBySuperId(superId));
    }

    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public Category saveCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.findByName(categoryDTO.getName()).orElse(null) != null){
            throw new CategoryException.DuplicatedNameFoundException(categoryDTO.getName());
        }
        return categoryRepository.save(categoryMapper.toEntity(categoryDTO));
    }

    public Category updateCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.findByName(categoryDTO.getName()).orElse(null) != null){
            throw new CategoryException.DuplicatedNameFoundException(categoryDTO.getName());
        }
        foundCategory = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(NoSuchElementException::new);

        foundCategory.setName(categoryDTO.getName());
        foundCategory.setSuperId(categoryDTO.getSuperId());

        return categoryRepository.save(foundCategory);
    }

    public void deleteCategory(Long id) {
        foundCategory = categoryRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        categoryRepository.delete(foundCategory);
    }

    // Category를 조회했을때 해당 Category에 속한 Product 조회
    @Transactional
    public CategoryProductDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        List<Product> products = productRepository.findProductsWithCategoryId(id);

        return CategoryProductDTO.builder()
                .categoryDTO(categoryMapper.toDto(category))
                .productsDTOList(productMapper.toDto(products))
                .build();
    }
}
