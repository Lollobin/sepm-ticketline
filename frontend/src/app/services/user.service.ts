import { Injectable } from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../generated-sources/openapi";

const baseUri = "localhost:4200" + "/user";

@Injectable({
  providedIn: "root"
})
export class UserService {

  constructor(private http: HttpClient) { }

  getLockedUser(): Observable<User[]> {
    const asHttpParam = new URLSearchParams();
    asHttpParam.set("locked", "true");
    return this.http.get<User[]>(baseUri + "/blocked-state");
  }

  unlockUser(id: number): Observable<User> {
    // const json = {isLocked: false};
    return this.http.patch<User>(baseUri + "&" + id, null);
  }


}
