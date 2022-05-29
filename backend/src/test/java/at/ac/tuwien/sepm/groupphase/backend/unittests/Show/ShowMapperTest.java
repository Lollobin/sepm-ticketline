package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ShowMapperTest {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowMapper showMapper;

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_location.sql"),
        @Sql("classpath:/sql/insert_seatingPlanLayout.sql"),
        @Sql("classpath:/sql/insert_seatingPlan.sql"), @Sql("classpath:/sql/insert_sector.sql"),
        @Sql("classpath:/sql/insert_event.sql"), @Sql("classpath:/sql/insert_show.sql"),
        @Sql("classpath:/sql/insert_sectorPrice.sql"),})
    void showToShowDto_shouldMapLocationCorrectly() {

        Show showEntity = showRepository.getByShowId(-1L);

        ShowDto showDto = showMapper.showToShowDto(showEntity);

        assertThat(showDto.getShowId()).isEqualTo(showEntity.getShowId());
        assertThat(showDto.getDate()).isEqualTo(showEntity.getDate());
        assertThat(showDto.getEvent().getEventId()).isEqualTo(showEntity.getEvent().getEventId());
        assertThat(showDto.getLocation().getLocationId()).isEqualTo(-1L);

    }

}
