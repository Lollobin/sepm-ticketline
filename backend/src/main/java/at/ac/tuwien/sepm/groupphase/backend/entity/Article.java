package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(columnDefinition = "CLOB")
    private String summary;

    @Column(columnDefinition = "CLOB")
    private String text;

    @ManyToMany
    @JoinTable(
        name = "ReadArticle",
        joinColumns = @JoinColumn(name = "articleId"),
        inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<ApplicationUser> users;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        return articleId == article.articleId
            && Objects.equals(title, article.title)
            && Objects.equals(creationDate, article.creationDate)
            && Objects.equals(summary, article.summary)
            && Objects.equals(text, article.text)
            && Objects.equals(users, article.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, title, creationDate, summary, text, users);
    }

    @Override
    public String toString() {
        return "Article{"
            + "articleId="
            + articleId
            + ", title='"
            + title
            + '\''
            + ", creationDate="
            + creationDate
            + ", summary='"
            + summary
            + '\''
            + ", text='"
            + text
            + '\''
            + ", users="
            + users
            + '}';
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<ApplicationUser> getUsers() {
        return users;
    }

    public void setUsers(Set<ApplicationUser> users) {
        this.users = users;
    }
}
