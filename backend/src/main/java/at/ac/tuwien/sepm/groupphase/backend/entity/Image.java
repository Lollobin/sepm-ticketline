package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Article article;

    @Override
    public String toString() {
        return "Image{"
            + "imageId="
            + imageId
            + ", filePath='"
            + filePath
            + '\''
            + ", article="
            + article
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return imageId == image.imageId
            && Objects.equals(filePath, image.filePath)
            && Objects.equals(article, image.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, filePath, article);
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
