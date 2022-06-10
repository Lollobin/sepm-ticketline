package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;

public interface ArticleService {

    /**
     * Saves an article.
     *
     * @param articleWithoutIdDto article which will be saved
     * @return ID of article
     */
    long createNewsArticle(ArticleWithoutIdDto articleWithoutIdDto);

}
