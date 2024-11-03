import { WebPlugin } from '@capacitor/core';

import type { cameraParametersPlugin } from './definitions';

export class cameraParametersWeb extends WebPlugin implements cameraParametersPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    return options;
  }

  async getIntrinsicParameters(): Promise<{ value: any }> {
    return {} as any;
  }

  async getExtrinsicParameters(): Promise<{ value: any }> {
    return {} as any;
  }
}
