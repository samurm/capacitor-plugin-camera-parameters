<template>
  <ion-page>
    <ion-header :translucent="true">
      <ion-toolbar>
        <ion-title>Blank</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content :fullscreen="true">
      <ion-header collapse="condense">
        <ion-toolbar>
          <ion-title size="large">Blank</ion-title>
        </ion-toolbar>
      </ion-header>

      <div id="container">
        <template v-if="intrinsicParamsData && Object.keys(intrinsicParamsData).length">
          <strong>Intrinsic params:</strong>
          <div v-for="(intrinsicParam, index) of Object.keys(intrinsicParamsData)" :key="index">
            {{ intrinsicParam + ': ' + intrinsicParamsData[intrinsicParam as keyof IntrinsicParameters] }}
          </div>
          <div class="ion-margin" v-if="computeIntrinsicMatrixData">
            <strong>Intrinsic matrix for a 1280 x 720 image:</strong>
            <div>{{ computeIntrinsicMatrixData[0] }}</div>
            <div>{{ computeIntrinsicMatrixData[1] }}</div>
            <div>{{ computeIntrinsicMatrixData[2] }}</div>
          </div>
          <div v-else>Intrinsic matrix not found</div>
        </template>
        <div v-else>
          <strong>Intrinsic params not found</strong>
        </div>

        <template v-if="extrinsicParamsData && Object.keys(extrinsicParamsData).length">
          <strong>Extrinsic params:</strong>
          <div v-for="(extrinsicParam, index) of Object.keys(extrinsicParamsData)" :key="index">
            {{ extrinsicParam + ': ' + extrinsicParamsData[extrinsicParam as keyof ExtrinsicParameters] }}
          </div>
          <div class="ion-margin" v-if="computeExtrinsicMatrixData">
            <strong>Extrinsic matrix:</strong>
            <div>{{ computeExtrinsicMatrixData[0] }}</div>
            <div>{{ computeExtrinsicMatrixData[1] }}</div>
            <div>{{ computeExtrinsicMatrixData[2] }}</div>
          </div>
          <div v-else>Extrinsic matrix not found</div>
        </template>
        <div v-else>
          <strong>Extrinsic params not found</strong>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { onMounted, Ref, ref } from 'vue';
import { cameraParameters, IntrinsicParameters, RotationEvent, ExtrinsicParameters } from 'capacitor-plugin-camera-parameters';

const intrinsicParamsData: Ref<IntrinsicParameters | undefined> = ref();
const extrinsicParamsData: Ref<RotationEvent | undefined> = ref();
const computeIntrinsicMatrixData = ref();
const computeExtrinsicMatrixData = ref();

onMounted(async () => {
  try {
    const intrinsicParams = await cameraParameters.getIntrinsicParameters({ position: "back" });
    intrinsicParamsData.value = intrinsicParams;
    console.log('TODO: back ', JSON.stringify(intrinsicParams));
    console.log('TODO: front ', JSON.stringify(await cameraParameters.getIntrinsicParameters({ position: "front" })));
    computeIntrinsicMatrixData.value = computeIntrinsicMatrix();
  } catch (error) {
    console.error('TODO: Error retrieving intrinsic parameters:', error);
  }

  try {
    /*const extrinsicParams = await cameraParameters.getExtrinsicParameters();
    extrinsicParamsData.value = extrinsicParams;
    computeExtrinsicMatrixData.value = computeExtrinsicMatrix();*/
  } catch (error) {
    console.error('TODO: Error retrieving extrinsic parameters:', error);
  }

  let isProcessing = false; // Flag para evitar múltiples llamadas

  cameraParameters.addListener('rotationMatrixUpdated', (event) => {
    if (!isProcessing) { // Solo permite la ejecución si no está en proceso
        isProcessing = true; // Activar el bloqueo

        setTimeout(() => {
            // Actualizar valores después de 2 segundos
            extrinsicParamsData.value = {
                gravity: event.gravity,
                rotation: event.rotation,
                geomagnetic: event.geomagnetic,
            };
            console.log('TODO: 0 Rotation Matrix Updated:', event);
            isProcessing = false; // Desbloquear para permitir futuras ejecuciones
        }, 1000);
    }
    // computeExtrinsicMatrixData.value = computeExtrinsicMatrix();
  });
});

function computeIntrinsicMatrix() {
  if (intrinsicParamsData.value) {
    const { intrinsicCalibration } = intrinsicParamsData.value;

    /*const fx = (focalLength * imageWidth) / sensorWidth;
    const fy = (focalLength * imageHeight) / sensorHeight;
    const cx = imageWidth / 2;
    const cy = imageHeight / 2;*/

    const intrinsicMatrix = [
      [intrinsicCalibration[0], intrinsicCalibration[4], intrinsicCalibration[2]],
      [0, intrinsicCalibration[1], intrinsicCalibration[3]],
      [0, 0, 1],
    ];

    return intrinsicMatrix;
  } else {
    return undefined;
  }
}

function computeExtrinsicMatrix() {
  if (extrinsicParamsData.value) {
    const { rotationMatrix, translationVector } = extrinsicParamsData.value;

    const extrinsicMatrix = [
      [rotationMatrix[0], rotationMatrix[1], rotationMatrix[2], translationVector[0]],
      [rotationMatrix[3], rotationMatrix[4], rotationMatrix[5], translationVector[1]],
      [rotationMatrix[6], rotationMatrix[7], rotationMatrix[8], translationVector[2]],
    ];

    return extrinsicMatrix;
  } else {
    return undefined;
  }
}
</script>

<style scoped>
#container {
  text-align: center;

  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

#container strong {
  font-size: 20px;
  line-height: 26px;
}

#container p {
  font-size: 16px;
  line-height: 22px;

  color: #8c8c8c;

  margin: 0;
}

#container a {
  text-decoration: none;
}
</style>
