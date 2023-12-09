package app.magiavventure.category.operation;

import app.magiavventure.category.entity.ECategory;
import app.magiavventure.category.error.CategoryException;
import app.magiavventure.category.mapper.CategoryMapper;
import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.UpdateCategory;
import app.magiavventure.category.repository.CategoryRepository;
import app.magiavventure.category.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category operation tests")
class CategoryOperationTest {

    private CategoryOperation categoryOperation;
    @Mock
    private CategoryRepository categoryRepository;
    @Captor
    private ArgumentCaptor<ECategory> eCategoryArgumentCaptor;
    @Captor
    private ArgumentCaptor<Example<ECategory>> exampleArgumentCaptor;
    @Captor
    private ArgumentCaptor<Sort> sortArgumentCaptor;

    @BeforeEach
    void beforeEach() {
        CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
        CategoryService categoryService = new CategoryService(categoryRepository, categoryMapper);
        this.categoryOperation = new CategoryOperation(categoryService);
    }

    @Test
    @DisplayName("Create category test with name that not exists")
    void createCategoryTest_ok() {
        var createCategory = CreateCategory
                .builder()
                .name("test")
                .background("background")
                .active(true)
                .build();
        var eCategory = ECategory
                .builder()
                .id(UUID.randomUUID())
                .name("test")
                .background("background")
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(categoryRepository.save(eCategoryArgumentCaptor.capture()))
                .thenReturn(eCategory);

        Category categoryResponse = categoryOperation.createCategory(createCategory);

        Mockito.verify(categoryRepository).save(eCategoryArgumentCaptor.capture());
        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());

