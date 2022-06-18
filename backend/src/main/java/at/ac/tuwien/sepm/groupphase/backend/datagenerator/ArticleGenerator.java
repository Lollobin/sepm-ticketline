package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Profile("generateData")
@Component
public class ArticleGenerator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ZoneId zone = ZoneId.of("Europe/Berlin");
    private static final ZoneOffset zoneOffSet = zone.getRules().getOffset(LocalDateTime.now());
    private final ArticleRepository articleRepository;
    private final Faker faker = new Faker();
    private final ImageGenerator imageGenerator;
    private final Random numGenerator = new Random();
    private final ImageRepository imageRepository;


    public ArticleGenerator(
            ArticleRepository artistRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.articleRepository = artistRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }


    public void generateData(int numberOfArticles) throws IOException {
        if (!articleRepository.findAll().isEmpty()) {
            LOGGER.debug("articles already generated");
            return;
        }

        LOGGER.debug("generating {} articles", numberOfArticles);

        for (int i = 0; i < numberOfArticles; i++) {
            Article article = generateArticle();
            LOGGER.trace("saving article {}", article);

            articleRepository.save(article);
        }
    }

    private Article generateArticle() throws IOException {


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Article article = new Article();
        article.setCreationDate(faker.date().between(cal.getTime(), new Date()).toInstant().atOffset(zoneOffSet));
        article.setTitle(faker.lorem().sentence());
        article.setSummary(faker.lorem().paragraph(3));
        article.setText(String.valueOf(faker.lorem().paragraphs(14)));


        int numOfImages = numGenerator.nextInt(4);

        List<Long> imageIds = imageGenerator.generateData(numOfImages);

        List<Image> imagesList = imageRepository.findAllById(imageIds);

        article.setImages(imagesList);

        Article savedArticle = articleRepository.save(article);

        for (long i : imageIds) {
            Image image = imageRepository.findById(i).get();
            image.setArticle(savedArticle);
            imageRepository.save(image);

        }


        return article;

    }


}
