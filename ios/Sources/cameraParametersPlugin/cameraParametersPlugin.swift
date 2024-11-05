import Foundation
import Capacitor
import AVFoundation
import CoreMotion
import CoreLocation
import ARKit
import UIKit

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(cameraParametersPlugin)
public class cameraParametersPlugin: CAPPlugin, CAPBridgedPlugin, CLLocationManagerDelegate, ARSessionDelegate {
    public let identifier = "cameraParametersPlugin"
    public let jsName = "cameraParameters"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "getIntrinsicParameters", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getExtrinsicParameters", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = cameraParameters()
    private let motionManager = CMMotionManager()
    private let locationManager = CLLocationManager()
    private var rotationMatrix = [Double](repeating: 0.0, count: 9)
    private var translationVector = [Double](repeating: 0.0, count: 3)

    override public func load() {
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
        startSensors()
    }

    @objc func getIntrinsicParameters(_ call: CAPPluginCall) {
        guard let cameraPosition = call.getString("position"), (cameraPosition == "front" || cameraPosition == "back") else {
            call.reject("Parameter 'position' is required and should be either 'front' or 'back'")
            return
        }

        let devicePosition: AVCaptureDevice.Position = (cameraPosition == "front") ? .front : .back
        guard let device = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: devicePosition) else {
            call.reject("No \(cameraPosition) camera found")
            return
        }

        do {
            try device.lockForConfiguration()

            // Focal Length in millimeters
            let focalLength = device.activeFormat.videoFieldOfView // Note: lensPosition is a normalized value (0.0 to 1.0)
            let lensPosition = device.lensPosition // Note: lensPosition is a normalized value (0.0 to 1.0)

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
                "lensPosition": lensPosition,
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

    // Start sensors for rotation matrix with a listener for rotationMatrixArray
    private func startSensors() {
        if motionManager.isDeviceMotionAvailable {
            motionManager.deviceMotionUpdateInterval = 0.1
            motionManager.startDeviceMotionUpdates(to: .main) { (motion, error) in
                if let motion = motion {
                    self.extractRotationMatrix(from: motion)
                
                    // Convert the updated rotation matrix to JSON format
                    let rotationMatrixArray = self.rotationMatrix.map { NSNumber(value: $0) }
                
                    // Notify JavaScript listeners about the update
                    self.notifyListeners("rotationMatrixUpdated", data: [
                        "rotationMatrix": rotationMatrixArray
                    ])
                }
            }
        }

        if CLLocationManager.locationServicesEnabled() {
            locationManager.startUpdatingLocation()
        }
    }

    // Extract rotation matrix from motion data
    private func extractRotationMatrix(from motion: CMDeviceMotion) {
        let rotationMatrixData = motion.attitude.rotationMatrix
        
        // Map to 9-element array
        rotationMatrix[0] = rotationMatrixData.m11
        rotationMatrix[1] = rotationMatrixData.m12
        rotationMatrix[2] = rotationMatrixData.m13
        rotationMatrix[3] = rotationMatrixData.m21
        rotationMatrix[4] = rotationMatrixData.m22
        rotationMatrix[5] = rotationMatrixData.m23
        rotationMatrix[6] = rotationMatrixData.m31
        rotationMatrix[7] = rotationMatrixData.m32
        rotationMatrix[8] = rotationMatrixData.m33
    }

    // Handle location updates (translation vector)
    public func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            translationVector[0] = location.coordinate.latitude
            translationVector[1] = location.coordinate.longitude
            translationVector[2] = location.altitude
        }
    }

    // Method to get extrinsic parameters (rotation matrix and translation vector)
    @objc func getExtrinsicParameters(_ call: CAPPluginCall) {
        // Convert rotation matrix and translation vector to JSON array format
        let rotationMatrixArray = rotationMatrix.map { NSNumber(value: $0) }
        let translationVectorArray = translationVector.map { NSNumber(value: $0) }
        
        // Create a result dictionary
        let result: [String: Any] = [
            "rotationMatrix": rotationMatrixArray,
            "translationVector": translationVectorArray
        ]
        
        // Resolve the result to JavaScript side
        call.resolve(result)
    }
}
