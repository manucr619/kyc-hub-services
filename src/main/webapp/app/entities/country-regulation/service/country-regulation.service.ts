import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountryRegulation, NewCountryRegulation } from '../country-regulation.model';

export type PartialUpdateCountryRegulation = Partial<ICountryRegulation> & Pick<ICountryRegulation, 'id'>;

export type EntityResponseType = HttpResponse<ICountryRegulation>;
export type EntityArrayResponseType = HttpResponse<ICountryRegulation[]>;

@Injectable({ providedIn: 'root' })
export class CountryRegulationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/country-regulations');

  create(countryRegulation: NewCountryRegulation): Observable<EntityResponseType> {
    return this.http.post<ICountryRegulation>(this.resourceUrl, countryRegulation, { observe: 'response' });
  }

  update(countryRegulation: ICountryRegulation): Observable<EntityResponseType> {
    return this.http.put<ICountryRegulation>(
      `${this.resourceUrl}/${this.getCountryRegulationIdentifier(countryRegulation)}`,
      countryRegulation,
      { observe: 'response' },
    );
  }

  partialUpdate(countryRegulation: PartialUpdateCountryRegulation): Observable<EntityResponseType> {
    return this.http.patch<ICountryRegulation>(
      `${this.resourceUrl}/${this.getCountryRegulationIdentifier(countryRegulation)}`,
      countryRegulation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICountryRegulation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountryRegulation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCountryRegulationIdentifier(countryRegulation: Pick<ICountryRegulation, 'id'>): number {
    return countryRegulation.id;
  }

  compareCountryRegulation(o1: Pick<ICountryRegulation, 'id'> | null, o2: Pick<ICountryRegulation, 'id'> | null): boolean {
    return o1 && o2 ? this.getCountryRegulationIdentifier(o1) === this.getCountryRegulationIdentifier(o2) : o1 === o2;
  }

  addCountryRegulationToCollectionIfMissing<Type extends Pick<ICountryRegulation, 'id'>>(
    countryRegulationCollection: Type[],
    ...countryRegulationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const countryRegulations: Type[] = countryRegulationsToCheck.filter(isPresent);
    if (countryRegulations.length > 0) {
      const countryRegulationCollectionIdentifiers = countryRegulationCollection.map(countryRegulationItem =>
        this.getCountryRegulationIdentifier(countryRegulationItem),
      );
      const countryRegulationsToAdd = countryRegulations.filter(countryRegulationItem => {
        const countryRegulationIdentifier = this.getCountryRegulationIdentifier(countryRegulationItem);
        if (countryRegulationCollectionIdentifiers.includes(countryRegulationIdentifier)) {
          return false;
        }
        countryRegulationCollectionIdentifiers.push(countryRegulationIdentifier);
        return true;
      });
      return [...countryRegulationsToAdd, ...countryRegulationCollection];
    }
    return countryRegulationCollection;
  }
}
