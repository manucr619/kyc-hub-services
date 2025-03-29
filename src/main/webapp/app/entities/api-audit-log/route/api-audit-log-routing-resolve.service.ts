import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPIAuditLog } from '../api-audit-log.model';
import { APIAuditLogService } from '../service/api-audit-log.service';

const aPIAuditLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IAPIAuditLog> => {
  const id = route.params.id;
  if (id) {
    return inject(APIAuditLogService)
      .find(id)
      .pipe(
        mergeMap((aPIAuditLog: HttpResponse<IAPIAuditLog>) => {
          if (aPIAuditLog.body) {
            return of(aPIAuditLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aPIAuditLogResolve;
