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
          <div v-else>
            Intrinsic matrix not found
          </div>
        </template>
        <div v-else>
          <strong>Intrinsic params not found</strong>
        </div>
      </div>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { IonContent, IonHeader, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { onMounted, Ref, ref } from 'vue';
import { cameraParameters, IntrinsicParameters } from 'capacitor-plugin-camera-parameters';

const intrinsicParamsData: Ref<IntrinsicParameters | undefined> = ref();
const computeIntrinsicMatrixData = ref();

onMounted(async () => {
  try {
      const intrinsicParams = await cameraParameters.getIntrinsicParameters();
      intrinsicParamsData.value = intrinsicParams;
      computeIntrinsicMatrixData.value = computeIntrinsicMatrix();
    } catch (error) {
      console.error('TODO: Error retrieving intrinsic parameters:', error);
    }
});

function computeIntrinsicMatrix() {
  if (intrinsicParamsData.value) {
    const { focalLength, sensorWidth, sensorHeight } = intrinsicParamsData.value;

    // Assuming you have the image resolution
    const imageWidth = 1280/* Image width in pixels */
    const imageHeight = 720/* Image height in pixels */

    const fx = (focalLength * imageWidth) / sensorWidth;
    const fy = (focalLength * imageHeight) / sensorHeight;
    const cx = imageWidth / 2;
    const cy = imageHeight / 2;

    const intrinsicMatrix = [
      [fx, 0,  cx],
      [0,  fy, cy],
      [0,  0,  1],
    ];

    return intrinsicMatrix;
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
