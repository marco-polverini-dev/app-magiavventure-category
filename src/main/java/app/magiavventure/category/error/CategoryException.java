package app.magiavventure.category.error;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    public static final String TEST = "test";
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
