package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;

@Mapper
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.atOffset(ZoneId.of("Europe/Vienna").getRules().getOffset(localDateTime));
        } else {
            return null;
        }
    }

    public LocalDateTime asLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime != null) {
            return offsetDateTime.toLocalDateTime();
        } else {
            return null;
        }
    }

}
