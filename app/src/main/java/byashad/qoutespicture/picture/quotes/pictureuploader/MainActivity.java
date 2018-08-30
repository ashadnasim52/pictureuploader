package byashad.qoutespicture.picture.quotes.pictureuploader;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference();
    DatabaseReference urlofimage=ref.child("urls");
    StorageReference mStorageRef;
    private static int Gallery = 4;
    HashMap<String ,String> hashMap=new HashMap<>();
    Spinner spinner;

    Button uplad;
    ImageView upladedimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        uplad=findViewById(R.id.upload);
        upladedimage=findViewById(R.id.upladimagesee);
        spinner=findViewById(R.id.spinner);


        uplad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Gallery);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode==Gallery)
        {
            uri=data.getData();
            upladedimage.setImageURI(uri);


            StorageReference filename=mStorageRef.child("Status/"+uri.getLastPathSegment()+".png");
            filename.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
