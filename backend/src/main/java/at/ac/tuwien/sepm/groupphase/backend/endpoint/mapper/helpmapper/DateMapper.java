package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;

@Mapper
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(LocalDateTime localDateTime){
        return localDateTime.atOffset(ZoneOffset.UTC);
    }

    public LocalDateTime asLocalDateTime(OffsetDateTime offsetDateTime){
        return offsetDateTime.toLocalDateTime();
    }

}
