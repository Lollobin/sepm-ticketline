package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ArticlesApi;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import java.lang.invoke.MethodHandles;
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


    public ArticlesEndpoint(ArticleService articleService) {
        this.articleService = articleService;

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
}
