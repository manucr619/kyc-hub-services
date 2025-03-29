import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountryRegulation } from '../country-regulation.model';
import { CountryRegulationService } from '../service/country-regulation.service';

const countryRegulationResolve = (route: ActivatedRouteSnapshot): Observable<null | ICountryRegulation> => {
  const id = route.params.id;
  if (id) {
    return inject(CountryRegulationService)
      .find(id)
      .pipe(
        mergeMap((countryRegulation: HttpResponse<ICountryRegulation>) => {
          if (countryRegulation.body) {
            return of(countryRegulation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default countryRegulationResolve;
