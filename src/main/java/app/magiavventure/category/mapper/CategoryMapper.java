package app.magiavventure.category.mapper;

import app.magiavventure.category.entity.ECategory;
import app.magiavventure.category.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category map(ECategory eUser);
}
