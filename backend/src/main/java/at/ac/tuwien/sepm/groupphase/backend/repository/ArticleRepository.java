package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    /**
     * Get all articles that have been read by a specific user.
     *
     * @param userId search for articles the user with this userId has read
     * @return List of articles
     */
    @Query("select distinct a from Article a inner join a.users users where users.userId = :userId")
    List<Article> findArticlesReadByUser(@Param("userId") long userId);

    /**
     * Get all articles that have not been read by a specific user.
     *
     * @param userId search for articles the user with this userId has not read
     * @return List of articles
     */
    @Query(
        "select distinct a from Article a left outer join a.users users where users.userId is null or "
            + "a.articleId not in (select a.articleId from Article a join a.users tes where tes.userId = :userId )")
    List<Article> findArticlesNotReadByUser(@Param("userId") long userId);


}
