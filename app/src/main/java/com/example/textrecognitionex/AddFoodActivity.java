package com.example.textrecognitionex;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.textrecognitionex.ItemActivity;
import com.example.textrecognitionex.ItemViewModel;
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
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private String refrigeratorName;
    private String category;

    private ItemViewModel viewModel;
    private DatabaseReference itemDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        getSupportActionBar().setTitle("음식 추가하기");

        Intent intent = getIntent();
        refrigeratorName = intent.getStringExtra("refrigeratorName"); // 냉장고 이름 받아오기
        category = intent.getStringExtra("category"); // 카테고리 이름 받아오기
        viewModel = intent.getParcelableExtra("itemViewModel"); // item 뷰모델 받아오기

        color = intent.getExtras().getInt("theme");

        // 테마 설정?
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
        dialog.setContentView(R.layout.dialog_add_item);
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
                //onSelectImageClick(v);
                //TextRecognition(recognizer);

                // 여기부터 수정사항
                NotificationManager notificationManager= (NotificationManager)AddFoodActivity.this.getSystemService(AddFoodActivity.this.NOTIFICATION_SERVICE);
                Intent intent1 = new Intent(AddFoodActivity.this.getApplicationContext(),AddFoodActivity.class); //인텐트 생성.
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);//현재 액티비티를 최상으로 올리고, 최상의 액티비티를 제외한 모든 액티비티를 없앤다.
                PendingIntent pendingNotificationIntent = PendingIntent.getActivity( AddFoodActivity.this,0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setSmallIcon(R.drawable.food).setTicker("HETT").setWhen(System.currentTimeMillis()).setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용").setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true);
                notificationManager.notify(1, builder.build()); // Notification send
            }
        });

        // 음식 유통기한 사진 추가
        button_add_expiration_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = 1;
                //onSelectImageClick(v);
                //TextRecognition(recognizer);
            }
        });

        button_recognize_expiration_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognition(recognizer);
            }
        });


        button_return_from_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    public void onSelectImageClick(View view) {
//        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            System.out.println("resultCode = " + resultCode);
//            if(resultCode == RESULT_OK) { // 크롭 성공
//                uri = result.getUri();
//                if (photo == 0)
//                    imageView.setImageURI(uri);
//                else
//                    imageView2.setImageURI(uri);
//                setImage(uri);
//            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) { // 크롭 실패
//
//            }
//        }
//    }

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


//    private void cropImage(Uri uri) {
//        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
//                .setCropShape(CropImageView.CropShape.RECTANGLE)
//                .start(this);
//    }

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

//    private void show() {
//        // 다이얼로그에 이미지도 추가하기
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("음식 추가");
//        builder.setMessage("유통기한이 " + expirationdate.getText() + "까지인 " + foodname.getText() + "을 추가하시겠습니까?\n메모 : " + memo.getText());
//        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                databaseReference = FirebaseDatabase.getInstance().getReference();
//                databaseReference.child("냉장고").child("생선").setValue("");
//
//                databaseReference = FirebaseDatabase.getInstance().getReference();
//                String key = databaseReference.child("냉장고").child("냉장고1").child("생선").getKey();
//                String value = databaseReference.child("냉장고").child("냉장고1").child(key).toString();
//
//                Log.e("AddFoodActivity", "key = " + key);
//                Log.e("AddFoodActivity", "value = " + value);
//                Toast.makeText(getApplicationContext(),"음식이 추가되었습니다",Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(),"음식 추가가 취소되었습니다.",Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

    public void createItemInDatabase(String item) {
        itemDatabaseReference = FirebaseDatabase.getInstance().getReference();
        itemDatabaseReference.child("냉장고").child(refrigeratorName).child(category).child(item).setValue("");
    }

    public void createExpirationDate(String item, String date) {
        itemDatabaseReference = FirebaseDatabase.getInstance().getReference();
        itemDatabaseReference.child("냉장고").child(refrigeratorName).child(category).child(item).child(date).setValue("");
    }

    public void createMemo(String item, String memo) {
        itemDatabaseReference = FirebaseDatabase.getInstance().getReference();
        itemDatabaseReference.child("냉장고").child(refrigeratorName).child(category).child(item).child(memo).setValue("");
    }

    public Date StringToDate(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try{
            d = format.parse(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public void addItemBtnClick(View v) {
        String name = foodname.getText().toString(); // 입력한 이름 받아오고
        String ed = expirationdate.getText().toString(); // 유통기한 받아오기
        String mm = memo.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_item, null);
        builder.setView(layout);

        TextView addItemTextView = findViewById(R.id.addItemTextView);
        String s = name + "을 추가하시겠습니까?";
        //addItemTextView.setText(s);

        Context context = getApplicationContext();
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(name.equals("")) {
                    Toast.makeText(AddFoodActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT);
                    return;
                }


                //long now = System.currentTimeMillis(); // 현재 시간 가져오기
                Date date;
                Date current = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);

                try {
                    date = dateFormat.parse(ed);
                    String cur = dateFormat.format(current);
                    current = dateFormat.parse(cur); // 다시 날짜 형식으로 변환
                    //int compare = date.compareTo(current);

                    // 입력된 유통기한이 현재 날짜와 같거나 나중이어야 함
                    if (current.before(date) || current.equals(date)){
                        Calendar d = Calendar.getInstance();
                        d.setTime(date);
                        d.add(Calendar.MONTH, 1);
                        Calendar c = Calendar.getInstance();
                        c.setTime(current);
                        c.add(Calendar.MONTH, 1);
                        System.out.println("유통기한이 올바르게 입력됨, 유통기한 : " + d);
                        System.out.println("유통기한이 올바르게 입력됨, 현재 날짜 : " + c);
                        System.out.println("유통기한: " + d.get(Calendar.YEAR) +"년 " + d.get(Calendar.MONTH) + "월 " + d.get(Calendar.DAY_OF_MONTH)+"일");
                        System.out.println("현재 날짜: " + c.get(Calendar.YEAR) +"년 " + c.get(Calendar.MONTH) + "월 " + c.get(Calendar.DAY_OF_MONTH)+"일");

                        viewModel.addItem(name); // viewModel에 아이템 추가
                        viewModel.addExpirationDate(current); // 유통기한 추가

                        System.out.println(name + "의 메모 : " + memo.getText());
                        if (TextUtils.isEmpty(memo.getText().toString()))
                            memo.setText(" ");

                        viewModel.addMemo(mm);
                        createItemInDatabase(name);
                        createExpirationDate(name, ed);
                        createMemo(name, mm);

                        // 유통기한 형식 올바르면서 현재 날짜보다 나중인 경우에만 ItemActivity로 이동
                        Intent intent = new Intent(context, ItemActivity.class);
                        intent.putExtra("category", category);
                        startActivity(intent);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);
                        builder.setTitle("경고");
                        builder.setMessage("유통기한이 현재 날짜보다 빠를 수 없습니다.\n다시 입력해주세요.");

                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                } catch (ParseException e) {
                    //System.out.println("날짜 형식에 맞지 않음");

                    // 다이얼로그 띄우고 날짜 다시 입력받기
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);
                    builder.setTitle("경고");
                    builder.setMessage("잘못된 유통기한입니다. 다시 입력해주세요.\n올바른 유통기한 형식: 0000-00-00");

                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    builder.show();
                    e.printStackTrace();
                }


//                viewModel.addExpirationDate(ed); // 유통기한 추가
//                createExpirationDate(name, ed);

                Log.e("AddItemActivity", category + "에 " + name + " 추가");

                // 유통기한 형식이 잘못되어 있는 경우는 ItemActivity로 넘어가지 x
//                if (date.isLenient()) {
//                    Intent intent = new Intent(context, ItemActivity.class);
//                    intent.putExtra("category", category);
//                    startActivity(intent);
//                }

            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("AddItemActivity", "item 추가 취소");
                Intent intent = new Intent(context, ItemActivity.class);
                startActivity(intent);
            }
        });

        builder.create().show();
    }

    // 유통기한에 임박한 음식이 있으면 알림 띄워주는 함수
    public void pushAlarm() {

    }
}