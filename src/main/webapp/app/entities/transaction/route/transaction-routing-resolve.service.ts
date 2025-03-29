import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransaction } from '../transaction.model';
import { TransactionService } from '../service/transaction.service';

const transactionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransaction> => {
  const id = route.params.id;
  if (id) {
    return inject(TransactionService)
      .find(id)
      .pipe(
        mergeMap((transaction: HttpResponse<ITransaction>) => {
          if (transaction.body) {
            return of(transaction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transactionResolve;
