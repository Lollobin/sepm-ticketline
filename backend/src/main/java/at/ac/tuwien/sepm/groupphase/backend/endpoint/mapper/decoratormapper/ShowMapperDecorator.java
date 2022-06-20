package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.decoratormapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class ShowMapperDecorator implements ShowMapper {

    @Autowired
    @Qualifier("delegate")
    private ShowMapper delegate;
    @Autowired
    private LocationMapper locationMapper;

    @Override
    public ShowDto showToShowDto(Show show) {
        ShowDto showDto = delegate.showToShowDto(show);

        BigDecimal minPrice = show.getSectorPrices().stream().map(SectorPrice::getPrice)
           .min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);

        LocationDto locationDto = null;
        if (show.getSectorPrices().iterator().hasNext()) {
            locationDto = locationMapper.locationToLocationDto(
                show.getSectorPrices().iterator().next().getSector().getSeatingPlan()
                    .getLocation());
        }

        showDto.setLocation(locationDto);
        showDto.setMinPrice(minPrice);
        return showDto;
    }

}
