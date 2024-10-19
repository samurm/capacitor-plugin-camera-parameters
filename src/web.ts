import { WebPlugin } from '@capacitor/core';

import type { cameraParametersPlugin } from './definitions';

export class cameraParametersWeb extends WebPlugin implements cameraParametersPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    return options;
  }
}
