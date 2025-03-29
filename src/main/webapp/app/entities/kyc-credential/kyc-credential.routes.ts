import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import KycCredentialResolve from './route/kyc-credential-routing-resolve.service';

const kycCredentialRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/kyc-credential.component').then(m => m.KycCredentialComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/kyc-credential-detail.component').then(m => m.KycCredentialDetailComponent),
    resolve: {
      kycCredential: KycCredentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/kyc-credential-update.component').then(m => m.KycCredentialUpdateComponent),
    resolve: {
      kycCredential: KycCredentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/kyc-credential-update.component').then(m => m.KycCredentialUpdateComponent),
    resolve: {
      kycCredential: KycCredentialResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default kycCredentialRoute;
