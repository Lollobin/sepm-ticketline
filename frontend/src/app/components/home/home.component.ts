import {Component, OnInit} from '@angular/core';
import {CustomAuthService} from '../../services/custom-auth.service';
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

  constructor(public authService: CustomAuthService, private articleService: ArticlesService) { }

  ngOnInit() {
    this.getArticles();
  }

  getArticles(){
    this.articleService.articlesGet(false).subscribe({
      next: articles => {

        this.articles = articles.slice(0,5);
        for (const article of this.articles) {

          this.getImage(article.images[0], article.articleId);


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

}
