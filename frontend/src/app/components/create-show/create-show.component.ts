import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Show } from 'src/app/generated-sources/openapi';
import { ShowService } from 'src/app/services/show.service';

@Component({
  selector: 'app-create-show',
  templateUrl: './create-show.component.html',
  styleUrls: ['./create-show.component.scss']
})
export class CreateShowComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private showService: ShowService, private route: ActivatedRoute) { }

  id: number;

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params["id"];
      this.showForm.value.id = this.id;
      console.log(this.id);
    });


  }

  showForm = this.formBuilder.group({
    date: ['', [Validators.required]],
    time: ['', [Validators.required]],
    event: []

  });

  createShow(): void{
    this.showForm.value.event = this.id;
    this.showForm.value.date = this.showForm.value.date + "T" + this.showForm.value.time + ":00+00:00";
    this.showService.createEvent(this.showForm.value).subscribe(
      (res: HttpResponse<Show>) => {
        console.log("Succesfully created show");
        console.log(res.headers.get('Location'));
      },
      error => {
        console.log(error.message);
      }
    );
  }

  get date(){
    return this.showForm.get("date");
  }

  get time(){
    return this.showForm.get("time");
  }

  get artists(){
    return this.showForm.get("artists");
  }


}
