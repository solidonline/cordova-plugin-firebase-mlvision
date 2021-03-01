cordova-plugin-firebase-mlvision
========================

Cordova plugin for Firebase MLKit Vision


# Installation
Run:
```
cordova plugin add https://github.com/solidonline/cordova-plugin-firebase-mlvision.git
```

dependency id="cordova-plugin-androidx" version="^2.0.0"
dependency id="cordova-plugin-androidx-adapter" version="^1.1.1"

Ios know issues when install

`
Error: pod: Command failed with exit code 1
`

Fix https://stackoverflow.com/questions/62423247/cocoapods-could-not-find-compatible-versions-for-pod-googledatatransport-when-in

```js
cd cordova/platforms/ios
rm Podfile.lock
pod install
```


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
