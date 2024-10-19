package com.capacitor.plugins.cameraparameters;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import android.util.SizeF;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "cameraParameters")
public class cameraParametersPlugin extends Plugin {

    private cameraParameters implementation = new cameraParameters();

    @PluginMethod
    public void getIntrinsicParameters(PluginCall call) {
        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0]; // Assuming rear camera
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            // Focal Length in millimeters
            float[] focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            float focalLength = focalLengths[0];

            // Sensor Physical Size in millimeters
            SizeF sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            float sensorWidth = sensorSize.getWidth();
            float sensorHeight = sensorSize.getHeight();

            // Principal Point (Assuming center of the sensor)
            float principalPointX = sensorWidth / 2;
            float principalPointY = sensorHeight / 2;

            // Output the parameters
            JSObject result = new JSObject();
            result.put("focalLength", focalLength);
            result.put("sensorWidth", sensorWidth);
            result.put("sensorHeight", sensorHeight);
            result.put("principalPointX", principalPointX);
            result.put("principalPointY", principalPointY);

            call.resolve(result);

        } catch (CameraAccessException e) {
            call.reject("Failed to access camera", e);
        } catch (Exception e) {
            call.reject("An error occurred", e);
        }
    }
}