        ECategory eCategoryCaptured = eCategoryArgumentCaptor.getValue();
        Example<ECategory> eCategoryExampleCaptured = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(categoryResponse);
        Assertions.assertNotNull(categoryResponse.getId());
        Assertions.assertEquals(createCategory.getName(), categoryResponse.getName());
        Assertions.assertEquals(createCategory.getBackground(), categoryResponse.getBackground());
        Assertions.assertEquals(createCategory.getName(), eCategoryExampleCaptured.getProbe().getName());
        Assertions.assertNotNull(eCategoryCaptured.getId());
        Assertions.assertEquals(createCategory.getName(), eCategoryCaptured.getName());
        Assertions.assertEquals(createCategory.getBackground(), eCategoryCaptured.getBackground());
        Assertions.assertEquals(createCategory.isActive(), eCategoryCaptured.isActive());
    }

    @Test
    @DisplayName("Create category test with name that exists")
    void createCategoryTest_throwConflictException_categoryAlreadyExists() {
        var createCategory = CreateCategory
                .builder()
                .name("test")
                .background("background")
                .active(true)
                .build();

        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(Boolean.TRUE);

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryOperation.createCategory(createCategory));

        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());

        Example<ECategory> eCategoryExampleCaptured = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(createCategory.getName(), eCategoryExampleCaptured.getProbe().getName());
        Assertions.assertEquals("category-exists", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Find all categories test")
    void findAllCategoriesTest_ok() {

        var eCategory = ECategory
                .builder()
                .id(UUID.randomUUID())
                .name("test")
                .background("background")
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();
        List<ECategory> categories = List.of(eCategory);

        Mockito.when(categoryRepository.findAll(sortArgumentCaptor.capture()))
                .thenReturn(categories);

        List<Category> categoriesResponse = categoryOperation.retrieveCategories();

        Mockito.verify(categoryRepository).findAll(sortArgumentCaptor.capture());

        Sort sort = sortArgumentCaptor.getValue();

        Assertions.assertNotNull(categoriesResponse);
        Assertions.assertEquals(1, categoriesResponse.size());
        Assertions.assertNotNull(sort);
        Assertions.assertTrue(Objects.requireNonNull(sort.getOrderFor("name")).getDirection().isAscending());
        categoriesResponse
                .stream()
                .findFirst()
                .ifPresent(category -> {
                    Assertions.assertEquals(eCategory.getName(), category.getName());
                    Assertions.assertEquals(eCategory.getBackground(), category.getBackground());
                    Assertions.assertEquals(eCategory.getId(), category.getId());
                });
    }

    @Test
    @DisplayName("Find all categories test without results")
    void findAllCategoriesTest_withoutResults() {

        Mockito.when(categoryRepository.findAll(sortArgumentCaptor.capture()))
                .thenReturn(List.of());

        List<Category> categoriesResponse = categoryOperation.retrieveCategories();

        Mockito.verify(categoryRepository).findAll(sortArgumentCaptor.capture());

        Sort sort = sortArgumentCaptor.getValue();

        Assertions.assertNotNull(categoriesResponse);
        Assertions.assertEquals(0, categoriesResponse.size());
        Assertions.assertNotNull(sort);
        Assertions.assertTrue(Objects.requireNonNull(sort.getOrderFor("name")).getDirection().isAscending());
    }

    @Test
    @DisplayName("Find category by id test")
    void findCategoryByIdTest_ok() {

        var id = UUID.randomUUID();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(eCategory));

        Category categoryResponse = categoryOperation.retrieveCategory(id);

        Mockito.verify(categoryRepository).findById(id);

        Assertions.assertNotNull(categoryResponse);
        Assertions.assertEquals(eCategory.getName(), categoryResponse.getName());
        Assertions.assertEquals(eCategory.getBackground(), categoryResponse.getBackground());
        Assertions.assertEquals(eCategory.getId(), categoryResponse.getId());
    }

    @Test
    @DisplayName("Find category by id test that not exists")
    void findCategoryByIdTest_throwNotFoundException_categoryNotExists() {

        var id = UUID.randomUUID();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryOperation.retrieveCategory(id));

        Mockito.verify(categoryRepository).findById(id);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("category-not-found", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Update category test with name that not exists")
    void updateCategoryTest_ok() {
        var id = UUID.randomUUID();
        var updateCategory = UpdateCategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(false)
                .build();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("old_name")
                .background("old_background")
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();
        var savedECategory = ECategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .createdDate(LocalDateTime.now())
                .active(false)
                .build();

        Mockito.when(categoryRepository.findById(updateCategory.getId()))
                .thenReturn(Optional.of(eCategory));
        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(categoryRepository.save(eCategoryArgumentCaptor.capture()))
                .thenReturn(savedECategory);

        Category categoryResponse = categoryOperation.updateCategory(updateCategory);

        Mockito.verify(categoryRepository).save(eCategoryArgumentCaptor.capture());
        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        Mockito.verify(categoryRepository).findById(updateCategory.getId());

        ECategory eCategoryCaptured = eCategoryArgumentCaptor.getValue();
        Example<ECategory> eCategoryExampleCaptured = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(categoryResponse);
        Assertions.assertNotNull(categoryResponse.getId());
        Assertions.assertEquals(updateCategory.getName(), categoryResponse.getName());
        Assertions.assertEquals(updateCategory.getBackground(), categoryResponse.getBackground());
        Assertions.assertEquals(updateCategory.getName(), eCategoryExampleCaptured.getProbe().getName());
        Assertions.assertNotNull(eCategoryCaptured.getId());
        Assertions.assertEquals(updateCategory.getName(), eCategoryCaptured.getName());
        Assertions.assertEquals(updateCategory.getBackground(), eCategoryCaptured.getBackground());
        Assertions.assertEquals(updateCategory.getActive(), eCategoryCaptured.isActive());
    }

    @Test
    @DisplayName("Update category test with name that exists")
    void updateCategoryTest_throwConflictException_categoryAlreadyExists() {
        var id = UUID.randomUUID();
        var updateCategory = UpdateCategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(true)
                .build();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("old_name")
                .background("old_background")
                .createdDate(LocalDateTime.now())
                .active(true)
                .build();

        Mockito.when(categoryRepository.findById(updateCategory.getId()))
                .thenReturn(Optional.of(eCategory));
        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(Boolean.TRUE);

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryOperation.updateCategory(updateCategory));

        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        Mockito.verify(categoryRepository).findById(updateCategory.getId());

        Example<ECategory> eCategoryExampleCaptured = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(updateCategory.getName(), eCategoryExampleCaptured.getProbe().getName());
        Assertions.assertEquals("category-exists", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Update category test throw category not found")
    void updateCategoryTest_throwNotFoundException_categoryNotExists() {
        var id = UUID.randomUUID();
        var updateCategory = UpdateCategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(true)
                .build();

        Mockito.when(categoryRepository.findById(updateCategory.getId()))
                .thenReturn(Optional.empty());

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryOperation.updateCategory(updateCategory));

        Mockito.verify(categoryRepository).findById(updateCategory.getId());


        Assertions.assertNotNull(exception);
        Assertions.assertEquals("category-not-found", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }
}
