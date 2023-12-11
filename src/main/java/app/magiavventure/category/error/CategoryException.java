package app.magiavventure.category.error;

import app.magiavventure.category.model.CategoryError;
import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    public static final String CATEGORY_NOT_FOUND = "category-not-found";
    public static final String CATEGORY_EXISTS = "category-exists";
    public static final String UNKNOWN_ERROR = "unknown-error";

    private final transient CategoryError categoryError;

    private CategoryException(CategoryError categoryError) {
        super(categoryError.getKey(), categoryError.getThrowable());
        this.categoryError = categoryError;
    }

    public static CategoryException of(String key, String... args) {
        final var categoryError = CategoryError
                .builder()
                .key(key)
                .args(args)
                .build();
        return new CategoryException(categoryError);
    }

}
