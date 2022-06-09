package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ReadArticleStatusApi;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadArticlesEndpoint implements ReadArticleStatusApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final UserService userService;


    private final AuthenticationUtil authenticationUtil;

    public ReadArticlesEndpoint(
        UserService userService, AuthenticationUtil authenticationUtil) {
        this.userService = userService;

        this.authenticationUtil = authenticationUtil;
    }

    @Override
    public ResponseEntity<Void> readArticleStatusIdPut(Long id) {

        LOGGER.info("PUT /articles with id {}", id);

        String userWhoReadArticle = authenticationUtil.getEmail();

        userService.updateArticleRead(userWhoReadArticle, id);

        return ResponseEntity.noContent().build();
    }

}
