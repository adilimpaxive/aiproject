package com.pakistan.textrecognition_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;


import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
RollNum num=new RollNum();
    String name,uu,studentname,studNameInner,studentatendence;
    ////////////////////////////////////////////////////////////
    public static final String PACKAGE_NAME = "com.pakistan.textrecognition_app";
    public static final String DATA_PATH = Environment
            .getExternalStorageDirectory().toString() + "/AndroidOCR/";
    public static final String lang = "eng";
    private static final String TAG = "MainActivity";
  /*  //private static final String TAG = "TESSERACT";*/
  //SharedPreferences.Editor editor = getSharedPreferences("abc", MODE_PRIVATE).edit();

   // private TessBaseAPI mTess;





    ////////////////////////////////////////////////////////////









InputStream io;

OutputStream ou;


    //  Workbook wb;
    String[] valueToWrite;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PERMISSION_REQUEST_CODE =2 ;
    private Button mBtn, mBtn_detect;
    private ImageView mimage;
    private TextView mTv;
    private AssetManager assetManager;
    private Context ctx;
    List<String> all;
    List<ArrayList> arrayLists;

    TessBaseAPI tessBaseApi = new TessBaseAPI();
    File dir;
    private String dataDi,str;
    File directory, sd, file;
    //WritableWorkbook workbook;
    Bitmap bitmap;
    String txt;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    Object a;
    ArrayList<String>lines;
    ArrayList<String>studennamearray;
    ArrayList<String> test;
    ArrayList<String> rolnumaray;
    ArrayList<String> attendancearray;
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

     test   = new ArrayList<String>();
     lines=new ArrayList<String>();








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
              //  detect();
                try {
                    text_requestObject();
                   // saveExcelFile();
                 //   getOCRResult(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });







    }




    void text_requestObject(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url="http://192.168.10.34/attendence/attendence/read_attendence.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responce is =", "Response " + response);

                try {
                    rolnumaray=new ArrayList<String>();
                    studennamearray=new ArrayList<String>();
                    attendancearray=new ArrayList<String>();
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        name = jsonobject.getString("roll_num");
                        studentname=jsonobject.getString("name");
                        studentatendence=jsonobject.getString("attendence");
                       // Toast.makeText(MainActivity.this, ""+studentatendence, Toast.LENGTH_SHORT).show();

                      //  Log.d("name is", name);

                    }



                    JSONArray jsonarray1 = new JSONArray(name);
                    for (int j = 0; j < jsonarray1.length(); j++) {
                        JSONObject jsonobject1 = jsonarray1.getJSONObject(j);
                        uu = jsonobject1.getString("roll_num");
                        rolnumaray.add(uu);
                        //Toast.makeText(MainActivity.this, ""+rolnumaray.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("is", uu);





                    }



                    JSONArray jsonarray2 = new JSONArray(studentname);
                    for (int j = 0; j < jsonarray2.length(); j++) {
                        JSONObject jsonobject2 = jsonarray2.getJSONObject(j);
                        studNameInner = jsonobject2.getString("names");
                        studennamearray.add(studNameInner);

                        //Toast.makeText(MainActivity.this, ""+studennamearray.toString(), Toast.LENGTH_SHORT).show();

                    }




                    JSONArray jarray = new JSONArray(studentatendence);
                    for (int i = 0; i < jarray.length(); i++) {
                Object oo=   jarray.getJSONArray(i);
                attendancearray.add(oo.toString());
                        Toast.makeText(MainActivity.this, ""+attendancearray.toString(), Toast.LENGTH_SHORT).show();

                      /*  JSONArray innerArray = jarray.getJSONArray(i);
                        for (int j = 0; j < innerArray.length(); j++) {
                            Object ooo = innerArray.getString(j);
                            Toast.makeText(MainActivity.this, ""+ooo.toString(), Toast.LENGTH_SHORT).show();
                        }*/



                    }











                   /* JSONArray jsonarray3 = new JSONArray(studentatendence);
                    for (int j = 0; j < jsonarray3.length(); j++) {
                       String a= String.valueOf(jsonarray3.getJSONObject(j));


                        Toast.makeText(MainActivity.this, ""+a, Toast.LENGTH_SHORT).show();

                    }
*/






                    saveExcelFile();


                    Log.d("jasonarray is", jsonarray.toString());



                } catch(JSONException e){
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {

                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            //  JSONObject em = jsonObject.getJSONObject("errors");

                            Log.d("responce 500 is", res);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }
        })


        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");

                // Toast.makeText(MainActivity.this, ""+token, Toast.LENGTH_SHORT).show();
                return params;

            }

        } ;




        queue.add(stringRequest);




    }




















    public void getOCRResult(Bitmap bitmap) {
      /*  TessBaseAPI mTess = new TessBaseAPI();
       // File myDir = getExternalFilesDir(Environment.MEDIA_MOUNTED);
        String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
       // String language = "eng";
        File dir = new File(datapath + "tessdata/");
        if (!dir.exists())
            dir.mkdirs();

            mTess.init(datapath, "eng");
*/
        tessBaseApi.setImage(bitmap);
        String result = tessBaseApi.getUTF8Text();
        Toast.makeText(MainActivity.this, "Result : " + result, Toast.LENGTH_LONG).show();
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
            String s=block.getText();


            for(FirebaseVisionText.Line line : lines){
                List<FirebaseVisionText.Element> elements = line.getElements();
               // Log.d(TAG, "_BLOCK_LINE: "+line.getText());
                //a= block.getText();
               a=line.getText();

              //  stringList.add(a.toString());




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

       // Log.d("test is ",stringList.toString());
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
        Cell c1 = null;
        Row row1=null;
        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        String[] columns = {"Roll num", "Name", "Attendance"};
        //New Sheet
       Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");
       // Row row=null;
        // Generate column headings









        // Create a Font for styling header cells
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet1.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        /*CellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
*/
        // Create Other rows and cells with employees data






















        Row row = null;
      //  Row row1=null;
     //   int rowCount = 0;

        //  row = sheet1.createRow(0);



       /* //This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
        data.put("2", new Object[] {rolnumaray.toString(), studennamearray.toString(), attendancearray.toString()});


        //Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet1.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
            }

        }*/


        sheet1.setColumnWidth(1, (15 * 800));
        sheet1.setColumnWidth(2, (15 * 800));
        sheet1.setColumnWidth(3, (15 * 800));
        int rowNum = 0;
        //int k = 0;
        for(int i=0;i<rolnumaray.size();i++){

            row = sheet1.createRow(rowNum++);
            row.createCell(0)
                    .setCellValue(rolnumaray.get(i));





           /* row = sheet1.createRow(0);

               c = row.createCell(0);
            c.setCellValue(attendancearray.get(i));

              c.setCellStyle(cellStyle);

*/
            sheet1.setColumnWidth(i, (15 * 800));

        }











/*

        for (int j=0;j<rolnumaray.size();j++){
            row = sheet1.createRow(1);

            c1 = row.createCell(0);
            c1.setCellValue(attendancearray.get(j));

            c1.setCellStyle(cellStyle);


            sheet1.setColumnWidth(j, (15 * 800));


        }

        for(int l=0;l<studennamearray.size();l++){
            row1 = sheet1.createRow(1);
         c1=row1.createCell(1);
           c1.setCellValue(studennamearray.get(l));
            //  Log.d("list is",stringList.get(i));
            c1.setCellStyle(cellStyle);
            //   val++;
            sheet1.setColumnWidth(l, (15 * 500));


        }










               int l = 0;
        for(int j=0;j<studennamearray.size();j++){
            row = sheet1.createRow(l);

            c1 = row.createCell(1);
            c1.setCellValue(studennamearray.get(j));
            //  Log.d("list is",stringList.get(i));
            c1.setCellStyle(cellStyle);
            //   val++;

            sheet1.setColumnWidth(j, (15 * 500));
            l++;
        }



*/










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

///////////////////////////////////////////////////////////////////


    public void TessOCR(AssetManager assetManager) {

        Log.i(TAG, DATA_PATH);

        this.assetManager = assetManager;

        String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }
        }

        if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
            try {



                io = assetManager.open("tessdata/" + lang + ".traineddata");
              ou = new FileOutputStream(new File(DATA_PATH + "tessdata/", lang + ".traineddata"));

                byte[] buf = new byte[1024];
                int len;
                while ((len = io.read(buf)) != -1) {
                    ou.write(buf, 0, len);
                }
                io.close();
                ou.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }


        tessBaseApi.setDebug(true);
        tessBaseApi.init(DATA_PATH, lang);

    }

   /* public String getResults(Bitmap bitmap)
    {
        tessBaseApi.setImage(bitmap);
        String result = tessBaseApi.getUTF8Text();
        return result;
    }
*/


////////////////////////////////////////////////



}


































