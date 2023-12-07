package app.magiavventure.category.service;

import app.magiavventure.category.entity.ECategory;
import app.magiavventure.category.mapper.CategoryMapper;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    @DisplayName("Create category test")
    void createUserTest() {
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

        Category category = categoryService.createCategory(createCategory);

        Mockito.verify(categoryRepository).save(eCategoryCaptor.capture());
        ECategory userCapt = eCategoryCaptor.getValue();

        Assertions.assertNotNull(category);
        Assertions.assertEquals(createCategory.getName(), category.getName());
        Assertions.assertEquals(createCategory.getBackground(), category.getBackground());
        Assertions.assertEquals(createCategory.getName(), userCapt.getName());
        Assertions.assertEquals(createCategory.getBackground(), userCapt.getBackground());
        Assertions.assertNotNull(userCapt.getId());
        Assertions.assertTrue(userCapt.isActive());
    }

}
