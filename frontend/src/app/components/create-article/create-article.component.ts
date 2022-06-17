import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ArticlesService, ArticleWithoutId} from "../../generated-sources/openapi";
import {map} from "lodash";
import {firstValueFrom} from "rxjs";
import {ViewportScroller} from "@angular/common";
import {ImageCroppedEvent} from "ngx-image-cropper";

@Component({
  selector: 'app-create-article',
  templateUrl: './create-article.component.html',
  styleUrls: ['./create-article.component.scss']
})
export class CreateArticleComponent implements OnInit {

  @ViewChild("myInput")
  myInputVariable: ElementRef;
  fileToUpload: FileList | null = null;
  articleForm: FormGroup;

  error: Error;
  submitted = false;
  success = false;
  title = "";

  imageIds = [];
  previews: string[] = [];
  imgChangeEvt: any = '';
  cropImgPreview: any = '';
  fileToReturn: File = null;
  uploaded = false;
  uploadSuccess = false;
  pressed = false;
  display = "none";


  constructor(private _formBuilder: FormBuilder, private articleService: ArticlesService, private scroll: ViewportScroller) {
    this.articleForm = this._formBuilder.group({
      title: ["", [Validators.required]],
      summary: ["", [Validators.required]],
      text: ["", [Validators.required]],
      image: []
    });
  }

  ngOnInit(): void {
  }

  onCloseHandled() {
    this.display = "none";
  }

  onFileChange(event: any): void {
    this.pressed = false;
    this.imgChangeEvt = event;
    this.uploadSuccess = false;
    // console.log("img change event", this.imgChangeEvt);
  }

  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
    this.fileToReturn = this.base64ToFile(e.base64, this.imgChangeEvt.target?.files[0].name);
    // console.log("crop img preview", this.cropImgPreview);
    // console.log(this.fileToReturn);
  }

  imgLoad() {
    // display cropper tool
  }

  initCropper() {
    // init cropper
  }

  imgFailed() {
    // error msg
  }

  reset() {
    this.myInputVariable.nativeElement.value = null;
  }

  base64ToFile(data, filename) {

    const arr = data.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);

    while (n--) {
      u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, {type: mime});
  }

  // onFileChange(event) {
  //   this.fileToUpload = event.target.files;
  //   if (event.target.files && event.target.files[0]) {
  //     const file = event.target.files[0];
  //     const numberOfFiles = event.target.files.length;
  //
  //     for (let i = 0; i < numberOfFiles; i++) {
  //       const reader = new FileReader();
  //       reader.onload = (e: any) => {
  //
  //         this.previews.push(e.target.result);
  //       };
  //
  //       reader.readAsDataURL(this.fileToUpload[i]);
  //     }
  //
  //
  //   }
  // }


  helpUpload() {

    this.articleService.imagesPost(this.fileToReturn, "response").subscribe({
      next: res => {
        const location = res.headers.get("location");
        const id = location.split("/").pop();
        this.imageIds.push(id);
        this.previews.push(this.cropImgPreview);
        this.uploaded = true;
        this.uploadSuccess = true;
        this.pressed = true;
        this.fileToReturn = null;
      }
    });

  }

  async uploadImage() {


    this.previews = [];

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

    if (this.cropImgPreview) {

    }

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
            this.imageIds = [];
            this.submitted = false;
            this.success = true;
            this.uploadSuccess = false;
            this.previews = [];
            this.reset();
            this.articleForm.reset();
            this.scroll.scrollToPosition([0, 0]);


          },
          error: err1 => {
            this.success = false;
            this.imageIds = [];
            if (err1 instanceof Object) {
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

  public vanishUploadSuccess(): void {
    this.uploadSuccess = null;
  }


  removeImage(id: number) {
    console.log(this.imageIds);

    this.imageIds.splice(id, 1);
    this.previews.splice(id, 1);
  }

  openModal() {
    this.display = "block";
  }
}
