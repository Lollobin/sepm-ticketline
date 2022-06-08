import { Component, OnInit } from '@angular/core';
import {Article, ArticlesService} from "../../generated-sources/openapi";

@Component({
  selector: 'app-news-overview',
  templateUrl: './news-overview.component.html',
  styleUrls: ['./news-overview.component.scss']
})
export class NewsOverviewComponent implements OnInit {

  articles: Article[] = [];
  error: Error;

  constructor(private articleService: ArticlesService) { }



  ngOnInit(): void {

    this.articleService.articlesGet(false).subscribe({
      next: response => {
        this.articles = response;
      },
      error: error => {
        this.error = error;
      }
    });
  }

}
