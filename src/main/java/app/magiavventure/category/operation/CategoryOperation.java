package app.magiavventure.category.operation;

import app.magiavventure.category.error.CategoryException;
import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.UpdateCategory;
import app.magiavventure.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/categories")
public class CategoryOperation {

    private final CategoryService categoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody @Valid CreateCategory createCategory) {
        log.debug("Starting create category with: {}", createCategory);
        return categoryService.createCategory(createCategory);
    }

    @GetMapping
    public List<Category> retrieveCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category retrieveCategory(@PathVariable("id") UUID id) {
        return categoryService.findById(id);
    }

    @PutMapping
    public Category updateCategory(@RequestBody @Valid UpdateCategory updateCategory) {
        log.debug("Starting update category with: {}", updateCategory);
        return categoryService.updateCategory(updateCategory);
    }
}
