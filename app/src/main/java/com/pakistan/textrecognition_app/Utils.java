package com.pakistan.textrecognition_app;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.aspose.cells.License;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class Utils {
    public static void applyALicense() {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + File.separator + "Aspose");
            String filePath = myDir.getCanonicalPath() + File.separator;

            License license = new License();
            license.setLicense(filePath + "Aspose.Total.Java.lic");
        } catch (Exception e) {
            Log.e(TAG, "Failed to set License", e);
        }
    }

    public static String saveFileToExternalStorage(Context context, String fileName) {

        try {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(fileName);

            FileOutputStream out = null;
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + File.separator + "Aspose");
            myDir.mkdirs();

            File file = new File(myDir, fileName);
            // Atomically creates a new, empty file named by this abstract pathname if and only if a file with this name does not yet exist.
            file.createNewFile();
            if (file.exists()) {
                return root;
            }
            try {
                out = new FileOutputStream(file);
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Saving File To External Storage", e);
        }
        return fileName;
    }

    public static void setupFontsFolderAndSaveFileToExternalStorage(Context context, String fontFolder1, String fontFolder2, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open(fileName);

            FileOutputStream out = null;
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + File.separator + "Aspose" + File.separator + fontFolder1);
            myDir.mkdirs();
            myDir = new File(root + File.separator + "Aspose" + File.separator + fontFolder2);
            myDir.mkdirs();

            File file = new File(myDir, fileName);
            file.createNewFile();
            if (file.exists()) file.delete();
            try {
                out = new FileOutputStream(file);
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Setup Fonts Folder And Save File To External Storage", e);
        }
    }
}
