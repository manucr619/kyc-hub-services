import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { APIAuditLogDetailComponent } from './api-audit-log-detail.component';

describe('APIAuditLog Management Detail Component', () => {
  let comp: APIAuditLogDetailComponent;
  let fixture: ComponentFixture<APIAuditLogDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [APIAuditLogDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./api-audit-log-detail.component').then(m => m.APIAuditLogDetailComponent),
              resolve: { aPIAuditLog: () => of({ id: 23832 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(APIAuditLogDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(APIAuditLogDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aPIAuditLog on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', APIAuditLogDetailComponent);

      // THEN
      expect(instance.aPIAuditLog()).toEqual(expect.objectContaining({ id: 23832 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
