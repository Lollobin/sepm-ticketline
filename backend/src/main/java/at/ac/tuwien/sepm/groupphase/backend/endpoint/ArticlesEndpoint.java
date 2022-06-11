package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ArticlesApi;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ArticlesEndpoint implements ArticlesApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ArticleService articleService;

    private final AuthenticationUtil authenticationUtil;


    public ArticlesEndpoint(ArticleService articleService, AuthenticationUtil authenticationUtil) {
        this.articleService = articleService;

        this.authenticationUtil = authenticationUtil;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> articlesPost(ArticleWithoutIdDto articleWithoutIdDto) {

        LOGGER.info("POST /articles with body: {}", articleWithoutIdDto);

        long id = articleService.createNewsArticle(articleWithoutIdDto);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<ArticleDto>> articlesGet(Boolean filterRead) {
        LOGGER.info("GET /articles with filterRead: {}", filterRead);

        boolean isAnonym = false;

        if (authenticationUtil.getAuthentication() instanceof AnonymousAuthenticationToken) {
            isAnonym = true;
            LOGGER.trace("Not logged in user");
            if (Boolean.TRUE.equals(filterRead)) {
                LOGGER.info("can not get read articles as not logged in user");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "You need to be logged in to access this route");
            }
        }

        String userEmail = authenticationUtil.getEmail();

        List<ArticleDto> articleDtos = articleService.getArticles(filterRead, userEmail, isAnonym);

        return ResponseEntity.ok().body(articleDtos);
    }

    @Override
    public ResponseEntity<ArticleDto> articlesIdGet(Long id) {

        LOGGER.info("GET /articles/{}", id);

        ArticleDto articleDto = articleService.getArticleDtoById(id);

        return ResponseEntity.ok().body(articleDto);

    }

}
