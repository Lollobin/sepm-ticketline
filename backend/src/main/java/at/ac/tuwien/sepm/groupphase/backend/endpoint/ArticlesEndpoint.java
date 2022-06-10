package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ArticlesApi;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
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

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<List<ArticleDto>> articlesGet(Boolean filterRead) {
        LOGGER.info("GET /articles with filterRead: {}", filterRead);

        String userEmail = authenticationUtil.getEmail();

        List<ArticleDto> articleDtos = articleService.getArticles(filterRead, userEmail);

        return ResponseEntity.ok().body(articleDtos);
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<ArticleDto> articlesIdGet(Long id) {

        LOGGER.info("GET /articles/{}", id);

        ArticleDto articleDto = articleService.getArticleDtoById(id);

        return ResponseEntity.ok().body(articleDto);

    }

}
