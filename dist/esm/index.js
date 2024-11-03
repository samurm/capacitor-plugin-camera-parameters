import { registerPlugin } from '@capacitor/core';
const cameraParameters = registerPlugin('cameraParameters', {
    web: () => import('./web').then((m) => new m.cameraParametersWeb()),
});
export * from './definitions';
export { cameraParameters };
//# sourceMappingURL=index.js.map