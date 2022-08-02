package com.example.textrecognitionex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private String refrigeratorName; // 냉장고 이름
    private String category; // 카테고리 이름

    private ItemViewModel viewModel;
    private RecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Log.e("ItemActivity", "ItemActivity 시작");

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class); // 뷰모델 생성

        itemRecyclerView = (RecyclerView)findViewById(R.id.itemRecyclerView);
        itemAdapter = new ItemAdapter(viewModel); // 어댑터 생성

        itemRecyclerView.setAdapter(itemAdapter); // 리사이클러뷰에 MyRecyclerAdapter2 객체 지정
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        itemRecyclerView.setHasFixedSize(true);

        registerForContextMenu(itemRecyclerView);

        // item에 변화가 생긴 경우 어댑터에게 알려서 갱신하도록
        final Observer<ArrayList<String>> myObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                itemAdapter.notifyDataSetChanged();
            }
        };
        viewModel.itemsLiveData.observe(this, myObserver);

        // MainActivity에서 넘어온 Intent, 카테고리 이름이 담겨있음
        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        refrigeratorName = "냉장고1";
        setTitle(category);
        getFirebaseDatabase(); // 카테고리 안에 있는 아이템들을 가져와서 보여줌
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ((Activity)v.getContext()).getMenuInflater().inflate(R.menu.item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }


    public void addItemBtnClick(View v) {
        // item 추가하는 액티비티로 이동
        Intent intent = new Intent(this, com.example.textrecognitionex.AddFoodActivity.class);
        intent.putExtra("refrigeratorName", refrigeratorName); // 냉장고 이름 전달
        intent.putExtra("category", category); // 카테고리 이름 전달
        intent.putExtra("itemViewModel", (Parcelable) viewModel); // item 뷰모델 전달
        startActivity(intent);
    }

    public void getFirebaseDatabase() {
        Log.e("getFirebaseDatabase2", "함수 실행");
        FirebaseDatabase.getInstance().getReference().child("냉장고").child(refrigeratorName).child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("getFirebaseDatabase", "onDataChange");
                for(DataSnapshot data : snapshot.getChildren()) {
                    //FirebasePost2 get = data.getValue(FirebasePost2.class);
                    String key = data.getKey();
                    Log.e("getFirebaseDatabase", "key = " + key);
                    viewModel.addItem(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}