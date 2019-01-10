package com.example.android.helloworld.DataObjects;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.android.helloworld.AddReviewActivity2;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static java.lang.Math.min;

public class CameraUpload extends Fragment {
    private static final int CAMERA_REQ_CODE = 0;
    private StorageReference storageRef;
    private boolean took_image;
    private Uri uri;
    private String image_path;
    private String currentPath;
    boolean camera_pre;
    boolean storage_pre;
    ImageView imgView;
    AddReviewActivity2 addRev;
    ImageButton addPhotoButton;
    Context cntx;
    String url;
    final FirebaseDatabase dataBase = FirebaseDatabase.getInstance();

    public CameraUpload(){

    }

    public CameraUpload(AddReviewActivity2 activity,ImageView view,
                        ImageButton b,Context cnx) {
        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.addRev = activity;
        this.took_image = true;
        this.uri = null;
        this.url = "";
        this.image_path = null;
        this.camera_pre = false;
        this.storage_pre = false;
        this.currentPath = "";
        imgView = view;
        this.cntx = cnx;
        this.addPhotoButton = b;
        image_path = "";
    }

    public String uploadImage(){

        if(took_image && uri != null){
            System.out.println("The URI: " + uri.toString());
            image_path = addImageToFirebase(uri);
            if(image_path!= ""){
                System.out.println("Should add an Image: " + image_path);

                return image_path;
            }
        }

        return image_path;
    }

    public boolean checkPermissions(){

        //Check for Camera Permission
        int cameraPermission = ContextCompat.checkSelfPermission(this.addRev.getContext(), Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(this.addRev.getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((cameraPermission != PackageManager.PERMISSION_GRANTED &&
                (storagePermission != PackageManager.PERMISSION_GRANTED))){

            ActivityCompat.requestPermissions(this.addRev.getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

            else if(cameraPermission != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions(this.addRev.getActivity(), new String[]{Manifest.permission.CAMERA},
                    1);
        }
        //Check for storage permission
        else if(storagePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.addRev.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);

        }
        else
            openCamera();

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0) // camera and storage
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {

                this.addPhotoButton.setEnabled(false); // enable the camera button
            }
            return;
        }
        else if (requestCode == 1 || requestCode == 2) // camera or storage
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                this.addPhotoButton.setEnabled(false);
            }
            return;
        }
        else //denied!
        {
            return;
        }
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(this.addRev.getActivity()!= null){
            if(intent.resolveActivity(this.addRev.getActivity().getPackageManager())!= null){
                File imageFile = null;

                try {
                    imageFile = createImageFile();
                }
                catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }
                if(imageFile != null){
                    this.uri = FileProvider.getUriForFile(this.cntx,
                        "com.example.android.toplate.provider",imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,this.uri);

                this.addRev.startActivityForResult(intent,CAMERA_REQ_CODE);
            }
        }
    }
    }

    private File createImageFile() throws NullPointerException{
        this.image_path = "toplateImg" + System.currentTimeMillis();

        File storageDir;
        try {
            storageDir = this.addRev.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(this.image_path,".jpg",storageDir);

            this.currentPath = image.getAbsolutePath();

            return image;
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }

        return null;
    }

    protected String addImageToFirebase(final Uri uri) {
        if(uri == null || this.currentPath == null)
            return "";

        //store the image in the fire store
        final StorageReference filePath = this.storageRef.child("Images").child(this.image_path);

        final UploadTask upTask = filePath.putFile(uri);

        upTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful())
                    throw task.getException();
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){

                    System.out.println("The Url: " +task.getResult().toString() );
                    updateImageUrl(task.getResult().toString());

                }
            }
        });

        upTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                took_image = true;

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error when Uploading image");
                System.out.println("Error message: " + e.getMessage());
            }
        });

        return this.image_path;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("In onActivityResult");
        System.out.println("Camera req code: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        if(requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK
                && data!=null){

            imgView.setImageURI(Uri.parse(this.currentPath));

            if(uri!= null)
                System.out.println("Uri - onActivity: " + uri.toString());
            else
                System.out.println("getData() return null");

        }
    }


    public String getImgPath(){
        return this.image_path;
    }

    public static String getUrl(final String path){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("ImagesUrl");
        final String url[] = new String[1] ;
        url[0] = "/";

        ref.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {

                MutableData urlData = mutableData.child(path);
                if(urlData.getValue() != null)
                    url[0] = urlData.getValue().toString();

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }

        });
        try {
            Thread.sleep(3000);
        } catch (java.lang.InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return url[0];

    }

    private void updateImageUrl(final String url) {
        System.out.println("image path in upadeImagUrl: " + this.image_path);
        DatabaseReference restRef = dataBase.getReference().child("ImagesUrl");

        restRef.runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(MutableData mutableData) {

                mutableData.child(image_path).setValue(url);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    System.out.println(databaseError.getMessage());
                }
            }
        });
    }


}
