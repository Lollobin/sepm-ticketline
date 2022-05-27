package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @ValueMappings({
        @ValueMapping(target = "CLASSICAL", source = "CLASSICAL"),
        @ValueMapping(target = "COUNTRY", source = "COUNTRY"),
        @ValueMapping(target = "EDM", source = "EDM"),
        @ValueMapping(target = "JAZZ", source = "JAZZ"),
        @ValueMapping(target = "OLDIES", source = "OLDIES"),
        @ValueMapping(target = "POP", source = "POP"),
        @ValueMapping(target = "RNB", source = "RNB"),
        @ValueMapping(target = "RAP", source = "RAP"),
        @ValueMapping(target = "ROCK", source = "ROCK"),
        @ValueMapping(target = "TECHNO", source = "TECHNO"),

    })
    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToCategoryDto(Category category);
}