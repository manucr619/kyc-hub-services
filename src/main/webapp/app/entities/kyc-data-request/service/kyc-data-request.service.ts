import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKycDataRequest, NewKycDataRequest } from '../kyc-data-request.model';

export type PartialUpdateKycDataRequest = Partial<IKycDataRequest> & Pick<IKycDataRequest, 'id'>;

type RestOf<T extends IKycDataRequest | NewKycDataRequest> = Omit<T, 'requestDate' | 'respondedAt'> & {
  requestDate?: string | null;
  respondedAt?: string | null;
};

export type RestKycDataRequest = RestOf<IKycDataRequest>;

export type NewRestKycDataRequest = RestOf<NewKycDataRequest>;

export type PartialUpdateRestKycDataRequest = RestOf<PartialUpdateKycDataRequest>;

export type EntityResponseType = HttpResponse<IKycDataRequest>;
export type EntityArrayResponseType = HttpResponse<IKycDataRequest[]>;

@Injectable({ providedIn: 'root' })
export class KycDataRequestService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-data-requests');

  create(kycDataRequest: NewKycDataRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDataRequest);
    return this.http
      .post<RestKycDataRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycDataRequest: IKycDataRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDataRequest);
    return this.http
      .put<RestKycDataRequest>(`${this.resourceUrl}/${this.getKycDataRequestIdentifier(kycDataRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycDataRequest: PartialUpdateKycDataRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycDataRequest);
    return this.http
      .patch<RestKycDataRequest>(`${this.resourceUrl}/${this.getKycDataRequestIdentifier(kycDataRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycDataRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycDataRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getKycDataRequestIdentifier(kycDataRequest: Pick<IKycDataRequest, 'id'>): number {
    return kycDataRequest.id;
  }

  compareKycDataRequest(o1: Pick<IKycDataRequest, 'id'> | null, o2: Pick<IKycDataRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycDataRequestIdentifier(o1) === this.getKycDataRequestIdentifier(o2) : o1 === o2;
  }

  addKycDataRequestToCollectionIfMissing<Type extends Pick<IKycDataRequest, 'id'>>(
    kycDataRequestCollection: Type[],
    ...kycDataRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycDataRequests: Type[] = kycDataRequestsToCheck.filter(isPresent);
    if (kycDataRequests.length > 0) {
      const kycDataRequestCollectionIdentifiers = kycDataRequestCollection.map(kycDataRequestItem =>
        this.getKycDataRequestIdentifier(kycDataRequestItem),
      );
      const kycDataRequestsToAdd = kycDataRequests.filter(kycDataRequestItem => {
        const kycDataRequestIdentifier = this.getKycDataRequestIdentifier(kycDataRequestItem);
        if (kycDataRequestCollectionIdentifiers.includes(kycDataRequestIdentifier)) {
          return false;
        }
        kycDataRequestCollectionIdentifiers.push(kycDataRequestIdentifier);
        return true;
      });
      return [...kycDataRequestsToAdd, ...kycDataRequestCollection];
    }
    return kycDataRequestCollection;
  }

  protected convertDateFromClient<T extends IKycDataRequest | NewKycDataRequest | PartialUpdateKycDataRequest>(
    kycDataRequest: T,
  ): RestOf<T> {
    return {
      ...kycDataRequest,
      requestDate: kycDataRequest.requestDate?.toJSON() ?? null,
      respondedAt: kycDataRequest.respondedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycDataRequest: RestKycDataRequest): IKycDataRequest {
    return {
      ...restKycDataRequest,
      requestDate: restKycDataRequest.requestDate ? dayjs(restKycDataRequest.requestDate) : undefined,
      respondedAt: restKycDataRequest.respondedAt ? dayjs(restKycDataRequest.respondedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycDataRequest>): HttpResponse<IKycDataRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycDataRequest[]>): HttpResponse<IKycDataRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
