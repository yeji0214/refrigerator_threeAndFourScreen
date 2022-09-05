package com.example.textrecognitionex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognitionex.CalendarActivity;
import com.example.textrecognitionex.CategoryAdapter;
import com.example.textrecognitionex.CategoryViewModel;
import com.example.textrecognitionex.ItemActivity;
import com.example.textrecognitionex.ItemViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CategoryActivity extends AppCompatActivity {
    private String refrigeratorName; // 앞에서 전달된 냉장고 이름으로 바꿔야 됨
    private String category;

    private CategoryViewModel viewModel;
    private ItemViewModel itemviewModel;
    private RecyclerView categoryRecyclerView; // 카테고리들을 담을 리싸이클러뷰
    private CategoryAdapter categoryAdapter;

    private String editCategoryText;

    private DatabaseReference databaseCategoryReference;
    private DatabaseReference itemDatabaseReference;
    private Notification mNotification;

    Date date;
    Date current = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Log.e("CategoryActivity", "CategoryActivity 시작");
        itemDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // 수정사항 (firebase에서 메시지 전달하기)
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            System.out.println("Fetching FCM registration token failed");
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        System.out.println(token);
//                        Toast.makeText(CategoryActivity.this, "Your device registration token is" + token
//                                , Toast.LENGTH_SHORT).show();
//                    }
//                });

        //2
//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Done";
//                        if (!task.isSuccessful()){
//                            msg = "Failed";
//                        }
//                    }
//                });
//        FirebaseMessaging.getInstance().unsubscribeFromTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Done";
//                        if (!task.isSuccessful()){
//                            msg = "Failed";
//                        }
//                    }
//                });
//
//        FirebaseMessaging.getInstance().subscribeToTopic("1dayago")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Done";
//                        if (!task.isSuccessful()){
//                            msg = "Failed";
//                        }
//                    }
//                });

        refrigeratorName = "냉장고1"; // 냉장고 이름
        setTitle(refrigeratorName + "의 냉장고");

        Intent intent = getIntent();
        category = intent.getStringExtra("category"); // 카테고리 이름 받아오기
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class); // 뷰모델 생성
        itemviewModel = intent.getParcelableExtra("itemViewModel"); // item 뷰모델 받아오기

        categoryRecyclerView = (RecyclerView)findViewById(R.id.categoryRecyclerView);
        categoryAdapter = new CategoryAdapter(viewModel); // 어댑터 생성

        categoryRecyclerView.setAdapter(categoryAdapter); // 리사이클러뷰에 CategoryrAdapter 객체 지정
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 리사이클러뷰에 LinearLayoutManager 객체 지정
        categoryRecyclerView.setHasFixedSize(true); // View마다 크기 똑같게

        registerForContextMenu(categoryRecyclerView); // categoryRecyclerView가 context menu를 가질 수 있도록

        final Observer<ArrayList<String>> myObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                categoryAdapter.notifyDataSetChanged(); // 어댑터에게 데이터가 변경되었다는 것을 알림
            }
        };
        viewModel.categorysLiveData.observe(this, myObserver);

        // DB에 있는 카테고리들 가져오기
        getFirebaseDatabase();

        // 얘네 주석을 풀면 실행이 안됨,,
//        System.out.println("현재 데베에 입력된 음식 개수: " + itemviewModel.dates.size());
//        for (int i=0; i<itemviewModel.items.size(); i++){
//            //if (viewModel.dates.get(i) != null)
//            try {
//                date = dateFormat.parse(itemviewModel.dates.get(i).toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            String cur = dateFormat.format(current);
//            try {
//                current = dateFormat.parse(cur); // 다시 날짜 형식으로 변환
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if(itemviewModel.dates.get(i).getMonth()-current.getMonth() == 1){
//                System.out.println(itemviewModel.items.get(i) + "의 유통기한이 하루 남았습니다.");
//                mNotification = new Notification(this);
//                NotificationCompat.Builder nb = mNotification.getChannel1Notification("나의 냉장고", itemviewModel.items.get(i) + "의 유통기한이 하루 남았습니다.");
//                mNotification.getManager().notify(1, nb.build());
//            }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 상단의 달력, 설정 메뉴 만들어질 때
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calender: // '달력' 버튼 선택
                // 달력 Activity로 이동
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.setting: // '설정' 버튼 선택
                // 설정 Activity로 이동
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        ((Activity)view.getContext()).getMenuInflater().inflate(R.menu.category_context_menu, contextMenu);
        Log.e("MainActivity", "onCreateContextMenu()");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: // 카테고리 삭제하기
                Log.e("delete", "longClickPosition = " + viewModel.longClickPosition);
                // 데이터베이스에도 삭제해야됨
                String deleteName = viewModel.categorys.get(viewModel.longClickPosition);
                Log.e("delete", "delete name = " + deleteName);
                deleteCategoryInDatabase(viewModel.categorys.get(viewModel.longClickPosition));
                viewModel.deleteItem(viewModel.longClickPosition);
                break;
        }
        return true;
    }


    // 데이터베이스에 새로운 카테고리를 만드는 함수
    public void createCategoryInDatabase(String category) {
        databaseCategoryReference = FirebaseDatabase.getInstance().getReference();
        // "냉장고"/냉장고 이름/입력받은 카테고리
        databaseCategoryReference.child("냉장고").child(refrigeratorName).child(category).setValue("");
    }

    // 데이터베이스에서 카테고리를 삭제하는 함수
    public void deleteCategoryInDatabase(String category) {
        databaseCategoryReference = FirebaseDatabase.getInstance().getReference();
        // "냉장고"/냉장고 이름/입력받은 카테고리 를 삭제
        databaseCategoryReference.child("냉장고").child(refrigeratorName).child(category).removeValue();
    }

    public void getFirebaseDatabase() {
        Log.e("CategoryActivity", "getFirebaseDatabase()");
        FirebaseDatabase.getInstance().getReference().child("냉장고").child(refrigeratorName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    String key = data.getKey(); // key = 카테고리 이름
                    viewModel.addCategory(key); // 뷰모델에 넣어주기
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // 카테고리 추가하는 버튼을 누르면 실행되는 함수
    public void categoryAddButtonClick(View v) {
        Log.e("CategoryActivity", "categoryAddButtonClick");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(layout);

        EditText editCategory = (EditText) layout.findViewById(R.id.editCategory);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editCategoryText = editCategory.getText().toString(); // 입력한 카테고리 받아오고
                if(editCategoryText.equals("")) {
                    Toast.makeText(CategoryActivity.this, "카테고리를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.addCategory(editCategoryText); // viewModel에 카테고리 추가
                createCategoryInDatabase(editCategoryText); // 데이터베이스에 카테고리 추가
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    // 카테고리 클릭 -> Activity 이동
    public void categoryClick(View v) {
        Intent intent = new Intent(this, ItemActivity.class);
        TextView t = (TextView) v;
        Log.e("categoryClick", (String)t.getText() + " Click");
        intent.putExtra("category", (String)t.getText()); // intent에 클릭한 카테고리 이름 넣어주기
        startActivity(intent); // 액티비티 이동
    }
}