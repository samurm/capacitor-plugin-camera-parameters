import { cameraParameters } from 'capacitor-plugin-camera-parameters';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    cameraParameters.echo({ value: inputValue })
}
