# capacitor-plugin-camera-parameters

Custom capacitor plugin that retrieve intrinsic camera parameters

## Install

```bash
npm install capacitor-plugin-camera-parameters
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`getIntrinsicParameters(...)`](#getintrinsicparameters)
* [`getExtrinsicParameters()`](#getextrinsicparameters)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### getIntrinsicParameters(...)

```typescript
getIntrinsicParameters(options: { position: 'front' | 'back'; }) => Promise<IntrinsicParameters>
```

| Param         | Type                                          |
| ------------- | --------------------------------------------- |
| **`options`** | <code>{ position: 'front' \| 'back'; }</code> |

**Returns:** <code>Promise&lt;<a href="#intrinsicparameters">IntrinsicParameters</a>&gt;</code>

--------------------


### getExtrinsicParameters()

```typescript
getExtrinsicParameters() => Promise<ExtrinsicParameters>
```

**Returns:** <code>Promise&lt;<a href="#extrinsicparameters">ExtrinsicParameters</a>&gt;</code>

--------------------


### Interfaces


#### IntrinsicParameters

| Prop                  | Type                |
| --------------------- | ------------------- |
| **`focalLength`**     | <code>number</code> |
| **`sensorWidth`**     | <code>number</code> |
| **`sensorHeight`**    | <code>number</code> |
| **`pixelWidth`**      | <code>number</code> |
| **`pixelHeight`**     | <code>number</code> |
| **`principalPointX`** | <code>number</code> |
| **`principalPointY`** | <code>number</code> |


#### ExtrinsicParameters

| Prop                    | Type                  |
| ----------------------- | --------------------- |
| **`rotationMatrix`**    | <code>number[]</code> |
| **`translationVector`** | <code>number[]</code> |

</docgen-api>
