package FiNe;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Zeep extends CordovaPlugin {

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
            String fromPath = removePathProtocolPrefix(from);
            String toPath = removePathProtocolPrefix(to);

            final ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_MAXIMUM);
            if (password != null && !password.isEmpty()) {
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                zipParameters.setPassword(password);
            }
            zipParameters.setIncludeRootFolder(false);

            ZipFile zipFile = new ZipFile(toPath);
            zipFile.addFolder(fromPath, zipParameters);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void unzip(String from, String to, String password) throws Exception {
        try {

            String fromPath = removePathProtocolPrefix(from);
            String toPath = removePathProtocolPrefix(to);

            ZipFile zipFile = new ZipFile(fromPath);
            if (password != null && !password.isEmpty() && zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }

            zipFile.extractAll(toPath);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private static String removePathProtocolPrefix(String path) {

        if (path.startsWith("file://")) {
            path = path.substring(7);
        } else if (path.startsWith("file:")) {
            path = path.substring(5);
        }

        return path.replaceAll("//", "/");

    }

}
