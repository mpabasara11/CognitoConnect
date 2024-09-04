package com.cognitoapps.cognitoconnect.Controllers;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.cognitoapps.cognitoconnect.Controllers.Adapters.Adapter_Conversation;
import com.cognitoapps.cognitoconnect.Models.Model_Chat;
import com.cognitoapps.cognitoconnect.Models.Model_Conversation;
import com.cognitoapps.cognitoconnect.Models.Model_Current_User;
import com.cognitoapps.cognitoconnect.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.Interpreter;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Controller_Chat extends CameraActivity implements CvCameraViewListener2 {


    EditText input_message;
    TextView lbl_username,lbl_emotion;
    Button btn_send_message,btn_settings;
    ProgressDialog loading_bar;
    String chat_owner,chat_recipient,chat_id;



    private CameraBridgeViewBase mOpenCvCameraView;
    private CascadeClassifier faceCascade;

    private Interpreter tflite;

    private Handler handler = new Handler(Looper.getMainLooper());


boolean x;
     RecyclerView recyclerView;
     Adapter_Conversation adapterConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_chat);


        input_message = findViewById(R.id.txt_new_message);
        lbl_username = findViewById(R.id.lbl_receiver_username);
        btn_send_message = findViewById(R.id.btn_send_message);
        btn_settings = findViewById(R.id.btn_settings);
        lbl_emotion = findViewById(R.id.lbl_emotion);



        recyclerView = findViewById(R.id.rcl_chat_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        chat_owner = Model_Current_User.usrStore.getPhone();
        chat_recipient = getIntent().getStringExtra("chat_recipient");
        chat_id = getIntent().getStringExtra("chat_id");





        final DatabaseReference RootRef1;
        RootRef1 = FirebaseDatabase.getInstance().getReference();

        RootRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Chat_log").child(chat_owner).child(chat_recipient).exists()) {

                    Model_Chat chatData = snapshot.child("Chat_log").child(chat_owner).child(chat_recipient).getValue(Model_Chat.class);

                    if (chatData != null) {

                    /////////////////////////////////

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lbl_username.setText("Recipient: "+ chat_recipient);


        FirebaseRecyclerOptions<Model_Conversation> options =
                new FirebaseRecyclerOptions.Builder<Model_Conversation>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Chats").child(chat_id), Model_Conversation.class)
                        .build();

        adapterConversation = new Adapter_Conversation(options, this);
        recyclerView.setAdapter(adapterConversation);



        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

 if(!(input_message.getText().toString().isEmpty()))
 {
     final DatabaseReference RootRef;
     RootRef = FirebaseDatabase.getInstance().getReference();

     String timestamp = String.valueOf(System.currentTimeMillis());

     HashMap<String, Object> message = new HashMap<>();
     message.put("sender", chat_owner);
     message.put("message", input_message.getText().toString());
     message.put("timestamp",timestamp);


     RootRef.child("Chats").child(chat_id).child(timestamp).updateChildren(message);

     input_message.getText().clear();
 }
 else
 {
     input_message.setError("You did not enter any message !");
     input_message.requestFocus();
 }

  }
        });



