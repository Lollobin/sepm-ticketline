package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import com.github.javafaker.Faker;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class ArticleGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private static final ZoneId zone = ZoneId.of("Europe/Berlin");
    private static final ZoneOffset zoneOffSet = zone.getRules().getOffset(LocalDateTime.now());
    private final ArticleRepository articleRepository;
    private final Faker faker = new Faker();
    private final ImageGenerator imageGenerator;
    private final Random numGenerator = new Random();
    private final ImageRepository imageRepository;


    public ArticleGenerator(ArticleRepository artistRepository, ImageGenerator imageGenerator,
        ImageRepository imageRepository) {
        this.articleRepository = artistRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }

    private List textStrings = List.of(
        new String[]{"The event will be cool I think.", "Many people will be there.",
            "No one will attend, if some artists are not showing up.",
            "This will be the best event ever and the shows will be great!",
            "The price will be high, and someone will still buy it.",
            "But will there be artists that make sense?",
            "Empty statements lead to high attendance rates.",
            "They will make a great job of organising the event!",
            "If you laugh at this event, you will have to pay an extra price.",
            "We will, we will, we will rock you!", "The band is playing in a good mood today.",
            "The band was the worst part of the evening",
            "I liked all of the bands that played, but the first was the best.",
            "What to do, what not to do...", "It cost me a fortune, but it was worth it!",
            "Almost as good as Fyre festival, but I liked it more.",
            "Start me up, I'm definetl in for this one!", "Give me that, I want it!",
            "I thought the vibes were great!", "Only good moods, please don't ruin them...",
            "Was that a real thing? For sure!", "Prices will rise, but please still buy it!"});

    private List<String> fullTitles = List.of(
        new String[]{"Back to the future is Back!", "Another one for Disney fans",
            "The Great Gatsby", "Wicked: The musical", "Phantom haunts the halls again",
            "My fair Lady in theaters now!", "Book of Mormon - it's nice", "Jersey Boys"});

    private String[][] imagePaths = new String[][]{
        new String[]{"bttf1.jpeg", "bttf2.jpeg"},
        new String[]{"lk1.jpeg"},
        new String[]{"gg1.jpeg", "gg2.jpeg"},
        new String[]{"w1.jpeg"},
        new String[]{"ph1.jpeg", "ph2.jpeg"},
        new String[]{"mfl1.jpeg"},
        new String[]{"bom1.jpeg"},
        new String[]{"jb1.jpeg"}
    };
    private List<String> fullTexts = List.of(new String[]{

        "Welcome to Hill Valley! Take an electrifying ride back in time as the 1985 "
            + "blockbuster film and pop culture phenomenon arrives in London's West End as a groundbreaking new musical adventure!\n"
            + "\n"
            + "When Marty McFly finds himself transported back to 1955 in a time machine built by the eccentric scientist Doc Brown,"
            + " he accidentally changes the course of history. Now he’s in a race against time to fix the present, escape the past and send himself... back to the future.\n"
            + "\n"
            + "Adapting this iconic story for the stage are the movie’s creators Bob Gale (Back to the Future trilogy) and Robert Zemeckis (Forrest Gump). The production features\n"
            + "original music by multi-Grammy winners\n"
            + "Alan Silvestri (Avengers: Endgame) and Glen Ballard (Michael Jackson’s Man in the Mirror),\n"
            + "alongside hit songs from the movie including The Power of Love, Johnny B Goode, Earth Angel and Back in Time.\n"
            + "\n"
            + "Tony Award-winning director John Rando leads the Tony and Olivier Award-winning creative team..\n"
            + "\n" + "Strap yourself in for a thrilling theatrical experience!\n" + "\n"
            + "When Back To The Future: The Musical hits 88mph, you’re gonna see some serious… entertainment.\n"
            + "\n" + "\n" + "Book your\n" + "tickets yesterday!",
        "From Hollywood to the West End, The Lion King has been an enduring success since 1994. "
            + "Taking the famous story of Simba and his turbulent ascension to king, the stage show is a one-way ticket to the Pride Lands. "
            + "With mesmeric scenery drawing you in, you'll almost feel part of the action as you journey through Simba's world. "
            + "To bring The Lion King to life, the show's original director, Julie Taymor, combined live performers and innovative props. "
            + "Creating a visual feast that's since redefined how musicals could and should look, The Lion King really is an all-singing, all-dancing affair.\n"
            + "\n" + "\n" + "Why is the Lion King a Roaring Success\n" + "\n"
            + "Since it debuted in 1997 at the Orpheum Theatre in Minneapolis, Minnesota, The Lion King has become the highest-grossing musical in history."
            + " As well as breaking Broadway running records, the show has taken the West End by storm. Today, with more than £8 billion grossed internationally, The Lion King is an undeniable success. "
            + "In fact, even for those that aren't inspired by the story of Simba, the show remains a musical hit. Why?"
            + " For many return viewers, the staging and styling are simply unrivalled. According to The Guardian's Michael Billington, the show remains \"kaleidoscopically brilliant\" to this day. "
            + "With the heart of the Serengeti beating in time with musical magic from Elton John, Lebo M and Tim Rice, audiences can't help but feel part of the action. "
            + "Indeed, as birds swoop down the aisles and giraffes glide across the stage, everyone is immersed in this Disney fantasy.",
        "Welcome back to the roaring twenties! Jay Gatsby invites you to one of his infamous parties."
            + " the champagne flows and as the drama unfolds the man himself will be the perfect host. "
            + "As invites go, this is the hottest ticket in town. A hedonistic world of red hot rhythms, bootleg liquor and pure jazz age self-indulgence awaits. "
            + "Dress to the nines and immerse yourself in this heart racing adaption of F Scott Fitzgerald's seminal tale. \n"
            + "\n"
            + "Olivier Award-winning producers Hartshorn - Hook Productions, bring back London's longest running immersive theatrical production. "
            + "The Great Gatsby, directed by Alexander Wright, will re-open in the heart of the West End following all current government guidelines and Covid safety measures.",
        "It's been described as The Wizard of Oz from the witches' perspective, but Wicked is so much more than just that. "
            + "A hit in its own right, this riotous show has taken stages around the world by storm thanks to its fanatical script and deep, interwoven storyline. "
            + "You're thrust into the early lives of two witches, Glinda and Elphaba. The former is the type of beautiful, superficial girl you'd see in the average high school teen drama. "
            + "The latter is green, misunderstood and misrepresented. "
            + "As their journey through school and out into the magical world of Oz unfolds, we're told Glinda is the Good Witch of the South. "
            + "However, are we really meant to believe that Wicked Witch of the West is a fitting title for Elphaba? Therein lies the crux of Wicked and its widespread, relentless appeal. "
            + "By subverting expectations and taking you on a magical ride, Stephen Schwartz and Winnie Holzman have created a truly captivating show. "
            + "Indeed, with records, box office receipts and awards to its name, Wicked is a modern musical that's already become a classic.",
        "Andrew Lloyd Webber’s The Phantom of the Opera has returned to its home, Her Majesty’s Theatre and is now celebrating it’s 35th anniversary in London’s West End.\n"
            + "\n"
            + "Experience the thrill of the West End’s most haunting love story, starring double Olivier Award nominee Killian Donnelly as The Phantom, Lucy St. Louis as Christine Daaé and Rhys Whitfield as Raoul.\n"
            + "\n"
            + "The Phantom of the Opera is widely considered one of the most beautiful and spectacular productions in history, playing to over 145 million people in 41 countries and 183 cities in 17 languages. "
            + "Andrew Lloyd Webber’s romantic, haunting and soaring score includes Music of the Night, All I Ask of You, Wishing You Were Somehow Here Again, Masquerade and the iconic title song.",
        "The Lincoln Center Theater’s critically acclaimed, multi-award-winning production of My Fair Lady comes to London in Summer 2022, the first major West End revival of Lerner & Loewe’s much-loved musical for 21 years.\n"
            + "\n"
            + "This 16-week engagement at the London Coliseum is a truly loverly way to celebrate London’s theatre scene back in bloom!\n"
            + "\n"
            + "My Fair Lady musical tells the story of Eliza Doolittle, a young Cockney flower seller, and Henry Higgins, a linguistics professor who is determined to transform her into his idea of a “proper lady”. "
            + "But who is really being transformed?\n"
            + "\n"
            + "Amara Okereke, who won the Stage Debut Award for her performance as Cosette in Les Misérables, plays Eliza. "
            + "In the role of Henry Higgins is Harry Haddon-Paton. He received a Tony Award nomination for his performance on Broadway. He is best known for his roles in The Crown and Downton Abbey.\n"
            + "\n"
            + "Stage legend Dame Vanessa Redgrave will play Mrs Higgins. Redgrave is returning to the West End stage for the first time since 2018. "
            + "Joining her will be Maureen Beattie who will play Mrs Pearce and Sharif Afifi who will play Freddie Eynesford-Hill.\n"
            + "\n"
            + "Directed by Bartlett Sher, this sublime production will feature the English National Opera orchestra playing Frederick Loewe’s ravishing score and a book and lyrics by Alan Jay Lerner.\n"
            + "\n"
            + "The incredible score includes the classic My Fair Lady songs I Could Have Danced All Night, Get Me to the Church on Time, Wouldn’t It Be Loverly, "
            + "On the Street Where You Live, The Rain in Spain, and I’ve Grown Accustomed to Her Face.\n"
            + "\n"
            + "Book your theatre tickets now to experience this timeless American musical for yourself!",
        "The hilarious Broadway musical from South Park creators Trey Parker and Matt Stone and Avenue Q co-creator Robert Lopez.\n"
            + "\n"
            + "The Book of Mormon was an instant sensation when it landed in the West End in 2013. "
            + "A sell-out winner of most major Broadway awards in its first year, it is now playing at London's Prince of Wales Theatre just by Leicester Square.\n"
            + "\n"
            + "The Book of Mormon  skillfully combines controversial humour with a traditional musical-theatre style, as well as a surprising amount of affection for its subject matter.\n"
            + "\n"
            + "Telling the story of two young and naive Mormon missionaries as they are sent out from Salt Lake City to find converts in Uganda, "
            + "The Book of Mormon is a sharp, savage and satirical show, with an intelligent message, not to mention great songs and performances, and a fair amount of filthy humour.\n"
            + "\n"
            + "The Book of Mormon has won nine Tony Awards, a Grammy and four Olivier Awards, as well as almost universally glowing reviews, from critics and audiences alike. \n",
        "They were just four guys from Jersey, until they sang their very first note. "
            + "They had a sound nobody had ever heard… and the radio just couldn’t get enough of. "
            + "But while their harmonies were perfect on stage, off stage it was a very different story — a story that has made them an international sensation all over again.\n"
            + "\n"
            + "Go behind the music and inside the story of Frankie Valli and The Four Seasons in the Tony Award®-winning true-life musical phenomenon, JERSEY BOYS. "
            + "From the streets of New Jersey to the Rock and Roll Hall of Fame, this is the musical that’s just too good to be true. "});

    private String generateText(int numberOfLines) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < numberOfLines; i++) {
            stringBuilder.append(textStrings.get(random.nextInt(textStrings.size())));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public void generateData(int numberOfArticles) throws IOException {
        if (!articleRepository.findAll().isEmpty()) {
            LOGGER.debug("articles already generated");
            return;
        }

        LOGGER.debug("generating {} articles", numberOfArticles);
        for (int i = 0; i < fullTexts.size(); i++) {
            Article article = generateCustomArticle(fullTitles.get(i),
                fullTexts.get(i).split("\\.")[0],
                fullTexts.get(i), faker.date()
                    .between(Date.from(OffsetDateTime.now().minusDays(2).toInstant()), new Date())
                    .toInstant().atOffset(zoneOffSet), imagePaths[i]);
            LOGGER.trace("saving article {}", article);

            articleRepository.save(article);
        }
        for (int i = fullTexts.size(); i < numberOfArticles; i++) {
            Article article = generateArticle(faker.book().title(), generateText(3),
                generateText(30), faker.date()
                    .between(Date.from(OffsetDateTime.now().minusDays(100).toInstant()),
                        Date.from(OffsetDateTime.now().minusDays(10).toInstant())).toInstant()
                    .atOffset(zoneOffSet));
            LOGGER.trace("saving article {}", article);

            articleRepository.save(article);
        }
    }

    private Article generateCustomArticle(String title, String summary, String text,
        OffsetDateTime date, String[] images) {

        Article article = new Article();
        article.setCreationDate(date);
        article.setTitle(title);
        article.setSummary(summary);
        article.setText(text);
        List<Long> imageIds = imageGenerator.generateSpecificImages(images);

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

    private Article generateArticle(String title, String summary, String text, OffsetDateTime date)
        throws IOException {

        Article article = new Article();
        article.setCreationDate(date);
        article.setTitle(title);
        article.setSummary(summary);
        article.setText(text);

        int numOfImages = numGenerator.nextInt(1, 4);

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
