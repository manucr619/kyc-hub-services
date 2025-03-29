import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IKycCredential, NewKycCredential } from '../kyc-credential.model';

export type PartialUpdateKycCredential = Partial<IKycCredential> & Pick<IKycCredential, 'id'>;

type RestOf<T extends IKycCredential | NewKycCredential> = Omit<T, 'issuedDate' | 'expiryDate'> & {
  issuedDate?: string | null;
  expiryDate?: string | null;
};

export type RestKycCredential = RestOf<IKycCredential>;

export type NewRestKycCredential = RestOf<NewKycCredential>;

export type PartialUpdateRestKycCredential = RestOf<PartialUpdateKycCredential>;

export type EntityResponseType = HttpResponse<IKycCredential>;
export type EntityArrayResponseType = HttpResponse<IKycCredential[]>;

@Injectable({ providedIn: 'root' })
export class KycCredentialService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/kyc-credentials');

  create(kycCredential: NewKycCredential): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCredential);
    return this.http
      .post<RestKycCredential>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(kycCredential: IKycCredential): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCredential);
    return this.http
      .put<RestKycCredential>(`${this.resourceUrl}/${this.getKycCredentialIdentifier(kycCredential)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(kycCredential: PartialUpdateKycCredential): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(kycCredential);
    return this.http
      .patch<RestKycCredential>(`${this.resourceUrl}/${this.getKycCredentialIdentifier(kycCredential)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestKycCredential>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestKycCredential[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getKycCredentialIdentifier(kycCredential: Pick<IKycCredential, 'id'>): number {
    return kycCredential.id;
  }

  compareKycCredential(o1: Pick<IKycCredential, 'id'> | null, o2: Pick<IKycCredential, 'id'> | null): boolean {
    return o1 && o2 ? this.getKycCredentialIdentifier(o1) === this.getKycCredentialIdentifier(o2) : o1 === o2;
  }

  addKycCredentialToCollectionIfMissing<Type extends Pick<IKycCredential, 'id'>>(
    kycCredentialCollection: Type[],
    ...kycCredentialsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const kycCredentials: Type[] = kycCredentialsToCheck.filter(isPresent);
    if (kycCredentials.length > 0) {
      const kycCredentialCollectionIdentifiers = kycCredentialCollection.map(kycCredentialItem =>
        this.getKycCredentialIdentifier(kycCredentialItem),
      );
      const kycCredentialsToAdd = kycCredentials.filter(kycCredentialItem => {
        const kycCredentialIdentifier = this.getKycCredentialIdentifier(kycCredentialItem);
        if (kycCredentialCollectionIdentifiers.includes(kycCredentialIdentifier)) {
          return false;
        }
        kycCredentialCollectionIdentifiers.push(kycCredentialIdentifier);
        return true;
      });
      return [...kycCredentialsToAdd, ...kycCredentialCollection];
    }
    return kycCredentialCollection;
  }

  protected convertDateFromClient<T extends IKycCredential | NewKycCredential | PartialUpdateKycCredential>(kycCredential: T): RestOf<T> {
    return {
      ...kycCredential,
      issuedDate: kycCredential.issuedDate?.toJSON() ?? null,
      expiryDate: kycCredential.expiryDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restKycCredential: RestKycCredential): IKycCredential {
    return {
      ...restKycCredential,
      issuedDate: restKycCredential.issuedDate ? dayjs(restKycCredential.issuedDate) : undefined,
      expiryDate: restKycCredential.expiryDate ? dayjs(restKycCredential.expiryDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestKycCredential>): HttpResponse<IKycCredential> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestKycCredential[]>): HttpResponse<IKycCredential[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
