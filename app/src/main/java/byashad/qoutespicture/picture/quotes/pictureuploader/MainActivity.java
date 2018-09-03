package byashad.qoutespicture.picture.quotes.pictureuploader;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref=database.getReference();
    DatabaseReference urlofimage=ref.child("urls");
    StorageReference mStorageRef;
    private static int Gallery = 4;
    int uploadornot=0;
    //notbro
    HashMap<String ,String> hashMap=new HashMap<>();
    Spinner spinner;

    Uri downloadurl;
    Uri uri;

    Button uplad,uplaodnow;
    ImageView upladedimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        uplad=findViewById(R.id.upload);
        upladedimage=findViewById(R.id.upladimagesee);
        uplaodnow=findViewById(R.id.uploadnow);
        spinner=findViewById(R.id.spinner);

        String type[]={"motivation","love","attitude","funny","techfacts"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,type);
        spinner.setAdapter(arrayAdapter);


        uplad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,Gallery);
            }
        });
        uplaodnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadornot==1)
                {
                    StorageReference filename=mStorageRef.child("Status/"+uri.getLastPathSegment()+".png");
                    filename.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                            downloadurl=taskSnapshot.getDownloadUrl();
                            String downlaodurltostring=downloadurl.toString();
                            hashMap.put("imageurl",downlaodurltostring);
                            hashMap.put("type",spinner.getSelectedItem().toString());
                            hashMap.put("likes","0");
                            urlofimage.push().setValue(hashMap);





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    Toast.makeText(MainActivity.this, "PLEASE SELECT IMAGE", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery)
        {
            try {
                uri=data.getData();
                upladedimage.setImageURI(uri);
                uploadornot=1;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "NO IMAGE SELECTED", Toast.LENGTH_SHORT).show();

            }


        }
    }
}
