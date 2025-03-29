import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycConsent } from '../kyc-consent.model';
import { KycConsentService } from '../service/kyc-consent.service';

const kycConsentResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycConsent> => {
  const id = route.params.id;
  if (id) {
    return inject(KycConsentService)
      .find(id)
      .pipe(
        mergeMap((kycConsent: HttpResponse<IKycConsent>) => {
          if (kycConsent.body) {
            return of(kycConsent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycConsentResolve;
