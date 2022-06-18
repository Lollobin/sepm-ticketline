package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticlePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    /**
     * Saves an article.
     *
     * @param articleWithoutIdDto article which will be saved
     * @return ID of article
     */
    long createNewsArticle(ArticleWithoutIdDto articleWithoutIdDto);


    /**
     * Get an ArticleDto based on the ID.
     *
     * @param id searches for the articles with the corresponding ID
     * @return an ArticleDto
     */
    ArticleDto getArticleDtoById(Long id);

    /**
     * Gets articles based on filterRead.
     *
     * @param filterRead if false then all articles are returned, if true then articles that have
     *                   been read by user with corresponding email are being returned
     * @param email      used when filterRead is set to true then returns articles that the user
     *                   with the associated email has read
     * @return List of ArticleDtos
     */
    ArticlePageDto getArticles(Boolean filterRead, String email, boolean isAnonym, Pageable pageable);


}
