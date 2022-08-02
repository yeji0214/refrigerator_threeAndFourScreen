package com.example.textrecognitionex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_CODE = 2;
    public int color = 0;
    public static Context mContext;

    ImageView imageView;    // 갤러리에서 가져온 이미지를 보여줄 뷰
    Uri uri;                // 갤러리에서 가져온 이미지에 대한 Uri
    Bitmap bitmap;          // 갤러리에서 가져온 이미지를 담을 비트맵
    InputImage image;       // ML 모델이 인식할 인풋 이미지
    TextView text_info;     // ML 모델이 인식한 텍스트를 보여줄 뷰
    Button btn_get_image, btn_detection_image;  // 이미지 가져오기 버튼, 이미지 인식 버튼
    // 임의 추가
    Button btn_to_setting, btn_to_add_food;

    TextRecognizer recognizer;    //텍스트 인식에 사용될 모델

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_category);

        //ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(activityMainBinding.getRoot());
        //activityMainBinding.setMyVariable(0);
        //mContext = this;

        imageView = findViewById(R.id.imageView);
        text_info = findViewById(R.id.text_info);
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);    //텍스트 인식에 사용될 모델

        // GET IMAGE 버튼
        btn_get_image = findViewById(R.id.btn_get_image);
        btn_get_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
                /*Intent intent = new Intent(Intent.ACTION_PICK); // Intent.ACTION_PICK은 앨범을 호출할 때 사용하는 액션
                intent.putExtra("crop", true);
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE); // 가져오는 것의 type을 image로
                startActivityForResult(intent, 0); // 새 액티비티를 열어주면서 결과값 전달 */
            }
        });

        // IMAGE DETECTION 버튼
        btn_detection_image = findViewById(R.id.btn_detection_image);
        btn_detection_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognition(recognizer);
            }
        });

        // 임의 추가
        btn_to_setting = findViewById(R.id.button_to_setting);
        btn_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingActivity.class);
//                intent.putExtra("state", "kill");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("theme", color);
                startActivityForResult(intent, 101);
            }
        });
        btn_to_add_food = findViewById(R.id.button_to_add_food);
        btn_to_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddFoodActivity.class);
//                intent.putExtra("state", "kill");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("theme", color);
                startActivity(intent);
            }
        });
    }

    public void onSelectImageClick(View view) {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    // startActivityForResult()이 호출되어서 새 액티비티가 열리면 결과를 받는 곳곳
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            System.out.println("resultCode = " + resultCode);
            if(resultCode == RESULT_OK) { // 크롭 성공
                uri = result.getUri();
                imageView.setImageURI(uri);
                setImage(uri);
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) { // 크롭 실패

            }
        }
        if (requestCode == 101) {
            color = data.getExtras().getInt("theme");
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
            imageView.setImageBitmap(bitmap); // imageView에 비트맵으로 변환한 사진 넣기

            image = InputImage.fromBitmap(bitmap, 0); // InputImage 생성
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
                        text_info.setText(resultText);  // 인식한 텍스트를 TextView에 세팅
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

}