btn_settings.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        Intent intent = new Intent(Controller_Chat.this, Controller_Chat_Settings.class);
        intent.putExtra("chat_recipient", chat_recipient);
        intent.putExtra("chat_id", chat_id);
        startActivity(intent);


    }
});


        /////////////////////////////////////////////////
        if (OpenCVLoader.initLocal()) {


            Log.d(TAG, "OpenCV loaded successfully");
        } else {

            Log.d(TAG, "OpenCV initialization failed!");
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setCameraIndex(1);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

         mOpenCvCameraView.setAlpha(0);

        mOpenCvCameraView.setCvCameraViewListener(this);



        try {
            InputStream is = getResources().getAssets().open("haarcascade_frontalface_default.xml");
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            faceCascade = new CascadeClassifier(cascadeFile.getAbsolutePath());
            if (faceCascade.empty()) {
                faceCascade = null;
            } else {
                cascadeDir.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
       // hlw.setText("sdasd");



        try {
            tflite = new Interpreter(loadModelFile());
            Log.d("tgmodel", "Model loaded successfully");
            if (tflite != null) {
                Log.d("tgmodel", "Model initialized successfully");
            } else {
                Log.d("tgmodel", "Model initialization failed");
            }


        } catch (IOException e) {
            e.printStackTrace();

            Log.d("tgmodel", "model issue");
        }
        ////////////////////////////////////////


    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        Intent intent = new Intent(Controller_Chat.this, Controller_Home.class);
        startActivity(intent);
    }



    @Override
    protected void onStart() {
        super.onStart();
        adapterConversation.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

            adapterConversation.stopListening();

        //kill the activity when screen is off
        finish();
    }


    ///////////////////////////

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {


        Mat frame = inputFrame.rgba();

        Log.d(TAG, "Frame size: " + frame.size());

        // Rotate the frame
        Mat rotatedFrame = new Mat();
        int orientation = getCameraOrientation();
        Log.d(TAG, "Camera orientation: " + orientation);

        switch (orientation) {
            case 90:
                Core.rotate(frame, rotatedFrame, Core.ROTATE_90_COUNTERCLOCKWISE);
                break;
            case 180:
                Core.rotate(frame, rotatedFrame, Core.ROTATE_180);
                break;
            case 270:
                Core.rotate(frame, rotatedFrame, Core.ROTATE_90_CLOCKWISE);
                break;
            default:
                rotatedFrame = frame.clone();
                break;
        }

        Log.d(TAG, "Rotated frame size: " + rotatedFrame.size());


        MatOfRect faces = new MatOfRect();
        if (faceCascade != null) {
            faceCascade.detectMultiScale(rotatedFrame, faces, 1.1, 10, 2,
                    new org.opencv.core.Size(30, 30),  new org.opencv.core.Size());
        }

        Rect[] facesArray = faces.toArray();
        for (Rect rect : facesArray) {
            Imgproc.rectangle(rotatedFrame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 3);

            // Extract face ROI and preprocess
            Mat faceROI = new Mat(rotatedFrame, rect);
            ByteBuffer inputBuffer = preprocessFace(faceROI);


            // Run the model
            float[][] output = new float[1][7]; // Assuming 7 emotions
            tflite.run(inputBuffer, output);

            // Interpret the results
            int maxIndex = 0;
            float maxConfidence = output[0][0];
            for (int i = 1; i < output[0].length; i++) {
                if (output[0][i] > maxConfidence) {
                    maxConfidence = output[0][i];
                    maxIndex = i;
                }
            }

            String[] emotions = {"Angry", "Natural", "Disgust", "Contempt", "Happy", "Sad", "Suprised"};
            String detectedEmotion = emotions[maxIndex];

            Log.d("emotion", "Detected emotion: " + detectedEmotion);

            // Update text field on main thread using Handler
            handler.post(new Runnable() {
                @Override
                public void run() {
                    lbl_emotion.setText("Emotion - "+detectedEmotion);
                }
            });

        }


        return rotatedFrame;
    }

    private int getCameraOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int orientation;
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = 90;
                break;
            case Surface.ROTATION_90:
                orientation = 0;
                break;
            case Surface.ROTATION_180:
                orientation = 270;
                break;
            case Surface.ROTATION_270:
                orientation = 180;
                break;
            default:
                orientation = 90;
                break;
        }
        return orientation;
    }

    private ByteBuffer preprocessFace(Mat face) {
        Mat resizedFace = new Mat();
        Imgproc.resize(face, resizedFace, new Size(64, 64));
        Bitmap bitmap = Bitmap.createBitmap(resizedFace.cols(), resizedFace.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(resizedFace, bitmap);

        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * 64 * 64 * 3);
        inputBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[64 * 64];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int pixelValue : intValues) {
            int r = (pixelValue >> 16) & 0xFF;
            int g = (pixelValue >> 8) & 0xFF;
            int b = pixelValue & 0xFF;

            inputBuffer.putFloat(r / 255.0f);
            inputBuffer.putFloat(g / 255.0f);
            inputBuffer.putFloat(b / 255.0f);
        }

        return inputBuffer;
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.enableView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    ///////////////////////////////
}