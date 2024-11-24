package com.capacitor.plugins.cameraparameters;
import com.google.ar.core.*;
import com.google.ar.core.exceptions.*;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.content.Context;
import android.util.Size;
import android.util.SizeF;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import 	android.content.pm.PackageManager;
import org.json.JSONArray;
import org.json.JSONException;

@CapacitorPlugin(name = "cameraParameters")
public class cameraParametersPlugin extends Plugin {

    private cameraParameters implementation = new cameraParameters();

    @PluginMethod
    public void getIntrinsicParameters(PluginCall call) {
        String cameraPosition = call.getString("position");

        if (cameraPosition == null || (!cameraPosition.equals("front") && !cameraPosition.equals("back"))) {
            call.reject("Parameter 'position' is required and should be either 'front' or 'back'");
            return;
        }

        CameraManager manager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            String selectedCameraId = null;

            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                Integer lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
    
                if ((cameraPosition.equals("front") && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_FRONT) ||
                    (cameraPosition.equals("back") && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK)) {
                    selectedCameraId = cameraId;
                    break;
                }
            }

            if (selectedCameraId == null) {
                call.reject("No " + cameraPosition + " camera found");
                return;
            }

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(selectedCameraId);

            // Focal Length in millimeters
            float[] focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);

            // Sensor Physical Size in millimeters
            SizeF sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            float sensorWidth = sensorSize.getWidth();
            float sensorHeight = sensorSize.getHeight();

            // Resolución en píxeles
            Size pixelArraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
            int pixelWidth = pixelArraySize.getWidth();
            int pixelHeight = pixelArraySize.getHeight();

            float[] intrinsicCalibration = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intrinsicCalibration = characteristics.get(CameraCharacteristics.LENS_INTRINSIC_CALIBRATION);
            }

            // Principal Point (Assuming center of the sensor)
            float principalPointX = sensorWidth / 2;
            float principalPointY = sensorHeight / 2;

            JSONArray focalLength = new JSONArray();
            for (float value : focalLengths) {
                focalLength.put(value);
            }

            JSONArray intrinsicCalibrations = new JSONArray();
            for (float value : intrinsicCalibration) {
                intrinsicCalibrations.put(value);
            }
            
            // Output the parameters
            JSObject result = new JSObject();
            result.put("focalLength", focalLength);
            result.put("sensorWidth", sensorWidth);
            result.put("sensorHeight", sensorHeight);
            result.put("pixelWidth", pixelWidth);
            result.put("pixelHeight", pixelHeight);
            result.put("principalPointX", principalPointX);
            result.put("principalPointY", principalPointY);
            result.put("intrinsicCalibration", intrinsicCalibrations);

            call.resolve(result);

        } catch (CameraAccessException e) {
            call.reject("Failed to access camera", e);
        } catch (Exception e) {
            call.reject("An error occurred", e);
        }
    }

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private float[] rotationMatrix = new float[9];
    private float[] translationVector = new float[3];
    private Session arSession;

    @Override
    public void load() {
        super.load();

        // Initialize ARCore session
        try {
            arSession = new Session(getContext());
            Config config = new Config(arSession);
            arSession.configure(config);
        } catch (UnavailableException e) {
            Log.e("ARCore", "ARCore is not available or not supported", e);
        }

        /*sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Register sensor listeners for rotation matrix
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        SensorEventListener sensorEventListener = new SensorEventListener() {
            float[] gravity;
            float[] geomagnetic;
            boolean usingRotationVector = false;

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                    usingRotationVector = true;
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    gravity = event.values;
                } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    geomagnetic = event.values;
                }

                if (!usingRotationVector && gravity != null && geomagnetic != null) {
                    SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);
                }

                JSObject data = new JSObject();
                JSONArray rotationMatrixArray = new JSONArray();
                for (float value : rotationMatrix) {
                    try {
                        rotationMatrixArray.put(value);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                data.put("rotationMatrix", rotationMatrixArray);

                notifyListeners("rotationMatrixUpdate", data);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        if (rotationVectorSensor != null) {
            sensorManager.registerListener(sensorEventListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }*/
    }

    // Method to get camera extrinsic parameters
    @PluginMethod
    public void getExtrinsicParameters(PluginCall call) {
        try {
            if (arSession == null) {
                call.reject("ARCore session is not initialized");
                return;
            }

            // Obtain current AR frame
            Frame frame = arSession.update();
            Pose cameraPose = frame.getCamera().getPose();

            // Get Rotation Matrix
            float[] rotationMatrix = new float[16];
            cameraPose.toMatrix(rotationMatrix, 0);

            // Get Translation Vector
            float[] translationVector = cameraPose.getTranslation();

            // Prepare result
            JSObject result = new JSObject();

            JSONArray rotationMatrixArray = new JSONArray();
            for (float value : rotationMatrix) {
                rotationMatrixArray.put(value);
            }

            JSONArray translationVectorArray = new JSONArray();
            for (float value : translationVector) {
                translationVectorArray.put(value);
            }

            result.put("rotationMatrix", rotationMatrixArray);
            result.put("translationVector", translationVectorArray);

            // Send to JS
            call.resolve(result);

            // Get location (translation) if needed
            /*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                call.resolve(null);
            } else {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    translationVector[0] = (float) location.getLatitude();
                    translationVector[1] = (float) location.getLongitude();
                    translationVector[2] = (float) location.getAltitude();
                }

                // Create a JSON object to return
                JSObject result = new JSObject();

                JSONArray rotationMatrixArray = new JSONArray();
                for (float value : rotationMatrix) {
                    rotationMatrixArray.put(value);
                }
                JSONArray translationVectorArray = new JSONArray();
                for (float value : translationVector) {
                    translationVectorArray.put(value);
                }

                result.put("rotationMatrix", rotationMatrixArray);
                result.put("translationVector", translationVectorArray);

                // Send the result back to the JS side
                call.resolve(result);
            }*/
        } catch (Exception e) {
            call.reject("An error occurred", e);
        }
    }
}
