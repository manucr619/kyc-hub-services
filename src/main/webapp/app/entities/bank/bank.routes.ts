import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BankResolve from './route/bank-routing-resolve.service';

const bankRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bank.component').then(m => m.BankComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bank-detail.component').then(m => m.BankDetailComponent),
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bank-update.component').then(m => m.BankUpdateComponent),
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bank-update.component').then(m => m.BankUpdateComponent),
    resolve: {
      bank: BankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bankRoute;
