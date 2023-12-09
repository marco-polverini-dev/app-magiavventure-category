package app.magiavventure.category.service;

import app.magiavventure.category.entity.ECategory;
import app.magiavventure.category.error.CategoryException;
import app.magiavventure.category.mapper.CategoryMapper;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.model.UpdateCategory;
import app.magiavventure.category.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category service tests")
class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Spy
    private CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);
    @Captor
    ArgumentCaptor<ECategory> eCategoryCaptor;
    @Captor
    ArgumentCaptor<Example<ECategory>> exampleArgumentCaptor;
    @Captor
    ArgumentCaptor<Sort> sortArgumentCaptor;

    @Test
    @DisplayName("Create category with name that not exists")
    void createCategory_ok_nameNotExists() {
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
                .active(true)
                .build();

        Mockito.when(categoryRepository.save(eCategoryCaptor.capture()))
                .thenReturn(eCategory);
        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(false);

        Category category = categoryService.createCategory(createCategory);

        Mockito.verify(categoryRepository).save(eCategoryCaptor.capture());
        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        ECategory categoryCapt = eCategoryCaptor.getValue();
        Example<ECategory> example = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(createCategory.getName(), category.getName());
        Assertions.assertEquals(createCategory.getBackground(), category.getBackground());
        Assertions.assertEquals(createCategory.getName(), categoryCapt.getName());
        Assertions.assertEquals(createCategory.getBackground(), categoryCapt.getBackground());
        Assertions.assertNotNull(categoryCapt.getId());
        Assertions.assertTrue(categoryCapt.isActive());
        Assertions.assertEquals(createCategory.getName(), example.getProbe().getName());
    }

    @Test
    @DisplayName("Create category with name that already exists")
     void createCategory_ko_nameAlreadyExists() {
        var createCategory = CreateCategory
                .builder()
                .name("test")
                .background("background")
                .active(true)
                .build();

        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(true);

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryService.createCategory(createCategory));

        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        Example<ECategory> example = exampleArgumentCaptor.getValue();

        Assertions.assertEquals(createCategory.getName(), example.getProbe().getName());
        Assertions.assertEquals("category-exists", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Update category with name that not exists")
    void updateCategory_ok_nameNotExists() {
        var id = UUID.randomUUID();
        var updateCategory = UpdateCategory
                .builder()
                .id(id)
                .name("test 2")
                .background("background 2")
                .active(false)
                .build();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(true)
                .build();
        var eCategoryUpdated = ECategory
                .builder()
                .id(id)
                .name("test 2")
                .background("background 2")
                .active(false)
                .build();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(eCategory));
        Mockito.when(categoryRepository.save(eCategoryCaptor.capture()))
                .thenReturn(eCategoryUpdated);
        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(false);

        Category category = categoryService.updateCategory(updateCategory);

        Mockito.verify(categoryRepository).findById(id);
        Mockito.verify(categoryRepository).save(eCategoryCaptor.capture());
        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        ECategory categoryCapt = eCategoryCaptor.getValue();
        Example<ECategory> example = exampleArgumentCaptor.getValue();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(updateCategory.getName(), category.getName());
        Assertions.assertEquals(updateCategory.getBackground(), category.getBackground());
        Assertions.assertEquals(updateCategory.getName(), categoryCapt.getName());
        Assertions.assertEquals(updateCategory.getBackground(), categoryCapt.getBackground());
        Assertions.assertNotNull(categoryCapt.getId());
        Assertions.assertFalse(categoryCapt.isActive());
        Assertions.assertEquals(updateCategory.getName(), example.getProbe().getName());
    }

    @Test
    @DisplayName("Update category but category not found")
    void updateCategory_ko_categoryNotExists() {
        var updateCategory = UpdateCategory
                .builder()
                .id(UUID.randomUUID())
                .name("test 2")
                .background("background 2")
                .active(false)
                .build();

        Mockito.when(categoryRepository.findById(updateCategory.getId()))
                .thenReturn(Optional.empty());

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryService.updateCategory(updateCategory));

        Mockito.verify(categoryRepository).findById(updateCategory.getId());

        Assertions.assertEquals("category-not-found", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Update category with name that already exists")
    void updateCategory_ko_categoryNameAlreadyExists() {
        var id = UUID.randomUUID();
        var updateCategory = UpdateCategory
                .builder()
                .id(id)
                .name("test 2")
                .background("background 2")
                .active(false)
                .build();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(true)
                .build();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(eCategory));
        Mockito.when(categoryRepository.exists(exampleArgumentCaptor.capture()))
                .thenReturn(true);

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryService.updateCategory(updateCategory));

        Mockito.verify(categoryRepository).findById(id);
        Mockito.verify(categoryRepository).exists(exampleArgumentCaptor.capture());
        Example<ECategory> example = exampleArgumentCaptor.getValue();

        Assertions.assertEquals("category-exists", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
        Assertions.assertEquals(updateCategory.getName(), example.getProbe().getName());
    }

    @Test
    @DisplayName("Find category by id")
    void findCategoryById_ok() {
        var id = UUID.randomUUID();
        var eCategory = ECategory
                .builder()
                .id(id)
                .name("test")
                .background("background")
                .active(true)
                .build();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.of(eCategory));

        Category category = categoryService.findById(id);

        Mockito.verify(categoryRepository).findById(id);

        Assertions.assertNotNull(category);
        Assertions.assertEquals("test", category.getName());
        Assertions.assertEquals("background", category.getBackground());
    }

    @Test
    @DisplayName("Find category by id but not found")
    void findCategoryById_ko_notFound() {
        var id = UUID.randomUUID();

        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> categoryService.findById(id));

        Mockito.verify(categoryRepository).findById(id);

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("category-not-found", exception.getCategoryError().getKey());
        Assertions.assertEquals(1, exception.getCategoryError().getArgs().length);
    }

    @Test
    @DisplayName("Find all categories")
    void findAllCategories_ok() {
        var eCategory = ECategory
                .builder()
                .id(UUID.randomUUID())
                .name("test")
                .background("background")
                .active(true)
                .build();
        var categoriesResponse = List.of(eCategory);

        Mockito.when(categoryRepository.findAll(sortArgumentCaptor.capture()))
                .thenReturn(categoriesResponse);

        List<Category> categories = categoryService.findAll();

        Mockito.verify(categoryRepository).findAll(sortArgumentCaptor.capture());

        Sort sort = sortArgumentCaptor.getValue();

        Assertions.assertNotNull(categories);
        Assertions.assertEquals(1, categories.size());
        var order = sort.getOrderFor("name");
        Assertions.assertNotNull(order);
        Assertions.assertEquals(Sort.Direction.ASC, order.getDirection());
    }


}
