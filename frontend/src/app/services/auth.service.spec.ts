import {TestBed} from '@angular/core/testing';

import {CustomAuthService} from './custom-auth.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {ReactiveFormsModule} from '@angular/forms';

describe('AuthService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule],
  }));

  it('should be created', () => {
    const service: CustomAuthService = TestBed.inject(CustomAuthService);
    expect(service).toBeTruthy();
  });
});
