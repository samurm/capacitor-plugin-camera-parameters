export interface cameraParametersPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
