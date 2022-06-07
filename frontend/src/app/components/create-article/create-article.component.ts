import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ArticlesService, ArticleWithoutId} from "../../generated-sources/openapi";
import {HttpResponse} from "@angular/common/http";
import {resolve} from "@angular/compiler-cli";
import {reject} from "lodash";

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
  uploaded = false;

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


    console.log("onfilechange", this.fileToUpload);
  }

  async uploadImage() {

      this.imageIds = [];

      this.submitted = false;




      // eslint-disable-next-line @typescript-eslint/prefer-for-of
      for (let i = 0; i < this.fileToUpload.length; i++) {

        this.articleService.imagesPost(this.fileToUpload[i], "response").subscribe({
          next: (res: HttpResponse<any>) => {
            console.log(i);
            this.uploaded = true;

            const location = res.headers.get("location");
            const id = location.split("/").pop();
            this.imageIds.push(id);


          },
          error: err1 => {
            console.log("error", err1);
            this.uploaded = false;
          }
        });


        // return new Promise((resolve1, reject1) => {
        //   // console.log("so viele werden gespeichert", this.fileToUpload);
        //
        //   this.imageIds = [];
        //
        //   // eslint-disable-next-line @typescript-eslint/prefer-for-of
        //   for (let i = 0; i < this.fileToUpload.length; i++) {
        //
        //     this.articleService.imagesPost(this.fileToUpload[i], "response").subscribe({
        //       next: (res: HttpResponse<any>) => {
        //         console.log(i);
        //
        //         const location = res.headers.get("location");
        //         const id = location.split("/").pop();
        //         this.imageIds.push(id);
        //         this.uploaded = true;
        //
        //         if(i === this.fileToUpload.length-1){
        //           // console.log(i);
        //           // console.log(this.imageIds);
        //           resolve1("finished");
        //         }
        //
        //
        //
        //
        //
        //       },
        //       error: err1 => {
        //         console.log("error", err1);
        //         reject1(err1);
        //       }
        //     });
        //
        //   }
        //
        //
        //
        // });

      }
  }

  createArticle() {

    // if(this.fileToUpload?.length > 0){
    //   // console.log("in await");
    //    await this.uploadImage();
    // }
    this.submitted = true;

    if (this.articleForm.valid && (this.fileToUpload === null || this.fileToUpload.length > 0)) {

      const form = this.articleForm.value;

      // console.log("imageids vor const article", this.imageIds);

      const article: ArticleWithoutId = {
        title: form.title,
        summary: form.summary,
        text: form.text,
        images: this.imageIds
      };
      this.imageIds = [];

      // console.log("article to save", article);

      this.articleService.articlesPost(article).subscribe({
        next: () => {

          this.submitted = false;
          this.uploaded = false;

          // console.log("successfully created article");
        },
        error: err1 => {
          this.error = err1;
        }
      });

    }
  }

}
