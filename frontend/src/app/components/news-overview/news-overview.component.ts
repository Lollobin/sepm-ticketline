import {Component, OnInit} from '@angular/core';
import {Article, ArticlePage, ArticlesService, Sort} from "../../generated-sources/openapi";
import {Router} from "@angular/router";
import {CustomAuthService} from "../../services/custom-auth.service";
import {ViewportScroller} from "@angular/common";

@Component({
  selector: 'app-news-overview',
  templateUrl: './news-overview.component.html',
  styleUrls: ['./news-overview.component.scss']
})
export class NewsOverviewComponent implements OnInit {

  articles: Article[] = [];
  articleResult: ArticlePage;
  error: Error;
  empty = false;
  filterRead = null;
  articleImages = {};
  defaultImage = 'https://dummyimage.com/640x360/fff/aaa';
  errorImage = 'https://mdbcdn.b-cdn.net/img/new/standard/city/053.webp';
  pageSize = 8;
  sort: 'ASC' | 'DESC' = 'ASC';
  page = 1;


  constructor(public articleService: ArticlesService, private router: Router,
              public authService: CustomAuthService, private scroll: ViewportScroller) {
  }


  ngOnInit(): void {

    this.filterRead = this.router.url.includes("read");
    this.reloadArticles(this.filterRead);
  }

  reloadArticles(filterRead: boolean) {

    this.filterRead = filterRead;

    this.articleService.articlesGet(this.filterRead, this.pageSize, this.page - 1, Sort.Desc).subscribe({
      next: async articlePage => {
        this.articleResult = articlePage;
        this.articles = articlePage.articles;
        this.empty = articlePage.articles.length === 0;
        for (const article of this.articles) {

          this.getImage(article.images[0], article.articleId);

          if (article.images?.length === 0) {
            this.articleImages[article.articleId] = this.errorImage;
          }


        }
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

  onPageChange(ngbpage: number) {
    this.page = ngbpage;
    this.reloadArticles(this.filterRead);
    this.scroll.scrollToPosition([0, 0]);

  }


  public vanishEmpty(): void {
    this.empty = null;
  }

}
