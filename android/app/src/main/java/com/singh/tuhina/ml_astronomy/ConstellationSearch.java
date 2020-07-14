package com.singh.tuhina.ml_astronomy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class ConstellationSearch extends AppCompatActivity {
    Button btnstart;
    ImageView ivcons;
    private final int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constellation_search);

        btnstart = findViewById(R.id.btnstart);
        ivcons = findViewById(R.id.ivcons);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap b = (Bitmap) data.getExtras().get("data");
                textrecog(b);
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(ConstellationSearch.this, "Operation cancelled by user.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ConstellationSearch.this, "Failed to capture image.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void textrecog(Bitmap b) {
        FirebaseApp.initializeApp(this);
        FirebaseVisionImage img = FirebaseVisionImage.fromBitmap(b);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        detector.processImage(img)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        for (FirebaseVisionText.TextBlock b : firebaseVisionText.getTextBlocks()) {
                            String text = b.getText().toLowerCase().trim();
                            Toast.makeText(ConstellationSearch.this, text, Toast.LENGTH_SHORT).show();

                            Context context = ivcons.getContext();
                            int id = context.getResources().getIdentifier(text, "drawable", context.getPackageName());
                            if (id == 0) {
                                Toast.makeText(ConstellationSearch.this, "Image not found.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ivcons.setImageResource(id);
                                ivcons.setScaleType(ImageView.ScaleType.FIT_XY);
                                }
                            }
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConstellationSearch.this, "Failed to recognize text. Please retake picture.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
