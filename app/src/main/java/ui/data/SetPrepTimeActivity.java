package ui.data;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safeshiprestaurantpartner.MainActivity;
import com.example.safeshiprestaurantpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetPrepTimeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mPrepTimeText;
    private FirebaseFirestore db;
    private String resUid;
    private final String RES_LIST = "RestaurantList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_prep_time);

        init();
        fetchPrepTime();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        db = FirebaseFirestore.getInstance();
        resUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mPrepTimeText = findViewById(R.id.showPrepTime);
        ImageView mGoBackBtn = findViewById(R.id.goBackBtn);
        TextView mToolBarText = findViewById(R.id.setTextChoice);
        mToolBarText.setText("Set Preparation Time");
        ImageView mAddTimeBtn = findViewById(R.id.addTimeBtn);
        ImageView mMinusTimeBtn = findViewById(R.id.minusTimeBtn);
        Button mSaveTimeBtn = findViewById(R.id.savePrepTimeBtn);
        mGoBackBtn.setOnClickListener(this);
        mAddTimeBtn.setOnClickListener(this);
        mMinusTimeBtn.setOnClickListener(this);
        mSaveTimeBtn.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.goBackBtn:
                onBackPressed();
                break;

            case R.id.addTimeBtn:
                incrementTime();
                break;

            case R.id.minusTimeBtn:
                decrementTime();
                break;

            case R.id.savePrepTimeBtn:
                uploadPrepTime();
                break;
        }
    }

    private void fetchPrepTime() {
        db.collection(RES_LIST).document(resUid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot docRef = task.getResult();
                String prepTime = String.valueOf(Objects.requireNonNull(docRef).get("restaurant_prep_time"));
                mPrepTimeText.setText(prepTime);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void incrementTime() {
        int time = Integer.parseInt(mPrepTimeText.getText().toString().replace(" Mins" , ""));
        if (time >= 90){
            Toast.makeText(this, "Cannot go over 90 minutes", Toast.LENGTH_SHORT).show();
        }else {
            time += 5;
            mPrepTimeText.setText(time + " Mins");
        }
    }

    @SuppressLint("SetTextI18n")
    private void decrementTime() {
        int time1 = Integer.parseInt(mPrepTimeText.getText().toString().replace(" Mins" , ""));
        if(time1 <= 5){
            Toast.makeText(this, "Cannot go under 5 minutes", Toast.LENGTH_SHORT).show();
        }else {
            time1 -= 5;
            mPrepTimeText.setText(time1 + " Mins");
        }
    }

    private void uploadPrepTime() {
        Map<String, Object> updatePrepTimeMap = new HashMap<>();
        updatePrepTimeMap.put("restaurant_prep_time", mPrepTimeText.getText().toString());

        db.collection(RES_LIST).document(resUid).update(updatePrepTimeMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "Updated Prep Time", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}