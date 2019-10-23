package com.pakistan.textrecognition_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    private Button mBtn,mBtn_detect;
    private ImageView mimage;
    private TextView mTv;
    private Context ctx;
    List<String> all;
    File directory, sd, file;
    //WritableWorkbook workbook;
    Bitmap bitmap;
    String txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn=findViewById(R.id.button);
        mBtn_detect=findViewById(R.id.button2);
        mimage=findViewById(R.id.imageView);
        mTv=findViewById(R.id.textView);
      //  System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
     // System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


       /* if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;
        }*/





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


                //chooseImage();
            }
        });



        mBtn_detect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here


              detect();
            }
        });



    }







            public void detect(){
if (bitmap==null){

    Toast.makeText(this, "image is null", Toast.LENGTH_SHORT).show();



}else {
    FirebaseVisionImage firebaseVisionImage=FirebaseVisionImage.fromBitmap(bitmap);
    FirebaseVisionTextDetector firebaseVisionTextDetector =
            FirebaseVision.getInstance().getVisionTextDetector();


    firebaseVisionTextDetector.detectInImage(firebaseVisionImage).
              addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText texts) {
                    processTextRecognitionResult(texts);
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

    private void processTextRecognitionResult(FirebaseVisionText texts) {


        List<FirebaseVisionText.Block> blocks=texts.getBlocks();
        if (blocks.size()==0){


            Toast.makeText(this, "no data is there", Toast.LENGTH_SHORT).show();
        }
        else{


            for(FirebaseVisionText.Block block:texts.getBlocks())
            {

                String text=block.getText();

             all = new ArrayList<>();

                // Adding new elements to the ArrayList
              all.add(block.getText());

TestModel tm=new TestModel();


                for (int i=0; i<all.size();i++){

                    mTv.append(all.get(i));
                    Log.d("names are ",all.get(i));

                    mTv.append("\n");



                  Log.d("dd", Arrays.toString(all.get(i).split(",")));

                  Object o=Arrays.toString(all.get(2).split(","));

                  Log.d("objet is ", String.valueOf(o));

                  tm.getName(String.valueOf(o));
                  Log.d("s", ""+tm.getName(String.valueOf(o)));

                    // all.get(i).split(",");
                   // book();

                }




            }
        }
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

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {






                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                mimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

















}













  /*  private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }


    public void onActivityResult(int requestCode,int resultCode,Intent data){

        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);

                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    mimage.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                    break;

            }
*/














  /*  public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {






            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                mimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }*/







