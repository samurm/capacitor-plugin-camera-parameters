import { WebPlugin } from '@capacitor/core';
export class cameraParametersWeb extends WebPlugin {
    async echo(options) {
        return options;
    }
    async getIntrinsicParameters() {
        return {};
    }
    async getExtrinsicParameters() {
        return {};
    }
}
//# sourceMappingURL=web.js.map