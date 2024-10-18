import { registerPlugin } from '@capacitor/core';

import type { cameraParametersPlugin } from './definitions';

const cameraParameters = registerPlugin<cameraParametersPlugin>('cameraParameters', {
  web: () => import('./web').then((m) => new m.cameraParametersWeb()),
});

export * from './definitions';
export { cameraParameters };
