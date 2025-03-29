import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

const customerResolve = (route: ActivatedRouteSnapshot): Observable<null | ICustomer> => {
  const id = route.params.id;
  if (id) {
    return inject(CustomerService)
      .find(id)
      .pipe(
        mergeMap((customer: HttpResponse<ICustomer>) => {
          if (customer.body) {
            return of(customer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default customerResolve;
