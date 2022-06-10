import {Component, OnInit} from '@angular/core';
import {Article, ArticlesService} from "../../generated-sources/openapi";
import {Router} from "@angular/router";

@Component({
  selector: 'app-news-overview',
  templateUrl: './news-overview.component.html',
  styleUrls: ['./news-overview.component.scss']
})
export class NewsOverviewComponent implements OnInit {

  articles: Article[] = [];
  error: Error;
  empty = false;
  filterRead = null;
  articleImages = {};


  constructor(public articleService: ArticlesService, private router: Router) {
  }


  ngOnInit(): void {

    this.filterRead = this.router.url.includes("read");

    console.log(this.filterRead + " ist filterread");

    this.articleService.articlesGet(this.filterRead).subscribe({
      next: async response => {
        this.articles = response;
        this.empty = response.length === 0;
        for (const article of this.articles) {

          this.getImage(article.images[0], article.articleId);


        }

        console.log(response);
      },
      error: error => {
        this.error = error;
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
