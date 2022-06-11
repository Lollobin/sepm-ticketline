import {Component, OnInit} from '@angular/core';
import {Article, ArticlesService} from "../../generated-sources/openapi";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  articles: Article[];
  error: Error;
  articleImages = {};
  empty = false;
  filterRead = null;
  defaultImage = 'https://dummyimage.com/640x360/fff/aaa';
  errorImage = 'https://mdbcdn.b-cdn.net/img/new/standard/city/053.webp';

  constructor(private articleService: ArticlesService) {
  }

  ngOnInit() {
    this.getArticles();
  }

  getArticles() {
    this.articleService.articlesGet(false).subscribe({
      next: articles => {

        this.articles = articles.slice(0, 6);
        this.empty = articles.length === 0;
        for (const article of this.articles) {

          this.getImage(article.images[0], article.articleId);
          if (article.images?.length === 0) {
            this.articleImages[article.articleId] = this.errorImage;
          }
        }
      },
      error: err => {
        this.error = err;
      }
    });
  }

  createImageFromBlob(image: Blob, id: number) {
    const reader = new FileReader();
    reader.addEventListener("load", () => {
      this.articleImages[id] = reader.result;

    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }


  getImage(id: number, articleId: number) {

    if (!id) {
      return;
    }

    this.articleService.imagesIdGet(id).subscribe({
      next: image => {
        this.createImageFromBlob(image, articleId);
      },
    });
  }

  public vanishEmpty(): void {
    this.empty = null;
  }

}
