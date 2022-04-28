package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(LocalDateTime localDateTime){
        return localDateTime.atOffset(ZoneId.of("Europe/Vienna").getRules().getOffset(localDateTime));
    }

    public LocalDateTime asLocalDateTime(OffsetDateTime offsetDateTime){
        return offsetDateTime.toLocalDateTime();
    }

}
