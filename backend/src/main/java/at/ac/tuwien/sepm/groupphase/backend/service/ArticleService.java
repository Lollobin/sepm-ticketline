package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;

public interface ArticleService {

    /**
     * Saves a new article.
     *
     * @param articleWithoutIdDto article which will be saved
     */
    void createNewsArticle(ArticleWithoutIdDto articleWithoutIdDto);

}
