package com.pakistan.textrecognition_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;



import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //  Workbook wb;
    String[] valueToWrite;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PERMISSION_REQUEST_CODE =2 ;
    private Button mBtn, mBtn_detect;
    private ImageView mimage;
    private TextView mTv;
    private Context ctx;
    List<String> all;
    List<ArrayList> arrayLists;
    List<String> stringList;

    File dir;
    private String dataDi,str;
    File directory, sd, file;
    //WritableWorkbook workbook;
    Bitmap bitmap;
    String txt;
    Object a;
    ArrayList<String> test;
    File sdcard;
    WriteGuru99ExcelFile objExcelFile = new WriteGuru99ExcelFile();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.button);
        mBtn_detect = findViewById(R.id.button2);
        mimage = findViewById(R.id.imageView);
        mTv = findViewById(R.id.textView);
        stringList=new ArrayList<String>();
     test   = new ArrayList<String>();





        mBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });


        mBtn_detect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                detect();


            }
        });







    }


    public void detect() {
        if (bitmap == null) {

            Toast.makeText(this, "image is null", Toast.LENGTH_SHORT).show();


        } else {
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextDetector firebaseVisionTextDetector =
                    FirebaseVision.getInstance().getVisionTextDetector();


            firebaseVisionTextDetector.detectInImage(firebaseVisionImage).
                    addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText texts) {
                            Log.d(TAG, "_TEXT: " + texts.toString());
                            processText(texts);



                            //processTextRecognitionResult(texts);
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    e.printStackTrace();
                                }
                            });
        }

    }

    private void processText(FirebaseVisionText texts) {
        List<FirebaseVisionText.Block> blocks = texts.getBlocks();


        for(FirebaseVisionText.Block block : blocks){
            List<FirebaseVisionText.Line> lines = block.getLines();
           // Log.d(TAG, "_BLOCK: "+block.getText());

            for(FirebaseVisionText.Line line : lines){
                List<FirebaseVisionText.Element> elements = line.getElements();
               // Log.d(TAG, "_BLOCK_LINE: "+line.getText());
                str= block.getText();
               a=line.getText();

                stringList.add(a.toString());




                // Adding text in line to ArrayList
              /*  test.add(str);
                Log.d("abc",test.toString());*/








                for(FirebaseVisionText.Element element : elements){
                    Log.d(TAG, "_ELEMENT: "+element.getText());

                    String str = element.getText();

                  //  mTv.append("\n");
                    // Extract Numbers from Convert Element Text
                    String[] numbers = str.split("(?<=\\D)(?=\\d)");
                    boolean isDigits = TextUtils.isDigitsOnly(numbers[0]);

                   /* if(isDigits){
                        Log.d(TAG, "_NUMBER: "+numbers[0]);
                    }else
                        Log.d(TAG, "_STRING: "+str);*/


                }

            }

        }

       /* String mySampleString = stringList.toString();
        String[] secondarray = mySampleString.split(", ");
        for (String arr : secondarray) {
            String[] data = arr.split(" ", 2);
            System.out.println(data[0]+ " - " +data[1]);

        }

*/

        Log.d("test is ",stringList.toString());
        mTv.append(a.toString());
        saveExcelFile();
        Toast.makeText(MainActivity.this, "data has been saved go to /storage/EXCEL/MyExcel.xls", Toast.LENGTH_LONG).show();
    }







    private void setImageCropper(Uri uri){
        CropImage
                .activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16,9)
                .setAutoZoomEnabled(true)
                .start(this);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_FROM_GALLERY);


                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }




                break;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            if (requestCode == PICK_FROM_GALLERY && data.getData() != null) {
                Uri uri = data.getData();
                setImageCropper(uri);
                Log.d(TAG, "_URI: "+uri);
            }
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri croppedImageUri = result.getUri();


                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);


                    mimage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }


//////////////////////////////////////////////////////////////////////////////////////////


    private void saveExcelFile() {
        String path;
        File dir;
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");
            return;
        }
        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");

        // Generate column headings
        Row row = null;


        int rowCount = 0;

        row = sheet1.createRow(0);

        // c = row.createCell(0);

      /*  for (String value :stringList){

            c.setCellValue(String.valueOf(stringList));
            row.createCell(1).setCellValue(String.valueOf(stringList));


            c.setCellStyle(cs);

        }
*/

        sheet1.setColumnWidth(0, (15 * 500));
       /* sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));*/
        int val = 0;
        int k = 1;
        for(int i=1;i<stringList.size();i++){
            row = sheet1.createRow(k);
            for(int j=0;j<=2;j++){
                c = row.createCell(j);
                c.setCellValue(stringList.get(i));
                //  Log.d("list is",stringList.get(i));
                c.setCellStyle(cellStyle);
                //   val++;
            }
            sheet1.setColumnWidth(i, (15 * 500));
            k++;
        }
        path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EXCEL/";
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "MyExcel.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }











}


































