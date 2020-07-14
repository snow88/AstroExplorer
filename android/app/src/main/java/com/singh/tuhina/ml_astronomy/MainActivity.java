package com.singh.tuhina.ml_astronomy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    EditText et1, et2, et3, et4, et5, et6, et7, et8, et9, et10, et11, et12, et13;
    Button btng, btns, btnq;
    FloatingActionButton btnresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        et7 = findViewById(R.id.et7);
        et8 = findViewById(R.id.et8);
        et9 = findViewById(R.id.et9);
        et10 = findViewById(R.id.et10);
        et11 = findViewById(R.id.et11);
        et12 = findViewById(R.id.et12);
        et13 = findViewById(R.id.et13);

        btng = findViewById(R.id.btng);
        btns = findViewById(R.id.btns);
        btnq = findViewById(R.id.btnq);
        btnresult = findViewById(R.id.btnresult);

        btng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("184.189574");
                et2.setText("99.482");
                et3.setText("19.25667");
                et4.setText("17.54869");
                et5.setText("16.63578");
                et6.setText("16.14922");
                et7.setText("15.76639");
                et8.setText("752");
                et9.setText("4");
                et10.setText("271");
                et11.setText("720.87");
                et12.setText("288");
                et13.setText("520.00");
            }
        });

        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("183.8833");
                et2.setText("102.557");
                et3.setText("17.55025");
                et4.setText("16.26342");
                et5.setText("16.43869");
                et6.setText("16.55492");
                et7.setText("16.61326");
                et8.setText("752");
                et9.setText("4");
                et10.setText("269");
                et11.setText("5.9");
                et12.setText("3306");
                et13.setText("512");
            }
        });

        btnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("184.3506");
                et2.setText("207.23");
                et3.setText("18.73832");
                et4.setText("18.60962");
                et5.setText("18.39696");
                et6.setText("18.31174");
                et7.setText("17.97663");
                et8.setText("752");
                et9.setText("4");
                et10.setText("272");
                et11.setText("2719.37");
                et12.setText("287");
                et13.setText("520.23");
            }
        });

        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modelfile = "converted_model.tflite";
                Interpreter tflite;
                try {
                    tflite = new Interpreter(loadModelFile(MainActivity.this, modelfile));

                    float in[][] = new float[][]{{Float.parseFloat(et1.getText().toString()), Float.parseFloat(et2.getText().toString()),
                            Float.parseFloat(et3.getText().toString()), Float.parseFloat(et4.getText().toString()),
                            Float.parseFloat(et5.getText().toString()), Float.parseFloat(et6.getText().toString()),
                            Float.parseFloat(et7.getText().toString()), Float.parseFloat(et8.getText().toString()),
                            Float.parseFloat(et9.getText().toString()), Float.parseFloat(et10.getText().toString()),
                            Float.parseFloat(et11.getText().toString()), Float.parseFloat(et12.getText().toString()),
                            Float.parseFloat(et13.getText().toString())}};
                    float out[][] = new float[][]{{0, 0, 0}};

                    tflite.run(in, out);

                    int maxidx = -1;
                    float maxval = -1;
                    for (int k=0; k<3; k++) {
                        if (out[0][k] > maxval) {
                            maxval = out[0][k];
                            maxidx = k;
                        }
                    }

                    Intent i = new Intent(MainActivity.this, ResultActivity.class);
                    i.putExtra("CLASS", maxidx);
                    i.putExtra("CONFIDENCE", maxval);
                    startActivity(i);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
