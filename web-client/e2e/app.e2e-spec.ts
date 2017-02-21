import { WebClientPage } from './app.po';

describe('web-client App', () => {
  let page: WebClientPage;

  beforeEach(() => {
    page = new WebClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
