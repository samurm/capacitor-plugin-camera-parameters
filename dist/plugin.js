var capacitorcameraParameters = (function (exports, core) {
    'use strict';

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

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
