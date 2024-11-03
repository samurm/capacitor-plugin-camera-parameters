import { WebPlugin } from '@capacitor/core';
import type { cameraParametersPlugin } from './definitions';
export declare class cameraParametersWeb extends WebPlugin implements cameraParametersPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
    getIntrinsicParameters(): Promise<any>;
    getExtrinsicParameters(): Promise<any>;
}
