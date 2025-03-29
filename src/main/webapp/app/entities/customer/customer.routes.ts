import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CustomerResolve from './route/customer-routing-resolve.service';

const customerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/customer.component').then(m => m.CustomerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/customer-detail.component').then(m => m.CustomerDetailComponent),
    resolve: {
      customer: CustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/customer-update.component').then(m => m.CustomerUpdateComponent),
    resolve: {
      customer: CustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/customer-update.component').then(m => m.CustomerUpdateComponent),
    resolve: {
      customer: CustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default customerRoute;
