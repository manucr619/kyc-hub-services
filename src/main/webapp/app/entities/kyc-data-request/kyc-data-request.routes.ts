import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import KycDataRequestResolve from './route/kyc-data-request-routing-resolve.service';

const kycDataRequestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-data-request.component').then(m => m.KycDataRequestComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-data-request-detail.component').then(m => m.KycDataRequestDetailComponent),
    resolve: {
      kycDataRequest: KycDataRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-data-request-update.component').then(m => m.KycDataRequestUpdateComponent),
    resolve: {
      kycDataRequest: KycDataRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-data-request-update.component').then(m => m.KycDataRequestUpdateComponent),
    resolve: {
      kycDataRequest: KycDataRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycDataRequestRoute;
