package by.alon22.cordova.firebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.common.InputImage;


import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * This class echoes a string called from JavaScript.
 */
public class FirebaseVisionPlugin extends CordovaPlugin {

    protected static Context applicationContext = null;
    private static Activity cordovaActivity = null;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        cordovaActivity = this.cordova.getActivity();
        applicationContext = cordovaActivity.getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("barcodeDetector")) {
            String message = args.getString(0);
            this.barcodeDetector(message, callbackContext);
            return true;
        }
        return false;
    }

    private void barcodeDetector(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            try {
                //InputImage image = getImage(message);

                //ImageScanner ZBarScanner = new ImageScanner();
                //ZBarScanner.setConfig(0, Config.X_DENSITY, 3);
                //ZBarScanner.setConfig(0, Config.Y_DENSITY, 3);
                //ZBarScanner.setConfig(64, Config.ENABLE, 1); // 64 only QR code


                byte[] base64data = base64toByte(message, callbackContext);
                Bitmap bitMap = BitmapFactory.decodeByteArray(base64data, 0, base64data.length);
                BarcodeScannerOptions options =
                        new BarcodeScannerOptions.Builder()
                                .setBarcodeFormats(
                                        Barcode.FORMAT_QR_CODE,
                                        Barcode.FORMAT_AZTEC
                                )
                                .build();
                BarcodeScanner detector = BarcodeScanning.getClient(options);

                InputImage image = InputImage.fromBitmap(bitMap, 0);

                //Image barcode = new Image(bitMap.getWidth(), bitMap.getHeight(), "Y800");
                //barcode.setData(base64data);

                /*if (ZBarScanner.scanImage(barcode) != 0) {
                    SymbolSet syms = ZBarScanner.getResults();
                    String qrValue = "";

                    for (Symbol sym : syms) {
                        Log.d("MyLogTag!!!!!!!!!", "Found in zbar");
                        qrValue = sym.getData();

                        // Return 1st found QR code value to the calling Activity.
                    }
                    callbackContext.success(qrValue);
                    return;
                }*/

                detector.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                            @Override
                            public void onSuccess(List<Barcode> firebaseVisionBarcodes) {
                                try {
                                    JSONArray barcodes = FirebaseUtils.parseBarcodes(image, firebaseVisionBarcodes);
                                    callbackContext.success(barcodes);
                                } catch (Exception e) {
                                    callbackContext.error(e.getLocalizedMessage());
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackContext.error(e.getLocalizedMessage());
                            }
                        });
            } catch (Exception e) {
                callbackContext.error(e.getLocalizedMessage());
            }
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private InputImage getImage(String message) throws IOException {
        if (message.contains("data:")) {
            message = message
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "");
            byte[] decodedString = Base64.decode(message, Base64.DEFAULT);
            Bitmap bitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            InputImage image = InputImage.fromBitmap(bitMap, 0);
            return image;
        } else {
            Uri uri = Uri.parse(message);
            InputImage image = InputImage.fromFilePath(applicationContext, uri);
            return image;
        }
    }

    private byte[] base64toByte(String message, CallbackContext callbackContext) throws IOException {
        byte[] decodedString;
        if (message.contains("data:")) {
            message = message.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
        decodedString = Base64.decode(message, Base64.DEFAULT);
        return decodedString;
    }
}
