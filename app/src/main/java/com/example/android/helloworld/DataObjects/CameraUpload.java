package com.example.android.helloworld.DataObjects;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.android.helloworld.AddReviewActivity2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class CameraUpload extends Fragment {
    private static final int CAMERA_REQ_CODE = 0;
    private StorageReference storageRef;
    private boolean took_image;
    private Uri uri;
    private String image_path;
    boolean camera_pre;
    boolean storage_pre;
    ImageView imgView;
    AddReviewActivity2 addRev;
    ImageButton addPhotoButton;

    public CameraUpload(){

    }
    public CameraUpload(AddReviewActivity2 activity,ImageView view, ImageButton b) {
        this.storageRef = FirebaseStorage.getInstance().getReference();
        this.addRev = activity;
        this.took_image = true;
        this.uri = null;
        this.image_path = null;
        this.camera_pre = false;
        this.storage_pre = false;
        imgView = view;
        this.addPhotoButton = b;
        image_path = "";
    }

    public String uploadImage(){

        if(took_image && uri != null){
            image_path = addImageToFirebase(uri);
            if(image_path!= ""){
                image_path = "Images/" + image_path;
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

        this.addRev.startActivityForResult(intent,CAMERA_REQ_CODE);
    }

    protected String addImageToFirebase(Uri uri) {
        if(uri == null)
            return "";

        //store the image in the fire store
        String img_path = "toplate_img_" + System.currentTimeMillis() + ".jpg";
        StorageReference filePath = this.storageRef.child("Images").child(img_path);

        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                took_image = true;
                //    Toast.makeText(getContext(), "Uploading finished ...", Toast.LENGTH_LONG).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error when Uploading image");
                System.out.println("Error message: " + e.getMessage());
            }
        });
        return img_path;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("In onActivityResult");
        System.out.println("Camera req code: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        if(requestCode == CAMERA_REQ_CODE && resultCode == RESULT_OK
                && data!=null){

            Bitmap image = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(image);
            uri = getImageUri(this.addRev.getContext(), image); // the URI of the image
            if(uri!= null)
                System.out.println("Uri - onActivity: " + uri.toString());
            else
                System.out.println("getData() return null");

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
