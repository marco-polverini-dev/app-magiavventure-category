package app.magiavventure.category.service;

import app.magiavventure.category.entity.ECategory;
import app.magiavventure.category.mapper.CategoryMapper;
import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.UpdateCategory;
import app.magiavventure.category.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Category createCategory(CreateCategory createCategory) {
        this.checkIfCategoryExists(createCategory.getName());

        var categoryToSave = ECategory
                .builder()
                .id(UUID.randomUUID())
                .name(createCategory.getName())
                .background(createCategory.getBackground())
                .active(createCategory.isActive())
                .build();
        ECategory categoryCreated = categoryRepository.save(categoryToSave);
        log.debug("Created new category with: {}", categoryCreated);
        return categoryMapper.map(categoryCreated);
    }

    public Category updateCategory(UpdateCategory updateCategory) {
        ECategory categoryToUpdate = findEntityById(updateCategory.getId());


        if(Objects.nonNull(updateCategory.getName())
                && !updateCategory.getName().isBlank()
                    && !updateCategory.getName().equals(categoryToUpdate.getName())) {
            this.checkIfCategoryExists(updateCategory.getName());
            categoryToUpdate.setName(updateCategory.getName());
        }
        if(Objects.nonNull(updateCategory.getBackground()) && !updateCategory.getBackground().isBlank())
            categoryToUpdate.setBackground(updateCategory.getBackground());
        if(Objects.nonNull(updateCategory.getActive())
                && !updateCategory.getActive().equals(categoryToUpdate.isActive()))
            categoryToUpdate.setActive(updateCategory.getActive());

        ECategory categoryUpdated = categoryRepository.save(categoryToUpdate);
        log.debug("Updated category with: {}", categoryUpdated);
        return categoryMapper.map(categoryUpdated);
    }

    public Category findById(UUID id) {
        return categoryMapper.map(findEntityById(id));
    }

    private ECategory findEntityById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Impossibile trovare la categoria"));
    }

    public List<Category> findAll() {
        var sort = Sort.by(Sort.Direction.ASC, "name");
        return categoryRepository.findAll(sort)
                .stream()
                .map(categoryMapper::map)
                .toList();
    }

    private void checkIfCategoryExists(String name) {
        Example<ECategory> categoryExample = Example.of(ECategory
                .builder()
                .name(name)
                .build(), ExampleMatcher.matchingAny());

        if(categoryRepository.exists(categoryExample)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format("Impossibile procedere con il salvataggio, esiste già una categoria con nome: %s",
                            name));
        }
    }
}
