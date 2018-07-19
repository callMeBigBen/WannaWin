package com.citiexchangeplatform.pointsleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.citiexchangeplatform.pointsleague.adapter.CardPointsAdapter;
import com.citiexchangeplatform.pointsleague.models.CardPointsModel;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.leochuan.ScaleLayoutManager;
import com.study.xuan.xvolleyutil.base.XVolley;
import com.study.xuan.xvolleyutil.callback.CallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PointsFragment extends Fragment {

    boolean isLogin;

    View view;
    LinearLayout accountInfoLayout;
    View content;
    ProgressDialog dialog;

    RecyclerView recyclerView;
    CardPointsAdapter cardPointsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_points, null);
        accountInfoLayout = (LinearLayout) view.findViewById(R.id.linearlayout_account_info_points);

        initImageSlider();
        //loadAccountInfo();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * 初始化推广轮播内容
     */
    private void initImageSlider() {

        SliderLayout sliderLayout = (SliderLayout) view.findViewById(R.id.slider_main);
        PagerIndicator indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator_main);

        //准备好要显示的数据
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://www.never-give-it-up.top/wp-content/uploads/2018/07/pic1.png");
        imageUrls.add("http://www.never-give-it-up.top/wp-content/uploads/2018/07/pic2.png");
        imageUrls.add("http://www.never-give-it-up.top/wp-content/uploads/2018/07/pic3.png");


        for (int i = 0; i < imageUrls.size(); i++) {
            DefaultSliderView sv = new DefaultSliderView(getActivity());
            sv.image(imageUrls.get(i));
            sv.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(getActivity(), "~", Toast.LENGTH_SHORT).show();
                }
            });
            sliderLayout.addSlider(sv);
        }

        //对SliderLayout自定义配置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setDuration(3000);
        sliderLayout.setCustomIndicator(indicator);
    }


    private void loadAccountInfo() {
        accountInfoLayout.removeAllViewsInLayout();
        isLogin = LogStateInfo.getInstance(getContext()).isLogin();

        if (isLogin) {
            content = LayoutInflater.from(getContext()).inflate(R.layout.content_cards_points, null);
            accountInfoLayout.addView(content);
            Button buttonAllCard = (Button) content.findViewById(R.id.button_all_card_main);
            buttonAllCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToAllCard = new Intent(getActivity(), AllCardActivity.class);
                    startActivity(intentToAllCard);
                }
            });
            Button buttonExchangePoint = (Button) content.findViewById(R.id.button_exchange_points_main);
            buttonExchangePoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToExchangePoint = new Intent(getActivity(), PayingActivity.class);
                    startActivity(intentToExchangePoint);
                }
            });

            cardPointsAdapter = new CardPointsAdapter(getContext());
            recyclerView = (RecyclerView) content.findViewById(R.id.recyclerView_cards_points);
            recyclerView.setLayoutManager(
                    new ScaleLayoutManager
                    .Builder(getContext(),2)
                    .setOrientation(OrientationHelper. HORIZONTAL)
                    .build());
            recyclerView.setAdapter(cardPointsAdapter);
            recyclerView.setItemAnimator( new DefaultItemAnimator());

        } else {

            content = LayoutInflater.from(getContext()).inflate(R.layout.content_login_button_points, null);
            accountInfoLayout.addView(content);

            final EditText editTextAccount = (EditText) content.findViewById(R.id.editText_account_card_points);
            final EditText editTextPassword = (EditText) content.findViewById(R.id.editText_password_card_points);

            Button buttonLogin = (Button) content.findViewById(R.id.button_login_card_points);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextAccount.getText().length() == 0) {
                        Toast.makeText(getContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                    } else if (editTextPassword.getText().length() == 0) {
                        Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {

                        String strAccount = editTextAccount.getText().toString();
                        String strPassword = editTextPassword.getText().toString();

                        tryLogin(strAccount, strPassword);
                    }
                }
            });

            TextView buttonRegister = (TextView) content.findViewById(R.id.textView_register_card_points);
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToRegister = new Intent(getContext(), RegisterActivity.class);
                    startActivity(intentToRegister);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("12345");
        loadAccountInfo();

        if (isLogin) {
            showCardInfo();
            showPoints(content);
        }

    }


    private void showCardInfo() {
        XVolley.getInstance()
                .doPost()
                .url("http://193.112.44.141:80/citi/mscard/infos")
                .addParam("userID", LogStateInfo.getInstance(getContext()).getUserID())
                .addParam("n", "5")
                .build()
                .execute(getContext(), new CallBack<String>() {
                    @Override
                    public void onSuccess(Context context, String response) {
                        System.out.println(response);
                        boolean logSuccess = false;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            ArrayList<CardPointsModel> items = new ArrayList<CardPointsModel>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String merchantName = jsonObject.getString("merchantName");
                                String merchantID = jsonObject.getString("merchantID");
                                int points = jsonObject.getInt("points");
                                double proportion = jsonObject.getDouble("proportion");
                                String logoURL = jsonObject.getString("logoURL");
                                CardPointsModel item = new CardPointsModel(merchantName, merchantID, points, proportion, logoURL);
                                items.add(item);
                            }

                            switch (items.size()){
                                case 1:
                                    cardPointsAdapter.addData(items.get(0));
                                    break;
                                case 2:
                                    cardPointsAdapter.addData(items.get(1));
                                    cardPointsAdapter.addData(items.get(0));
                                    break;
                                case 3:
                                    cardPointsAdapter.addData(items.get(1));
                                    cardPointsAdapter.addData(items.get(0));
                                    cardPointsAdapter.addData(items.get(2));
                                    break;
                                case 4:
                                    cardPointsAdapter.addData(items.get(3));
                                    cardPointsAdapter.addData(items.get(1));
                                    cardPointsAdapter.addData(items.get(0));
                                    cardPointsAdapter.addData(items.get(2));
                                    break;
                                case 5:
                                    cardPointsAdapter.addData(items.get(3));
                                    cardPointsAdapter.addData(items.get(1));
                                    cardPointsAdapter.addData(items.get(0));
                                    cardPointsAdapter.addData(items.get(2));
                                    cardPointsAdapter.addData(items.get(4));
                                    break;
                                default:
                            }

                            recyclerView.scrollToPosition(cardPointsAdapter.getItemCount()/2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "服务器连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBefore() {
                        super.onBefore();
                        dialog = ProgressDialog.show(getContext(), "", "正在获取卡片信息...");
                    }

                });
    }

    private void showPoints(final View view) {
        XVolley.getInstance()
                .doPost()
                .url("http://193.112.44.141:80/citi/points/generalPoints")
                .addParam("userID", LogStateInfo.getInstance(getContext()).getUserID())
                .build()
                .execute(getContext(), new CallBack<String>() {
                    @Override
                    public void onSuccess(Context context, String response) {
                        System.out.println(response);
                        boolean logSuccess = false;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            double generalPoints = jsonObject.getDouble("generalPoints");
                            TextView textView = (TextView)view.findViewById(R.id.textView_generalPoints_main);
                            textView.setText(String.format("%.1f",generalPoints));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "服务器连接失败", Toast.LENGTH_LONG).show();
                    }
                });

        XVolley.getInstance()
                .doPost()
                .url("http://193.112.44.141:80/citi/points/availablePoints")
                .addParam("userID", LogStateInfo.getInstance(getContext()).getUserID())
                .build()
                .execute(getContext(), new CallBack<String>() {
                    @Override
                    public void onSuccess(Context context, String response) {
                        System.out.println(response);
                        boolean logSuccess = false;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            double availablePoints = jsonObject.getDouble("availablePoints");
                            TextView textView = (TextView)view.findViewById(R.id.textView_availablePoints_main);
                            textView.setText(String.format("%.1f",availablePoints));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        Toast.makeText(getContext(), "服务器连接失败", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void tryLogin(final String account, String password) {
        XVolley.getInstance()
                .doPost()
                .url("http://193.112.44.141:80/citi/account/login")
                .addParam("phoneNum", account)
                .addParam("password", password)
                .build()
                .execute(getContext(), new CallBack<String>() {
                    @Override
                    public void onSuccess(Context context, String response) {
                        System.out.println(response);
                        boolean logSuccess = false;
                        try {
                            if (response.length() > 2) {
                                JSONObject jsonObject = new JSONObject(response);

                                String accountPhoneNum = jsonObject.getString("phoneNum");
                                String userID = jsonObject.getString("userID");

                                LogStateInfo.getInstance(getContext())
                                        .setAccount(account)
                                        .setUserID(userID);

                                logSuccess = true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                        if (logSuccess) {
                            LogStateInfo.getInstance(getContext()).login();
                            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            onResume();
                        } else {
                            Toast.makeText(getContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        super.onError(error);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "服务器连接失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBefore() {
                        super.onBefore();
                        dialog = ProgressDialog.show(getContext(), "", "登录中...");
                    }
                });
    }
}
