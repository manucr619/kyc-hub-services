import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKycConsent, NewKycConsent } from '../kyc-consent.model';

export type PartialUpdateKycConsent = Partial<IKycConsent> & Pick<IKycConsent, 'id'>;

type RestOf<T extends IKycConsent | NewKycConsent> = Omit<T, 'consentGivenAt' | 'expiresAt' | 'revokedAt'> & {
  consentGivenAt?: string | null;
  expiresAt?: string | null;
  revokedAt?: string | null;
};

export type RestKycConsent = RestOf<IKycConsent>;

export type NewRestKycConsent = RestOf<NewKycConsent>;

export type PartialUpdateRestKycConsent = RestOf<PartialUpdateKycConsent>;

export type EntityResponseType = HttpResponse<IKycConsent>;
export type EntityArrayResponseType = HttpResponse<IKycConsent[]>;

@Injectable({ providedIn: 'root' })
export class KycConsentService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-consents');

  create(kycConsent: NewKycConsent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycConsent);
    return this.http
      .post<RestKycConsent>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycConsent: IKycConsent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycConsent);
    return this.http
      .put<RestKycConsent>(`${this.resourceUrl}/${this.getKycConsentIdentifier(kycConsent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycConsent: PartialUpdateKycConsent): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycConsent);
    return this.http
      .patch<RestKycConsent>(`${this.resourceUrl}/${this.getKycConsentIdentifier(kycConsent)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycConsent>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycConsent[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getKycConsentIdentifier(kycConsent: Pick<IKycConsent, 'id'>): number {
    return kycConsent.id;
  }

  compareKycConsent(o1: Pick<IKycConsent, 'id'> | null, o2: Pick<IKycConsent, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycConsentIdentifier(o1) === this.getKycConsentIdentifier(o2) : o1 === o2;
  }

  addKycConsentToCollectionIfMissing<Type extends Pick<IKycConsent, 'id'>>(
    kycConsentCollection: Type[],
    ...kycConsentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycConsents: Type[] = kycConsentsToCheck.filter(isPresent);
    if (kycConsents.length > 0) {
      const kycConsentCollectionIdentifiers = kycConsentCollection.map(kycConsentItem => this.getKycConsentIdentifier(kycConsentItem));
      const kycConsentsToAdd = kycConsents.filter(kycConsentItem => {
        const kycConsentIdentifier = this.getKycConsentIdentifier(kycConsentItem);
        if (kycConsentCollectionIdentifiers.includes(kycConsentIdentifier)) {
          return false;
        }
        kycConsentCollectionIdentifiers.push(kycConsentIdentifier);
        return true;
      });
      return [...kycConsentsToAdd, ...kycConsentCollection];
    }
    return kycConsentCollection;
  }

  protected convertDateFromClient<T extends IKycConsent | NewKycConsent | PartialUpdateKycConsent>(kycConsent: T): RestOf<T> {
    return {
      ...kycConsent,
      consentGivenAt: kycConsent.consentGivenAt?.toJSON() ?? null,
      expiresAt: kycConsent.expiresAt?.toJSON() ?? null,
      revokedAt: kycConsent.revokedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycConsent: RestKycConsent): IKycConsent {
    return {
      ...restKycConsent,
      consentGivenAt: restKycConsent.consentGivenAt ? dayjs(restKycConsent.consentGivenAt) : undefined,
      expiresAt: restKycConsent.expiresAt ? dayjs(restKycConsent.expiresAt) : undefined,
      revokedAt: restKycConsent.revokedAt ? dayjs(restKycConsent.revokedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycConsent>): HttpResponse<IKycConsent> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycConsent[]>): HttpResponse<IKycConsent[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
