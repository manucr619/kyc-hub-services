import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAPIAuditLog, NewAPIAuditLog } from '../api-audit-log.model';

export type PartialUpdateAPIAuditLog = Partial<IAPIAuditLog> & Pick<IAPIAuditLog, 'id'>;

type RestOf<T extends IAPIAuditLog | NewAPIAuditLog> = Omit<T, 'eventTime'> & {
  eventTime?: string | null;
};

export type RestAPIAuditLog = RestOf<IAPIAuditLog>;

export type NewRestAPIAuditLog = RestOf<NewAPIAuditLog>;

export type PartialUpdateRestAPIAuditLog = RestOf<PartialUpdateAPIAuditLog>;

export type EntityResponseType = HttpResponse<IAPIAuditLog>;
export type EntityArrayResponseType = HttpResponse<IAPIAuditLog[]>;

@Injectable({ providedIn: 'root' })
export class APIAuditLogService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/api-audit-logs');

  create(aPIAuditLog: NewAPIAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aPIAuditLog);
    return this.http
      .post<RestAPIAuditLog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(aPIAuditLog: IAPIAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aPIAuditLog);
    return this.http
      .put<RestAPIAuditLog>(`${this.resourceUrl}/${this.getAPIAuditLogIdentifier(aPIAuditLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(aPIAuditLog: PartialUpdateAPIAuditLog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(aPIAuditLog);
    return this.http
      .patch<RestAPIAuditLog>(`${this.resourceUrl}/${this.getAPIAuditLogIdentifier(aPIAuditLog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAPIAuditLog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAPIAuditLog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAPIAuditLogIdentifier(aPIAuditLog: Pick<IAPIAuditLog, 'id'>): number {
    return aPIAuditLog.id;
  }

  compareAPIAuditLog(o1: Pick<IAPIAuditLog, 'id'> | null, o2: Pick<IAPIAuditLog, 'id'> | null): boolean {
    return o1 && o2 ? this.getAPIAuditLogIdentifier(o1) === this.getAPIAuditLogIdentifier(o2) : o1 === o2;
  }

  addAPIAuditLogToCollectionIfMissing<Type extends Pick<IAPIAuditLog, 'id'>>(
    aPIAuditLogCollection: Type[],
    ...aPIAuditLogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const aPIAuditLogs: Type[] = aPIAuditLogsToCheck.filter(isPresent);
    if (aPIAuditLogs.length > 0) {
      const aPIAuditLogCollectionIdentifiers = aPIAuditLogCollection.map(aPIAuditLogItem => this.getAPIAuditLogIdentifier(aPIAuditLogItem));
      const aPIAuditLogsToAdd = aPIAuditLogs.filter(aPIAuditLogItem => {
        const aPIAuditLogIdentifier = this.getAPIAuditLogIdentifier(aPIAuditLogItem);
        if (aPIAuditLogCollectionIdentifiers.includes(aPIAuditLogIdentifier)) {
          return false;
        }
        aPIAuditLogCollectionIdentifiers.push(aPIAuditLogIdentifier);
        return true;
      });
      return [...aPIAuditLogsToAdd, ...aPIAuditLogCollection];
    }
    return aPIAuditLogCollection;
  }

  protected convertDateFromClient<T extends IAPIAuditLog | NewAPIAuditLog | PartialUpdateAPIAuditLog>(aPIAuditLog: T): RestOf<T> {
    return {
      ...aPIAuditLog,
      eventTime: aPIAuditLog.eventTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAPIAuditLog: RestAPIAuditLog): IAPIAuditLog {
    return {
      ...restAPIAuditLog,
      eventTime: restAPIAuditLog.eventTime ? dayjs(restAPIAuditLog.eventTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAPIAuditLog>): HttpResponse<IAPIAuditLog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAPIAuditLog[]>): HttpResponse<IAPIAuditLog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
