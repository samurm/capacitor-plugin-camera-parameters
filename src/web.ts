import { WebPlugin } from '@capacitor/core';

import type { cameraParametersPlugin, ExtrinsicParameters, IntrinsicParameters } from './definitions';

export class cameraParametersWeb extends WebPlugin implements cameraParametersPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    return options;
  }

  async getIntrinsicParameters(options: { position: 'front' | 'back' }): Promise<IntrinsicParameters> {
    console.log(options.position);
    return {} as IntrinsicParameters;
  }

  async getExtrinsicParameters(): Promise<ExtrinsicParameters> {
    return {} as ExtrinsicParameters;
  }
}
