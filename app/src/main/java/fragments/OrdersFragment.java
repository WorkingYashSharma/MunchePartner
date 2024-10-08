package fragments;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safeshiprestaurantpartner.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

import models.RestaurantOrderItemModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class OrdersFragment extends Fragment {

    private View view;
    private FirebaseFirestore db;
    private String ruid;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mResOrderedItemRecyclerView;
    private String RES_LIST = "RestaurantList";
    private String RES_ORDERS = "RestaurantOrders";

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        init();
        fetchLiveOrders();

        return view;
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        ruid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mResOrderedItemRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        mResOrderedItemRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void fetchLiveOrders() {
        Query query = db.collection(RES_LIST).document(ruid).collection(RES_ORDERS);

        FirestoreRecyclerOptions<RestaurantOrderItemModel> orderedItemModel = new FirestoreRecyclerOptions.Builder<RestaurantOrderItemModel>()
                .setQuery(query, RestaurantOrderItemModel.class)
                .build();

        FirestoreRecyclerAdapter<RestaurantOrderItemModel, RestaurantItemHolder> orderAdapter = new FirestoreRecyclerAdapter<RestaurantOrderItemModel, RestaurantItemHolder>(orderedItemModel) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull RestaurantItemHolder holder, int position, @NonNull RestaurantOrderItemModel model) {

                ArrayList<String> orderedItems = model.getOrdered_items();
                for (int i = 0; i < orderedItems.size(); i++) {
                    TextView tv = new TextView(getContext());
                    final Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans);
                    tv.setText(orderedItems.get(i));
                    tv.setTypeface(typeface);
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    tv.setTextSize(16);
                    holder.orderItemLayout.addView(tv);
                }

                if (model.getPayment_method().equals("COD")) {
                    holder.mCodMethod.setVisibility(View.VISIBLE);
                } else if (model.getPayment_method().equals("PAID")) {
                    holder.mPaidMethod.setVisibility(View.VISIBLE);
                }

                holder.mOrderID.setText("ORDER ID: " + model.getOrder_id());
                holder.mCustomerName.setText(model.getCustomer_name() + "'s Order");
                holder.mDeliveryAddress.setText("Address: " + model.getDelivery_address());
                holder.mOrderTime.setText(model.getShort_time());
                holder.mTotalAmount.setText("TOTAL AMOUNT: " + model.getTotal_amount());

                holder.countDownTextview.setTime(0, 0, 0, 30);
                holder.countDownTextview.startTimer();
                final Typeface typeface2 = ResourcesCompat.getFont(requireContext(), R.font.open_sans_semibold);
                holder.countDownTextview.setTypeFace(typeface2);
                holder.countDownTextview.setOnTick(new CountDownInterface() {
                    @Override
                    public void onTick(long time) {

                        if (time > 60000) {
                            holder.timeFormatText.setText("minutes left");
                        } else {
                            holder.timeFormatText.setText("seconds left");
                        }

                    }

                    @Override
                    public void onFinish() {
                    }
                });

                holder.mOrderDeclineBtn.setOnClickListener(view -> {
                    Toast.makeText(getContext(), "Order is Declined", Toast.LENGTH_SHORT).show();
                });

                holder.mAcceptOrderBtn.setOnClickListener(view -> {
                    Toast.makeText(getContext(), "Order is Accepted", Toast.LENGTH_SHORT).show();


                });

                if (model.getExtra_instructions().equals("none")) {
                    holder.extraInstructionsText.setText("\u2022 No additional instruction");
                } else {
                    holder.extraInstructionsText.setText("\u2022 " + model.getExtra_instructions());
                }
            }

            @NonNull
            @Override
            public RestaurantItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ordered_items_layout, parent, false);

                return new RestaurantItemHolder(view);

            }
        };
        orderAdapter.startListening();
        orderAdapter.notifyDataSetChanged();
        mResOrderedItemRecyclerView.setAdapter(orderAdapter);

    }

    public static class RestaurantItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderId)
        TextView mOrderID;
        @BindView(R.id.orderTime)
        TextView mOrderTime;
        @BindView(R.id.deliveryAddress)
        TextView mDeliveryAddress;
        @BindView(R.id.codMethod)
        TextView mCodMethod;
        @BindView(R.id.paidMethod)
        TextView mPaidMethod;
        @BindView(R.id.totAmount)
        TextView mTotalAmount;
        @BindView(R.id.declineBtn)
        Button mOrderDeclineBtn;
        @BindView(R.id.acceptBtn)
        Button mAcceptOrderBtn;
        @BindView(R.id.orderedItemContainer)
        LinearLayout orderItemLayout;
        @BindView(R.id.easyCountDownTextview)
        EasyCountDownTextview countDownTextview;
        @BindView(R.id.timeFormatText)
        TextView timeFormatText;
        @BindView(R.id.extraInstructionsText)
        TextView extraInstructionsText;
        @BindView(R.id.customerNameText)
        TextView mCustomerName;

        public RestaurantItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}