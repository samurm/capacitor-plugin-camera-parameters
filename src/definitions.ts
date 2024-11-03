
export interface IntrinsicParameters {
  focalLength: number;
  sensorWidth: number;
  sensorHeight: number;
  principalPointX: number;
  principalPointY: number;
}

export interface ExtrinsicParameters {
  rotationMatrix: number[];
  translationVector: number[];
}

export interface cameraParametersPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  getIntrinsicParameters(): Promise<{ value: any }>;
  getExtrinsicParameters(): Promise<{ value: any }>;
}
