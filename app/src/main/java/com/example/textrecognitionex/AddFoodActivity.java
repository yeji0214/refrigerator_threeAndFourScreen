package com.example.textrecognitionex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

// 음식 추가 화면
public class AddFoodActivity extends AppCompatActivity {
    ImageView imageView, imageView2;    // 갤러리에서 가져온 이미지를 보여줄 뷰
    Uri uri;                // 갤러리에서 가져온 이미지에 대한 Uri
    Bitmap bitmap;          // 갤러리에서 가져온 이미지를 담을 비트맵
    InputImage image;       // ML 모델이 인식할 인풋 이미지
    //TextView expirationdate;     // ML 모델이 인식한 텍스트를 보여줄 뷰
    Button button_add_picture, button_add_expiration_date, button_add, button_return_from_add_food, button_recognize_expiration_date;
    TextRecognizer recognizer;
    TextView foodname, expirationdate, memo;
    Dialog dialog; // 커스텀 다이얼로그
    int color, photo = 0;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setTitle("음식 추가하기");

        Intent intent = getIntent();
        color = intent.getExtras().getInt("theme");

        if (color == 1) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFBB86FC));
            getWindow().setStatusBarColor(0xFFA566FF);
        }
        else if (color == 2) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF03DAC5));
            getWindow().setStatusBarColor(0xFF3DB7CC);
        }
        else if (color == 3) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF000000));
            getWindow().setStatusBarColor(0xFF4C4C4C);
        }
        else if (color == 4) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFB2D9));
            getWindow().setStatusBarColor(0xFFEDA0C7);
        }

        button_add_picture = findViewById(R.id.button_add_picture);
        button_add_expiration_date = findViewById(R.id.button_add_expiration_date);
        button_add = findViewById(R.id.button_add);
        button_return_from_add_food = findViewById(R.id.button_return_from_add_food);
        button_recognize_expiration_date = findViewById(R.id.button_recognize_expiration_date);
        foodname = findViewById(R.id.foodname);
        expirationdate = findViewById(R.id.expirationdate);
        memo = findViewById(R.id.memo);
        dialog = new Dialog(AddFoodActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_food);
        imageView = findViewById(R.id.imageView_add_food);
        imageView2 = findViewById(R.id.imageView_expirationdate);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // 갤러리에서 음식 사진 추가
        button_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                try {
//                    PackageManager pm = getPackageManager();
//                    final ResolveInfo mInfo = pm.resolveActivity(i, 0);
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
//                    intent.setAction(Intent.ACTION_MAIN);
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//                    startActivity(intent);
//                } catch (Exception e) {
//                    Log.i("TAG", "Unable to launch camera: " + e);
//                }
                photo = 0;
                onSelectImageClick(v);
                //TextRecognition(recognizer);
            }
        });

        // 음식 유통기한 사진 추가
        button_add_expiration_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = 1;
                onSelectImageClick(v);
                //TextRecognition(recognizer);
            }
        });

        button_recognize_expiration_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognition(recognizer);
            }
        });
        // 음식 추가
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        button_return_from_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void onSelectImageClick(View view) {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("resultCode = " + resultCode);
            if(resultCode == RESULT_OK) { // 크롭 성공
                uri = result.getUri();
                if (photo == 0)
                    imageView.setImageURI(uri);
                else
                    imageView2.setImageURI(uri);
                setImage(uri);
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) { // 크롭 실패

            }
        }
       /* if (requestCode == REQUEST_CODE) {
            // 갤러리에서 선택한 사진에 대한 uri를 가져온다.
            uri = data.getData();
            setImage(uri);
        } */
    }

    // uri를 비트맵으로 변환시킨후 이미지뷰에 띄워주고 InputImage를 생성하는 메서드
    private void setImage(Uri uri) {
        try{
            InputStream in = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(in); // 비트맵으로 변환
            if (photo == 0)
                imageView.setImageBitmap(bitmap); // imageView에 비트맵으로 변환한 사진 넣기
            else {
                imageView2.setImageBitmap(bitmap);
                image = InputImage.fromBitmap(bitmap, 0); // InputImage 생성
            }
            Log.e("setImage", "이미지 to 비트맵");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void TextRecognition(com.google.mlkit.vision.text.TextRecognizer recognizer){
        Task<Text> result = recognizer.process(image)
                // 이미지 인식에 성공하면 실행되는 리스너
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        Log.e("텍스트 인식", "성공");
                        // Task completed successfully
                        String resultText = visionText.getText(); // 인식한 텍스트
                        resultText = getDateString(resultText);
                        expirationdate.setText(resultText);  // 인식한 텍스트를 TextView에 세팅
                    }
                })
                // 이미지 인식에 실패하면 실행되는 리스너
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("텍스트 인식", "실패: " + e.getMessage());
                            }
                        });
    }


    private void cropImage(Uri uri) {
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(this);
    }

    private String getDateString(String value) {
        String str = value.replaceAll("[^0-9]", "");
        int index = 0;
        if((index = str.indexOf('2')) != -1) {
            str = str.substring(index, str.length());
        }

        // 유통기한 데이터는 2로 시작해야 함
        if(!str.startsWith("2")) {
            return "null";
        }

        // 2020년 유통기한에 대한 처리
        if(str.startsWith("20") && Integer.parseInt(str.substring(2, 4)) < 13) {
            str = "20" + str;
        }

        // 여섯 자리 유통기한 포맷의 경우 여덟 자리 포맷으로 수정
        str = !str.startsWith("20") ? "20" + str : str;

        // 여덟자리인지 확인
        if(str.substring(0, 8).length() != 8) {
            return "null";
        }

        String year = str.substring(0, 4);
        String month = str.substring(4, 6);
        String date = str.substring(6, 8);

        str = year + "-" + month + "-" + date;


        return str;
    }

    private void show() {
        // 다이얼로그에 이미지도 추가하기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("음식 추가");
        builder.setMessage("유통기한이 " + expirationdate.getText() + "까지인 " + foodname.getText() + "을 추가하시겠습니까?\n메모 : " + memo.getText());
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                databaseReference.child("냉장고").child("냉장고1").child("당근").setValue("");
                Toast.makeText(getApplicationContext(),"음식이 추가되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"음식 추가가 취소되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}