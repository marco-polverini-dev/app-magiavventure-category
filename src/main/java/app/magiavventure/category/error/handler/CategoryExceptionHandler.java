package app.magiavventure.category.error.handler;

import app.magiavventure.category.configuration.CategoryProperties;
import app.magiavventure.category.configuration.CategoryProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.category.model.CategoryError;
import app.magiavventure.category.error.CategoryException;
import app.magiavventure.category.model.HttpError;
import app.magiavventure.category.mapper.CategoryErrorMapper;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class CategoryExceptionHandler {
    private final CategoryProperties categoryProperties;
    private final CategoryErrorMapper categoryErrorMapper;

    @ExceptionHandler({CategoryException.class})
    public ResponseEntity<HttpError> categoryExceptionHandler(CategoryException categoryException) {

        CategoryError categoryError = categoryException.getCategoryError();
        ErrorMessage errorMessage = retrieveError(categoryError.getKey(), categoryError.getArgs());

        if(Objects.isNull(errorMessage))
            errorMessage = retrieveError(CategoryException.UNKNOWN_ERROR);

        HttpError httpError = categoryErrorMapper.map(errorMessage);

        return ResponseEntity
                .status(httpError.getStatus())
                .body(httpError);
    }

    private ErrorMessage retrieveError(@NotNull String key, @Nullable Object... args) {
        var errorMessage = categoryProperties
                .getErrors()
                .getErrorMessages()
                .get(key);
        if(Objects.isNull(errorMessage)) return null;

        if(Objects.nonNull(args) && args.length > 0) {
            String message = errorMessage.getMessage();
            errorMessage.setMessage(String.format(message, args));
        }
        return errorMessage;
    }

}
