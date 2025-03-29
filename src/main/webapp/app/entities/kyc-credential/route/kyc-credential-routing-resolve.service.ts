import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycCredential } from '../kyc-credential.model';
import { KycCredentialService } from '../service/kyc-credential.service';

const kycCredentialResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycCredential> => {
  const id = route.params.id;
  if (id) {
    return inject(KycCredentialService)
      .find(id)
      .pipe(
        mergeMap((kycCredential: HttpResponse<IKycCredential>) => {
          if (kycCredential.body) {
            return of(kycCredential.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycCredentialResolve;
