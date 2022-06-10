import {Component, OnInit} from '@angular/core';
import {Article, ArticlesService} from "../../generated-sources/openapi";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-article-detailed-view',
  templateUrl: './article-detailed-view.component.html',
  styleUrls: ['./article-detailed-view.component.scss']
})
export class ArticleDetailedViewComponent implements OnInit {

  article: Article;
  id: number;
  error: Error;

  constructor(private articleService: ArticlesService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.id = params["id"];
      this.getArticleById(this.id);
    });

  }

  getArticleById(id: number) {
    this.articleService.articlesIdGet(id).subscribe({
      next: article => {
        this.article = article;
        console.log("hier");
        this.setArticleToRead(id);

        console.log(article);
      },
      error: err => {
        this.error = err;
      }
    });
  }

  setArticleToRead(id: number) {
    this.articleService.readArticleStatusIdPut(id).subscribe({
      next: () => {
        console.log("worked");
      },
      error: err => {
        this.error = err;
      }
    });
  }

}
