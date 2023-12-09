package app.magiavventure.category.mapper;

import app.magiavventure.category.configuration.CategoryProperties.ErrorProperties.ErrorMessage;
import app.magiavventure.category.error.handler.HttpError;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryErrorMapper {
    HttpError map(ErrorMessage errorMessage);

}
