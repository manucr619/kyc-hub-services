import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IKycDataRequest } from '../kyc-data-request.model';
import { KycDataRequestService } from '../service/kyc-data-request.service';

const kycDataRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IKycDataRequest> => {
  const id = route.params.id;
  if (id) {
    return inject(KycDataRequestService)
      .find(id)
      .pipe(
        mergeMap((kycDataRequest: HttpResponse<IKycDataRequest>) => {
          if (kycDataRequest.body) {
            return of(kycDataRequest.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default kycDataRequestResolve;
