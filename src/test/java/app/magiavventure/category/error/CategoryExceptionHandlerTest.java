package app.magiavventure.category.error;

import app.magiavventure.category.configuration.CategoryProperties;
import app.magiavventure.category.error.handler.CategoryExceptionHandler;
import app.magiavventure.category.error.handler.HttpError;
import app.magiavventure.category.mapper.CategoryErrorMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
@DisplayName("Category exception handler tests")
class CategoryExceptionHandlerTest {


    @InjectMocks
    private CategoryExceptionHandler categoryExceptionHandler;

    @Spy
    private CategoryProperties categoryProperties = retrieveCategoryProperties();

    @Spy
    private CategoryErrorMapper categoryErrorMapper = Mappers.getMapper(CategoryErrorMapper.class);

    @ParameterizedTest
    @CsvSource({"unknown-error, unknown-error, errore sconosciuto, descrizione sconosciuta, 500, prova",
            "category-not-found, category-not-found, categoria non trovata, descrizione categoria non trovata, 404, prova",
            "category-exists, category-exists, categoria già esistente prova, descrizione categoria già esistente, 403, prova",
            "error-not-exists, unknown-error, errore sconosciuto, descrizione sconosciuta, 500, prova"})
    @DisplayName("Handle category exception and return ResponseEntity")
    void handleCategoryExceptionTest(String code, String expectedCode, String expectedMessage,
                                     String expectedDescription, int expectedStatus,
                                     String args) {

        var categoryException = CategoryException.of(code, args);

        ResponseEntity<HttpError> responseEntity = categoryExceptionHandler.categoryExceptionHandler(categoryException);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(expectedStatus, responseEntity.getStatusCode().value());
        Assertions.assertEquals(expectedCode, responseEntity.getBody().getCode());
        Assertions.assertEquals(expectedMessage, responseEntity.getBody().getMessage());
        Assertions.assertEquals(expectedDescription, responseEntity.getBody().getDescription());
        Assertions.assertEquals(expectedStatus, responseEntity.getBody().getStatus());
    }

    private CategoryProperties retrieveCategoryProperties() {
        var categoryProperties = new CategoryProperties();
        var errorProperties = new CategoryProperties.ErrorProperties();
        var mapErrorMessages = new HashMap<String, CategoryProperties.ErrorProperties.ErrorMessage>();
        mapErrorMessages.put("unknown-error", CategoryProperties.ErrorProperties.ErrorMessage
                .builder()
                        .code("unknown-error")
                        .status(500)
                        .message("errore sconosciuto")
                        .description("descrizione sconosciuta")
                .build());
        mapErrorMessages.put("category-not-found", CategoryProperties.ErrorProperties.ErrorMessage
                .builder()
                .code("category-not-found")
                .status(404)
                .message("categoria non trovata")
                .description("descrizione categoria non trovata")
                .build());
        mapErrorMessages.put("category-exists", CategoryProperties.ErrorProperties.ErrorMessage
                .builder()
                .code("category-exists")
                .status(403)
                .message("categoria già esistente %s")
                .description("descrizione categoria già esistente")
                .build());
        errorProperties.setErrorMessages(mapErrorMessages);
        categoryProperties.setErrors(errorProperties);
        return categoryProperties;
    }


}
