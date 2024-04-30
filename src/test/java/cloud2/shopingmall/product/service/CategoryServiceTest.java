// package cloud2.shopingmall.product.service;

// import cloud2.shopingmall.product.dto.CategoryDTO;
// import cloud2.shopingmall.product.entity.Category;
// import cloud2.shopingmall.product.mapper.ProductMainMapper;
// import cloud2.shopingmall.product.repository.CategoryRepository;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.context.SpringBootTest;

// import java.util.List;

// import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// @SpringBootTest
// @AutoConfigureTestDatabase
// class CategoryServiceTest {

//     private final CategoryRepository categoryRepository;
//     private final ProductMainMapper.CategoryMapper categoryMapper;
//     private final CategoryService categoryService;

//     @Autowired
//     public CategoryServiceTest(CategoryRepository categoryRepository, ProductMainMapper.CategoryMapper
//             categoryMapper, CategoryService categoryService) {
//         this.categoryRepository = categoryRepository;
//         this.categoryMapper = categoryMapper;
//         this.categoryService = categoryService;
//     }

//     @DisplayName("GET API - JPA 데이터 전체 조회")
//     @Test
//     void findCategories() {
//         // Given
//         CategoryDTO categoryDTO = new CategoryDTO();
//         categoryDTO.setId(1L);
//         categoryDTO.setCategoryName("상의");
//         categoryDTO.setCategoryRank(1);

//         CategoryDTO categoryDTO2 = new CategoryDTO();
//         categoryDTO2.setId(2L);
//         categoryDTO2.setCategoryName("하의");
//         categoryDTO2.setCategoryRank(1);

//         categoryRepository.save(categoryMapper.toEntity(categoryDTO));
//         categoryRepository.save(categoryMapper.toEntity(categoryDTO2));

//         // When
//         List<Category> results = categoryService.findCategories();

//         // Then
//         assertThat(results.get(0).getId()).isEqualTo(1);
//         assertThat(results.get(0).getCategoryName()).isEqualTo("상의");
//         assertThat(results.get(0).getCategoryRank()).isEqualTo(1);
//         assertThat(results.get(1).getId()).isEqualTo(2);
//         assertThat(results.get(1).getCategoryName()).isEqualTo("하의");
//         assertThat(results.get(1).getCategoryRank()).isEqualTo(1);
//     }

//     @DisplayName("GET API - JPA 데이터 단일 조회")
//     @Test
//     void findCategory() {
//         // Given
//         CategoryDTO categoryDTO = new CategoryDTO();
//         categoryDTO.setId(1L);
//         categoryDTO.setCategoryName("상의");
//         categoryDTO.setCategoryRank(1);

//         categoryRepository.save(categoryMapper.toEntity(categoryDTO));

//         // When
//         Category result = categoryService.findCategory(1L);

//         // Then
//         assertThat(result.getId()).isEqualTo(1);
//         assertThat(result.getCategoryName()).isEqualTo("상의");
//         assertThat(result.getCategoryRank()).isEqualTo(1);
//     }

//     @DisplayName("POST API - JPA 데이터 생성")
//     @Test
//     void saveCategory() {
//         // Given
//         CategoryDTO categoryDTO = new CategoryDTO();
//         categoryDTO.setId(1L);
//         categoryDTO.setCategoryName("상의");
//         categoryDTO.setCategoryRank(1);

//         // When
//         Category result = categoryService.saveCategory(categoryDTO);

//         // Then
//         assertThat(result.getId()).isEqualTo(1);
//         assertThat(result.getCategoryName()).isEqualTo("상의");
//         assertThat(result.getCategoryRank()).isEqualTo(1);
//     }

//     @DisplayName("PUT API - JPA 데이터 수정")
//     @Test
//     void updateCategory() {
//         // Given
//         CategoryDTO categoryDTO = new CategoryDTO();
//         categoryDTO.setId(1L);
//         categoryDTO.setCategoryName("상의");
//         categoryDTO.setCategoryRank(1);

//         // When
//         Category result = categoryService.updateCategory(categoryDTO);

//         // Then
//         assertThat(result.getCategoryName()).isEqualTo("상의");
//         assertThat(result.getCategoryRank()).isEqualTo(1);
//     }

//     @DisplayName("DELETE API - JPA 데이터 삭제")
//     @Test
//     void deleteCategory() {
//     }
// }