import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import APIAuditLogResolve from './route/api-audit-log-routing-resolve.service';

const aPIAuditLogRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/api-audit-log.component').then(m => m.APIAuditLogComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/api-audit-log-detail.component').then(m => m.APIAuditLogDetailComponent),
    resolve: {
      aPIAuditLog: APIAuditLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/api-audit-log-update.component').then(m => m.APIAuditLogUpdateComponent),
    resolve: {
      aPIAuditLog: APIAuditLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/api-audit-log-update.component').then(m => m.APIAuditLogUpdateComponent),
    resolve: {
      aPIAuditLog: APIAuditLogResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default aPIAuditLogRoute;
