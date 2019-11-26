package com.example.android.lookwhatiamwaring2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddItem extends AppCompatActivity {


    String sDes, sUrl,sDate;
    CalendarView jcal;
    EditText jetDes;
    ImageView jimg;
    Button jbtnSave;
    ProgressBar progressBar;
    Uri uri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference( "User Data");
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_item_activity );

        jcal = findViewById( R.id.calendarViewAdd );
        jetDes = findViewById( R.id.etAddDes );
        jimg = findViewById( R.id.imgAdd );
        jbtnSave = findViewById( R.id.btnSave );
        progressBar = findViewById( R.id.progressBar );
        progressBar.setVisibility(View.GONE);

        sDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        storageReference = FirebaseStorage.getInstance().getReference("images");

        jimg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType( "image/*" );
                intent.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( Intent.createChooser( intent, "Select Picture" ), 1 );
            }
        } );
        jbtnSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean gotData;
                gotData = getDescription();
                if(gotData)
                {
                    if(jimg.getDrawable() == null)
                    {
                        Toast.makeText( getApplicationContext(), "Select image", Toast.LENGTH_SHORT ).show();
                    }
                    else {
                        jbtnSave.setEnabled( false );
                        jbtnSave.setText( "Uploading..." );
                        progressBar.setVisibility(View.VISIBLE);
                        uploadImage();
                    }
                }
            }
        }  );

        jcal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                sDate = date+ "-"+month+"-"+year;
            }
        });

    }

    private boolean getDescription() {
        sDes = jetDes.getText().toString();
        if (sDes.length() < 0){
            jetDes.setError( "Enter description" );
            return false;
        }
        return true;
    }

    private void uploadImage() {
        try {
            final StorageReference ref = storageReference.child(  UUID.randomUUID().toString() );

            //uploads the image
            ref.putFile( uri ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri downloadUrl = uri;
                            sUrl = downloadUrl.toString();
                            insertData( sUrl );
                        }
                    } ).addOnFailureListener( new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText( getApplicationContext(), "Error while uploading image" + exception.getMessage(), Toast.LENGTH_LONG ).show();
                        }
                    } );


                }
            } );
        }catch (Exception e){}
    }

    private void insertData(String sUrl) {
        FBInsertData fbInsertData = new FBInsertData( sDes, sDate, Constant.EMAIL,sUrl );
        myRef.push().setValue( fbInsertData );
        Toast.makeText( this, "Saved", Toast.LENGTH_SHORT ).show();
        progressBar.setVisibility(View.GONE);

        jimg.setImageBitmap(null);
        jetDes.setText( "" );
        jbtnSave.setEnabled( true );
        jbtnSave.setText( "Save" );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                jimg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
