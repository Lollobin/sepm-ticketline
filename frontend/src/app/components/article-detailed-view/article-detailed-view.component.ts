import {Component, OnInit} from '@angular/core';
import {Article, ArticlesService} from "../../generated-sources/openapi";
import {ActivatedRoute} from "@angular/router";
import {CustomAuthService} from "../../services/custom-auth.service";
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-article-detailed-view',
  templateUrl: './article-detailed-view.component.html',
  styleUrls: ['./article-detailed-view.component.scss']
})
export class ArticleDetailedViewComponent implements OnInit {

  article: Article;
  id: number;
  articleImages = {};
  errorImage = 'https://mdbcdn.b-cdn.net/img/new/standard/city/053.webp';

  constructor(private articleService: ArticlesService, private activatedRoute: ActivatedRoute,
              private customAuthService: CustomAuthService, private toastrService: ToastrService) {
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

        if (this.customAuthService.isLoggedIn()) {
          this.setArticleToRead(id);
        }


        for(let i = 0; i < this.article.images.length; i++){

          this.getImage(article.images[i], i);
        }

        if(article.images?.length === 0){
          this.articleImages[0] = this.errorImage;
        }
      },
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastrService.error(error.message);
        } else {
          this.toastrService.warning(error.error);
        }
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


  getImage(id: number, position: number) {

    if (!id) {
      return;
    }

    this.articleService.imagesIdGet(id).subscribe({
      next: image => {
        this.createImageFromBlob(image, position);
      }, 
      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastrService.error(error.message);
        } else {
          this.toastrService.warning(error.error);
        }
      }
    });
  }

  setArticleToRead(id: number) {
    this.articleService.readArticleStatusIdPut(id).subscribe({

      error: (error) => {
        console.log(error);
        if (error.status === 0 || error.status === 500) {
          this.toastrService.error(error.message);
        } else {
          this.toastrService.warning(error.error);
        }
      }
    });
  }

}
