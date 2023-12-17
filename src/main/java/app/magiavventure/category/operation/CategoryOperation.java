package app.magiavventure.category.operation;

import app.magiavventure.category.model.CreateCategory;
import app.magiavventure.category.model.Category;
import app.magiavventure.category.model.UpdateCategory;
import app.magiavventure.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category Operation", description = "Create, update and search categories")
public class CategoryOperation {

    private final CategoryService categoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody @Valid CreateCategory createCategory) {
        return categoryService.createCategory(createCategory);
    }

    @GetMapping
    public List<Category> retrieveCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category retrieveCategory(@PathVariable(name = "id") UUID id) {
        return categoryService.findById(id);
    }

    @PutMapping
    public Category updateCategory(@RequestBody @Valid UpdateCategory updateCategory) {
        return categoryService.updateCategory(updateCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable(name = "id") UUID id) {
        categoryService.deleteById(id);
    }
}
