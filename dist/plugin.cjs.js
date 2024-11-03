'use strict';

var core = require('@capacitor/core');

const cameraParameters = core.registerPlugin('cameraParameters', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.cameraParametersWeb()),
});

class cameraParametersWeb extends core.WebPlugin {
    async echo(options) {
        return options;
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    cameraParametersWeb: cameraParametersWeb
});

exports.cameraParameters = cameraParameters;
//# sourceMappingURL=plugin.cjs.js.map
