package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Find an article based on the ID.
     *
     * @param articleId searches for article with this ID
     * @return Optional of Article
     */
    @Transactional
    @Query("select a from Article a where a.articleId = :articleId")
    Optional<Article> getArticleById(@Param("articleId") Long articleId);

    /**
     * Get all articles that have been read by a specific user.
     *
     * @param userId search for articles the user with this userId has read
     * @return List of articles
     */
    @Query("select distinct a from Article a inner join a.users users where users.userId = :userId")
    List<Article> findByUsersUserIdEquals(@Param("userId") long userId);

    /**
     * Get all articles that have not been read by a specific user.
     *
     * @param userId search for articles the user with this userId has not read
     * @return List of articles
     */
    @Query(
        "select distinct a from Article a left outer join a.users users where users.userId is null or "
            + "a.articleId not in (select a.articleId from Article a join a.users tes where tes.userId = :userId )")
    List<Article> findDistinctByUsersUserIdNot(@Param("userId") long userId);


}
