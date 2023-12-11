package app.magiavventure.category.mapper;

import app.magiavventure.category.configuration.CategoryProperties;
import app.magiavventure.category.model.HttpError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


@DisplayName("Category error mapping tests")
class CategoryErrorMapperTest {

    private final CategoryErrorMapper categoryErrorMapper = Mappers.getMapper(CategoryErrorMapper.class);

    @Test
    @DisplayName("Map ErrorMessage in HttpError object")
    void mapHttpError() {
        var errorMessage = CategoryProperties.ErrorProperties.ErrorMessage
                .builder()
                .code("code")
                .description("description")
                .message("message")
                .status(500)
                .build();
        HttpError httpError = categoryErrorMapper.map(errorMessage);

        Assertions.assertNotNull(httpError);
        Assertions.assertEquals(errorMessage.getCode(), httpError.getCode());
        Assertions.assertEquals(errorMessage.getMessage(), httpError.getMessage());
        Assertions.assertEquals(errorMessage.getDescription(), httpError.getDescription());
        Assertions.assertEquals(errorMessage.getStatus(), httpError.getStatus());
    }

    @Test
    @DisplayName("Map ErrorMessage null in HttpError")
    void mapHttpErrorNull() {
        HttpError httpError = categoryErrorMapper.map(null);

        Assertions.assertNull(httpError);
    }


}
