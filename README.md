# capacitor-plugin-camera-parameters

Custom capacitor plugin that retrieve intrinsic camera parameters

## Install

```bash
npm install capacitor-plugin-camera-parameters
npx cap sync
```

## API

<docgen-index>

* [`getIntrinsicParameters(...)`](#getIntrinsicParameters)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getIntrinsicParameters(...)

```typescript
getIntrinsicParameters() => Promise<IntrinsicParameters>
```

**Returns:** <code>Promise&lt;{ 
  focalLength: number;
  sensorWidth: number;
  sensorHeight: number;
  principalPointX: number;
  principalPointY: number;}
&gt;</code>

--------------------

</docgen-api>
