import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransaction, NewTransaction } from '../transaction.model';

export type PartialUpdateTransaction = Partial<ITransaction> & Pick<ITransaction, 'id'>;

type RestOf<T extends ITransaction | NewTransaction> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

export type RestTransaction = RestOf<ITransaction>;

export type NewRestTransaction = RestOf<NewTransaction>;

export type PartialUpdateRestTransaction = RestOf<PartialUpdateTransaction>;

export type EntityResponseType = HttpResponse<ITransaction>;
export type EntityArrayResponseType = HttpResponse<ITransaction[]>;

@Injectable({ providedIn: 'root' })
export class TransactionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transactions');

  create(transaction: NewTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .post<RestTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .put<RestTransaction>(`${this.resourceUrl}/${this.getTransactionIdentifier(transaction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transaction: PartialUpdateTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .patch<RestTransaction>(`${this.resourceUrl}/${this.getTransactionIdentifier(transaction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransactionIdentifier(transaction: Pick<ITransaction, 'id'>): number {
    return transaction.id;
  }

  compareTransaction(o1: Pick<ITransaction, 'id'> | null, o2: Pick<ITransaction, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransactionIdentifier(o1) === this.getTransactionIdentifier(o2) : o1 === o2;
  }

  addTransactionToCollectionIfMissing<Type extends Pick<ITransaction, 'id'>>(
    transactionCollection: Type[],
    ...transactionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transactions: Type[] = transactionsToCheck.filter(isPresent);
    if (transactions.length > 0) {
      const transactionCollectionIdentifiers = transactionCollection.map(transactionItem => this.getTransactionIdentifier(transactionItem));
      const transactionsToAdd = transactions.filter(transactionItem => {
        const transactionIdentifier = this.getTransactionIdentifier(transactionItem);
        if (transactionCollectionIdentifiers.includes(transactionIdentifier)) {
          return false;
        }
        transactionCollectionIdentifiers.push(transactionIdentifier);
        return true;
      });
      return [...transactionsToAdd, ...transactionCollection];
    }
    return transactionCollection;
  }

  protected convertDateFromClient<T extends ITransaction | NewTransaction | PartialUpdateTransaction>(transaction: T): RestOf<T> {
    return {
      ...transaction,
      timestamp: transaction.timestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransaction: RestTransaction): ITransaction {
    return {
      ...restTransaction,
      timestamp: restTransaction.timestamp ? dayjs(restTransaction.timestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransaction>): HttpResponse<ITransaction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransaction[]>): HttpResponse<ITransaction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
