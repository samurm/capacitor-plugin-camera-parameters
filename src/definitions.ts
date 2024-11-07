
export interface IntrinsicParameters {
  focalLength: number;
  sensorWidth: number;
  sensorHeight: number;
  pixelWidth: number;
  pixelHeight: number;
  principalPointX: number;
  principalPointY: number;
}

export interface ExtrinsicParameters {
  rotationMatrix: number[];
  translationVector: number[];
}

export interface cameraParametersPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getIntrinsicParameters(options: { position: 'front' | 'back'}): Promise<IntrinsicParameters>;
  getExtrinsicParameters(): Promise<ExtrinsicParameters>;
}
