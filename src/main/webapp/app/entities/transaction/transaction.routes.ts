import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TransactionResolve from './route/transaction-routing-resolve.service';

const transactionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transaction.component').then(m => m.TransactionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transaction-detail.component').then(m => m.TransactionDetailComponent),
    resolve: {
      transaction: TransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transaction-update.component').then(m => m.TransactionUpdateComponent),
    resolve: {
      transaction: TransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transaction-update.component').then(m => m.TransactionUpdateComponent),
    resolve: {
      transaction: TransactionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transactionRoute;
