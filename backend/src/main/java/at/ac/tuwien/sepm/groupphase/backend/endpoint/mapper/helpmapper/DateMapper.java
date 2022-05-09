package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;

@Mapper
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(LocalDate localDate) {
        if (localDate != null) {
            return OffsetDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneOffset.UTC);
        } else {
            return null;
        }
    }

    public LocalDate asLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime != null) {
            return offsetDateTime.toLocalDate();
        } else {
            return null;
        }
    }

}
