import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ArticlesService, ArticleWithoutId} from "../../generated-sources/openapi";
import {map} from "lodash";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {


  fileToUpload: FileList | null = null;
  articleForm: FormGroup;

  error: Error;
  submitted = false;
  success = false;
  title = "";

  imageIds = [];


  constructor(private _formBuilder: FormBuilder, private articleService: ArticlesService) {
    this.articleForm = this._formBuilder.group({
      title: ["", [Validators.required]],
      summary: [""],
      text: [""],
      image: []
    });
  }

  ngOnInit(): void {
  }

  onFileChange(event) {
    this.fileToUpload = event.target.files;
  }

  async uploadImage() {

    this.imageIds = [];

    this.submitted = false;

    const promises = map(this.fileToUpload, file =>
        firstValueFrom(this.articleService.imagesPost(file, "response")));


    const results = await Promise.all(promises);

    results.forEach(res => {
      const location = res.headers.get("location");
      const id = location.split("/").pop();
      this.imageIds.push(id);

    });


  }

  async createArticle() {

    try {

      if (this.fileToUpload?.length > 0) {

        await this.uploadImage();
      }
      this.submitted = true;

      if (this.articleForm.valid && (this.fileToUpload === null || this.fileToUpload.length > 0)) {

        const form = this.articleForm.value;

        this.title = form.title;

        const article: ArticleWithoutId = {
          title: form.title,
          summary: form.summary,
          text: form.text,
          images: this.imageIds
        };


        this.articleService.articlesPost(article).subscribe({
          next: () => {

            this.submitted = false;
            this.success = true;


          },
          error: err1 => {
            this.success = false;
            if(err1 instanceof Object){
              this.error = err1.error;
            } else {

              this.error = err1;
            }
          }
        });

      }
    } catch (error) {
      this.success = false;
      this.error = error;
    }
  }

  public vanishSuccess(): void {
    this.success = null;
  }


}
