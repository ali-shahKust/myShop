package com.tool.kustiit.myshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private TextView changeImg, updateTxt;
    private TextView phoneNumber;
    private CircleImageView dP;
    private static final int GallaryPick = 1;

    private Uri imageUri;
    boolean exists = false;
    private StorageReference productImagesRef;

    private DatabaseReference mRootRef;
    private String  downloadImgUrl;

    private ProgressDialog mprogressBar;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mAuth =FirebaseAuth.getInstance();
        dP = (CircleImageView) findViewById(R.id.setting_profileImg);

        phoneNumber = (TextView) findViewById(R.id.set_phoneNumber);


        updateTxt = (TextView) findViewById(R.id.updateTxt);


        checkImage();
        dP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGalleryImage();

            }
        });
        //PhoneAuthProvider auth = PhoneAuthProvider.getInstance();

        FirebaseUser current_user_id =mAuth.getCurrentUser();
        final String uid = current_user_id.getUid();
        final DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference().child("Profile_image").child(uid);
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    String getimage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(getimage).placeholder(R.drawable.profile).into(dP);
                }
                catch (Exception error){
                    error.getMessage();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateImage();

            }
        });

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String username = dataSnapshot.child("user_number").getValue().toString();
//                     String getimage = dataSnapshot.child("image").getValue().toString();
                phoneNumber.setText(username);


                // Picasso.get().load(getimage).placeholder(R.drawable.profile).into(myImageview);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void ValidateImage() {
        if (imageUri == null) {
            if (exists) {
                //TODO: update name only
            } else {
                Toast.makeText(SettingsActivity.this, "Select an image first", Toast.LENGTH_SHORT).show();
            }
        } else {
            {
                StoreProductInformation();
            }
        }

    }

    private void OpenGalleryImage() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GallaryPick);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GallaryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            dP.setImageURI(imageUri);

        }
    }

    private void StoreProductInformation() {




        //Image link for Product
        // ProductRandomKey = saveCurrentDate+saveCurrentTime;
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Users").child("user_image");
        FirebaseUser current_user_id = mAuth.getCurrentUser();
        final String uid = current_user_id.getUid();
        mRootRef = FirebaseDatabase.getInstance().getReference("Profile_image").child(uid);
        final StorageReference filepath = productImagesRef.child(uid + ".jpg");
        // imageUri.getLastPathSegment()+ProductRandomKey

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SettingsActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImgUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }


                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImgUrl = task.getResult().toString();


                            Toast.makeText(SettingsActivity.this, "Getting Url of Image", Toast.LENGTH_SHORT).show();

                            mRootRef.setValue(downloadImgUrl);

                            SaveProductInfo();


                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image", downloadImgUrl);

        mRootRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                    Toast.makeText(SettingsActivity.this, "Image Upload Successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                    // mprogressBar.dismiss();
                } else {
                    //mprogressBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(SettingsActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkImage() {
        FirebaseDatabase.getInstance().getReference("Profile_image").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    exists = true;
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        String image_url = i.getValue(String.class);
                        if (image_url != null && image_url.equals("")) {
                            Picasso.get().load(imageUri).into(dP);
                        }
                    }
                } else {
                    exists = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
