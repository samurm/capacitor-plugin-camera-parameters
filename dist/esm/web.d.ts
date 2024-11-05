import { WebPlugin } from '@capacitor/core';
import type { cameraParametersPlugin, ExtrinsicParameters, IntrinsicParameters } from './definitions';
export declare class cameraParametersWeb extends WebPlugin implements cameraParametersPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    getIntrinsicParameters(options: {
        position: 'front' | 'back';
    }): Promise<IntrinsicParameters>;
    getExtrinsicParameters(): Promise<ExtrinsicParameters>;
}
