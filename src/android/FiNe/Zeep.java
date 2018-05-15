package FiNe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;


public class Zeep extends CordovaPlugin {
    private static final int BUFFER_SIZE = 1024;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (action.equals("zip")) {
                zip(args.getString(0), args.getString(1), args.getString(2));
            } else {
                unzip(args.getString(0), args.getString(1), args.getString(2));
            }

            callbackContext.success(1);
            return true;
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }
    }

    private void zip(String from, String to, String password) throws Exception {
        try {
            String baseFileName = from;
            String destinationZipFilePath = to;

            final ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            if(password != false){
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                zipParameters.setPassword(password);
            }
            zipParameters.setIncludeRootFolder(false);

            ZipFile zipFile = new ZipFile(destinationZipFilePath);
            zipFile.addFolder(baseFileName, zipParameters);

        } finally {

        }
    }

    private void unzip(String from, String to, String password) throws Exception {
        try {

            ZipFile zipFile = new ZipFile(from);

            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }

            zipFile.extractAll(to);

        } finally {

        }
    }

    private static File getFile(String urlOrPath) {
        try {
            return new File(new URL(urlOrPath).toURI());
        } catch (Exception e) {
            return new File(urlOrPath);
        }
    }

}
