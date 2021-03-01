import MLKitBarcodeScanning
import UIKit

@objc(FirebaseVisionPlugin)
class FirebaseVisionPlugin: CDVPlugin {

    @objc(barcodeDetector:)
    func barcodeDetector(command: CDVInvokedUrlCommand) {
        guard let imageURL = command.arguments.first as? String else {
            let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Image URL required")
            self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            return
        }
        getImage(imageURL: imageURL) { (image, error) in
            if let error = error {
                let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
            } else {
                let barcodeDetector = BarcodeScanner.barcodeScanner()
                let visionImage = VisionImage(image: image!)
                barcodeDetector.process(visionImage) { (barcodes, error) in
                    if let error = error {
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: error.localizedDescription)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    } else {
                        let barcodesDict = barcodes?.compactMap({ $0.toJSON(with: image!) })
                        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: barcodesDict)
                        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
                    }
                }
            }
        }
    }

    private func getImage(imageURL: String, _ completion: @escaping (_ image: UIImage?, _ error: Error?) -> Void) {
        if imageURL.contains("data:") {
            guard let data = Data(base64Encoded: imageURL
                                    .replacingOccurrences(of: "data:image/jpeg;base64,", with: "")
                                    .replacingOccurrences(of: "data:image/png;base64,", with: "")),
                  let image = UIImage(data: data) else {
                let error = NSError(domain: "cordova-plugin-firebase-mlvision",
                                    code: -1,
                                    userInfo: [NSLocalizedDescriptionKey : "Base64ImageError"])
                completion(nil, error)
                return
            }
            completion(image, nil)
        } else {
            guard let url = URL(string: imageURL) else {
                let error = NSError(domain: "cordova-plugin-firebase-mlvision",
                                    code: -1,
                                    userInfo: [NSLocalizedDescriptionKey : "URLImageError"])
                completion(nil, error)
                return
            }
            URLSession.shared.dataTask(with: url) { (data, response, error) in
                if let error = error {
                    completion(nil, error)
                } else {
                    guard let data = data, let image = UIImage(data: data) else {
                        let error = NSError(domain: "cordova-plugin-firebase-mlvision",
                                            code: -1,
                                            userInfo: [NSLocalizedDescriptionKey : "DownloadImageError"])
                        completion(nil, error)
                        return
                    }
                    completion(image, nil)
                }
            }
            .resume()
        }
    }
}
