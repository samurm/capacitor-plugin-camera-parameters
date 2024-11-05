import { WebPlugin } from '@capacitor/core';
export class cameraParametersWeb extends WebPlugin {
    async echo(options) {
        return options;
    }
    async getIntrinsicParameters(options) {
        console.log(options.position);
        return {};
    }
    async getExtrinsicParameters() {
        return {};
    }
}
//# sourceMappingURL=web.js.map