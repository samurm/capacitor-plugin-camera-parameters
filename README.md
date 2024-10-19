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

**Returns:**
<pre>
Promise&lt;{ 
&ensp;focalLength: number;
&ensp;sensorWidth: number;
&ensp;sensorHeight: number;
&ensp;principalPointX: number;
&ensp;principalPointY: number;
}
&gt;</pre>

--------------------

</docgen-api>
