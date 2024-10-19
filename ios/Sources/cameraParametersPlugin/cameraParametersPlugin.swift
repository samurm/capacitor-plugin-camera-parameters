import Foundation
import Capacitor
import AVFoundation
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(cameraParametersPlugin)
public class cameraParametersPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "cameraParametersPlugin"
    public let jsName = "cameraParameters"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = cameraParameters()

    @objc func getIntrinsicParameters(_ call: CAPPluginCall) {
        guard let device = AVCaptureDevice.default(for: .video) else {
            call.reject("No video device found")
            return
        }

        do {
            try device.lockForConfiguration()

            // Focal Length in millimeters
            let focalLength = device.lensPosition // Note: lensPosition is a normalized value (0.0 to 1.0)

            // Sensor Size
            let formatDescription = device.activeFormat.formatDescription
            let dimensions = CMVideoFormatDescriptionGetPresentationDimensions(formatDescription, usePixelAspectRatio: true, useCleanAperture: true)
            let sensorWidth = dimensions.width
            let sensorHeight = dimensions.height

            // Principal Point (Assuming center of the sensor)
            let principalPointX = sensorWidth / 2
            let principalPointY = sensorHeight / 2

            device.unlockForConfiguration()

            // Output the parameters
            let result: [String: Any] = [
                "focalLength": focalLength,
                "sensorWidth": sensorWidth,
                "sensorHeight": sensorHeight,
                "principalPointX": principalPointX,
                "principalPointY": principalPointY
            ]

            call.resolve(result)
        } catch {
            call.reject("Failed to lock device for configuration \(error)")
        }
    }
}
