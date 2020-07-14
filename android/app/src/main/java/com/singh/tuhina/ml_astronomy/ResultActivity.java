package com.singh.tuhina.ml_astronomy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {
    LinearLayout ll;
    private StorageReference storage_ref, img_ref;
    ImageView ivclass;
    TextView tvclass, tvconf, tvprocess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int gsq_class = getIntent().getIntExtra("CLASS", 0);
        float confidence = getIntent().getFloatExtra("CONFIDENCE", 0);

        ll = findViewById(R.id.ll);
        ivclass = findViewById(R.id.ivclass);
        tvclass = findViewById(R.id.tvclass);
        tvconf = findViewById(R.id.tvconf);
        tvprocess = findViewById(R.id.tvprocess);

        storage_ref = FirebaseStorage.getInstance().getReference();
        if (gsq_class == 0) {
            img_ref = storage_ref.child("galaxy.jpg");
            tvclass.setText("GALAXY");
        }
        else if (gsq_class == 1) {
            img_ref = storage_ref.child("star.jpg");
            tvclass.setText("STAR");
        }
        else if (gsq_class == 2) {
            img_ref = storage_ref.child("quasar.jpg");
            tvclass.setText("QUASAR");
        }

        tvconf.setText("Confidence:   " + String.valueOf(confidence));

        try {
            final File imgfile = File.createTempFile("gsq_image", "jpg");
            img_ref.getFile(imgfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap b = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
                            ivclass.setImageBitmap(b);
                            ivclass.setScaleType(ImageView.ScaleType.FIT_XY);

                            ll.setBackgroundColor(Color.BLACK);
                            tvprocess.setVisibility(View.GONE);
                            ivclass.setVisibility(View.VISIBLE);
                            tvclass.setVisibility(View.VISIBLE);
                            tvconf.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            ll.setBackgroundColor(Color.BLACK);
                            tvprocess.setVisibility(View.GONE);
                            ivclass.setVisibility(View.VISIBLE);
                            tvclass.setVisibility(View.VISIBLE);
                            tvconf.setVisibility(View.VISIBLE);
                            Toast.makeText(ResultActivity.this, "Unable to fetch image.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
