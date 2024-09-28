import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { AppService } from './app.service';

describe('AppComponent', () => {

  let mockAppService;

  beforeEach(async () => {
    mockAppService = jasmine.createSpyObj(['callStart', 'callGetState', 'startStateTimer', 'stopStateTimer']);
    mockAppService.callStart.and.returnValue('');
    mockAppService.callGetState.and.returnValue('');
    mockAppService.startStateTimer.and.returnValue('');
    mockAppService.stopStateTimer.and.returnValue('');
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [ { provide: AppService, useValue: mockAppService } ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

});
