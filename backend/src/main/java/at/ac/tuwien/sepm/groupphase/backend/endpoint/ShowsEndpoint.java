package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowsApi;
import org.springframework.http.ResponseEntity;

public class ShowsEndpoint implements ShowsApi {

    @Override
    public ResponseEntity<ShowDto> showsIdGet(Integer id) {
        return ShowsApi.super.showsIdGet(id);
    }
}
