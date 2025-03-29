import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBank } from '../bank.model';
import { BankService } from '../service/bank.service';

const bankResolve = (route: ActivatedRouteSnapshot): Observable<null | IBank> => {
  const id = route.params.id;
  if (id) {
    return inject(BankService)
      .find(id)
      .pipe(
        mergeMap((bank: HttpResponse<IBank>) => {
          if (bank.body) {
            return of(bank.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bankResolve;
