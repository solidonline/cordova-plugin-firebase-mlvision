cordova-plugin-firebase-mlvision
========================

Cordova plugin for Firebase MLKit Vision

<a href="https://www.buymeacoffee.com/alon22" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" style="height: 51px !important;width: 217px !important;" ></a>

# Installation
Run:
```
cordova plugin add cordova-plugin-firebase-mlvision --save
```

dependency id="cordova-plugin-androidx" version="^2.0.0"
dependency id="cordova-plugin-androidx-adapter" version="^1.1.1"


# Usage
## Text recognition

```js
//FILE_URI: File URI or Base64 Format
FirebaseVisionPlugin.onDeviceTextRecognizer(FILE_URI,
    (text) => {
        console.log(text);
    },
    (error) => {
        console.error(error);;
    })
})
```

## Barcode detector
```js
//FILE_URI: File URI or Base64 Format
FirebaseVisionPlugin.barcodeDetector(FILE_URI,
    (json) => {
        console.log(json);
    },
    (error) => {
        console.error(error);;
    })
})
```

## Image Labeler
```js
//FILE_URI: File URI or Base64 Format
FirebaseVisionPlugin.imageLabeler(FILE_URI,
    (json) => {
        console.log(json);
    },
    (error) => {
        console.error(error);;
    })
})
```

# Support
|   |Android|iOS|
|---|---|---|
|Text recognition|X|X|
|Face detection| | |
|Barcode scanning|X|X|
|Image labeling|X|X|
|Object detection & tracking| | |
|Landmark recognition| | |

# Know Issues
## iOS
Build iOS from command line failed in
`PhaseScriptExecution [CP] Copy Pods Resources`

Running from Xcode work correctly
