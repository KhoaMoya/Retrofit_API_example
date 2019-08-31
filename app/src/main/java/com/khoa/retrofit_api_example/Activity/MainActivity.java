package com.khoa.retrofit_api_example.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonObject;
import com.khoa.retrofit_api_example.Control.Communicator;
import com.khoa.retrofit_api_example.Control.MyDataBase;
import com.khoa.retrofit_api_example.Event.EventGetLastTimePingSuccess;
import com.khoa.retrofit_api_example.Model.Ping;
import com.khoa.retrofit_api_example.Model.SimplePing;
import com.khoa.retrofit_api_example.Utility.DemoPing;
import com.khoa.retrofit_api_example.Control.PreferencesManager;
import com.khoa.retrofit_api_example.Event.EventFail;
import com.khoa.retrofit_api_example.Event.EventLoadUrlAvatarSuccess;
import com.khoa.retrofit_api_example.Event.EventSavePingSuccess;
import com.khoa.retrofit_api_example.Model.AvatarImage;
import com.khoa.retrofit_api_example.Model.User;
import com.khoa.retrofit_api_example.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtName, txtPing, txtMin, txtMax, txtAverage;
    private Button btnLogout, btnGetPing, btnStop;
    private PreferencesManager preferencesManager;
    private User user;
    private ImageView imgAvatar;
    private EditText edtIP;
    private Runnable runnable;
    private Handler handler;
    private Communicator communicator;
    private float ping;
    private LineChart lineChart;
    private ArrayList<Entry> arrayListValue;
    private LineDataSet lineDataSet;
    private float min = 1000f, max = 0f, average = 0f, total = 0f;
    private MyDataBase myDataBase;
    private String address;
    private ArrayList<SimplePing> arrayPingTemp;
    private final int n = 3;
    private String lastTimeServer;
    private ArrayList<SimplePing> arrMissPing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        handler = new Handler();

        myDataBase = new MyDataBase(this);
        txtPing = findViewById(R.id.txt_ping);
        txtMin = findViewById(R.id.txt_min);
        txtMax = findViewById(R.id.txt_max);
        txtAverage = findViewById(R.id.txt_average);
        txtName = findViewById(R.id.txtName);
        btnLogout = findViewById(R.id.btn_log_out);
        imgAvatar = findViewById(R.id.img_avatar);
        btnGetPing = findViewById(R.id.btn_get_ping);
        btnStop = findViewById(R.id.btn_stop);
        edtIP = findViewById(R.id.edt_ip);
        lineChart = findViewById(R.id.line_chart);

        arrMissPing = new ArrayList<>();
        arrayPingTemp = new ArrayList<>();
        edtIP.append("google.com.vn");
        btnLogout.setOnClickListener(this);
        preferencesManager = new PreferencesManager(this);
        communicator = new Communicator();
        user = preferencesManager.getUser();

        btnGetPing.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);

//        showInfor();
        initLineChart();
//        Log.e("Loi", myDataBase.getArrayMissPing());
    }

    public void initLineChart() {
        arrayListValue = new ArrayList<>();
        arrayListValue.add(new Entry(0, 0));
        lineDataSet = new LineDataSet(arrayListValue, "pings");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(true);
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return lineChart.getAxisLeft().getAxisMinimum();
            }
        });
        lineChart.setViewPortOffsets(0, 0, 0, 0);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        lineChart.setDrawGridBackground(false);
        lineChart.setMaxHighlightDistance(300);

        XAxis x = lineChart.getXAxis();
        x.setEnabled(false);


        YAxis y = lineChart.getAxisLeft();
//        y.setTypeface(tfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.TRANSPARENT);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);

//        lineChart.animateX(1000);
//        lineChart.setVisibleXRangeMinimum(10);
//        lineChart.setVisibleXRangeMaximum(30);
//        setData(50, 100);
        LineData data = new LineData(lineDataSet);
//            data.setValueTypeface(tfLight);
        data.setValueTextSize(9f);
        data.setDrawValues(false);


        // set data
        lineChart.setScaleYEnabled(false);
        lineChart.setData(data);
        lineChart.invalidate();
        communicator.getLastTimePing();

    }

    private void updateLineChart() {
        lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
        lineDataSet.setValues(arrayListValue);
        lineChart.getData().notifyDataChanged();
        lineChart.notifyDataSetChanged();
        for (IDataSet set : lineChart.getData().getDataSets()) {
            set.setDrawValues(true);
            set.setValueTextColor(Color.parseColor("#BAFFFFFF"));
        }
        lineChart.invalidate();
    }

    public void updateTextView() {
        max = ping > max ? ping : max;
        min = ping < min ? ping : min;
        total += ping;
        average = total / (float) arrayListValue.size();
        txtPing.setText(ping + "");
        txtMin.setText(min + " ms");
        txtMax.setText(max + " ms");
        txtAverage.setText(String.format("%.1f", average) + " ms");
    }

    public void stopPing() {
        handler.removeCallbacks(runnable);
    }

    public void showInfor() {
        txtName.setText(user.getUsername());
        if (user.getId_avatar_image() != -1) {
            communicator.getAvatarImage(user.getToken(), user.getId_avatar_image());
        } else {
            imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.icon_google_48));
        }
    }

    public void loadAvatarImage(String url) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.profile_icon)
                .into(imgAvatar);
    }

    public void addSimplePing(SimplePing simplePing) {
        Log.e("Loi", "addSimlpePing");
        if (arrayPingTemp.size() < n) {
            arrayPingTemp.add(simplePing);
        } else {
            arrayPingTemp.clear();
            arrayPingTemp.add(simplePing);
        }
        if (arrayPingTemp.size() == n) {
            Log.e("Loi", "getLastTimePing");
            savePingToServer(arrayPingTemp);
        } else {
            handler.postDelayed(runnable,1000);
        }
    }

    public void savePingToServer(final ArrayList<SimplePing> arrPing) {

        new Runnable() {
            @Override
            public void run() {
                JSONObject jsonMain = new JSONObject();
                try {
                    for (int i = 0; i < arrPing.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("value_ping", arrPing.get(i).getValue_ping());
                        jsonObject.put("address", arrPing.get(i).getAddress());
                        jsonObject.put("time", arrPing.get(i).getTime());
                        jsonMain.put("ping" + i, jsonObject);
                    }
                    Log.e("Loi",jsonMain.toString());
                    Log.e("Loi", arrPing.size()+"");
//                    Log.e("Loi", user.getToken());
                    communicator.savePing(user.getToken(), arrPing.size(), jsonMain.toString());
                    Log.e("Loi", "Đã gửi");
                } catch (JSONException e) {
                    Log.e("Loi", e.getMessage());
                }
            }
        }.run();
//        JSONObject jsonMain = new JSONObject();
//        try {
//            for (int i = 0; i < 10; i++) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("value_ping", arrPing.get(i).getValue_ping());
//                jsonObject.put("address", arrPing.get(i).getAddress());
//                jsonObject.put("time", arrPing.get(i).getAddress());
//                jsonMain.put("ping"+ i, jsonObject);
//            }
//            jsonMain.put("token", user.getToken());
//        }catch (JSONException e){
//            Log.e("Loi", e.getMessage());
//        }
//        communicator.savePing(user.getToken(), arrPing);
//        Log.e("Loi", jsonMain.toString());
//        Toast.makeText(this, "Đã gửi", Toast.LENGTH_SHORT).show();


//        communicator.savePing(
//                user.getToken(),
//                ping,
//                address);
//        myDataBase.savePing(
//                ping,
//                address,
//                user.getId(),
//                true,
//                String.valueOf(System.currentTimeMillis()));
    }

    public void showAlertDialogMessage(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Subscribe
    public void onEventGetLastTimePingSuccess(EventGetLastTimePingSuccess eventGetLastTimePingSuccess) {
        lastTimeServer = eventGetLastTimePingSuccess.getTime();
        if (lastTimeServer.equals("null")) return;
        Log.e("Loi", "getLastTimePingSuccess: " + lastTimeServer );
        if(lastTimeServer.length()>0) {
            arrMissPing = myDataBase.getArrayMissPing(lastTimeServer);
            if (arrMissPing == null) {
//                savePingToServer(arrayPingTemp);
                showAlertDialogMessage("Error get array miss ping");
            } else if (arrMissPing.size() == 0) {
//                savePingToServer(arrayPingTemp);
                Toast.makeText(this, "Dữ liệu là mới nhất", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Có " + arrMissPing.size() + " đối tượng chưa được cập nhập", Toast.LENGTH_SHORT).show();
                savePingToServer(arrMissPing);
            }
        }
    }

    @Subscribe
    public void onEventSavePingSuccess(EventSavePingSuccess eventSavePingSuccess) {
        int code = eventSavePingSuccess.getCode();
        if (code!=1) {
            stopPing();
            showAlertDialogMessage("Cập nhập thất bại");
        } else {
            Toast.makeText(this, "Cập nhập thành công", Toast.LENGTH_SHORT).show();
            arrayListValue.add(new Entry(arrayListValue.size(), ping));
            updateLineChart();
            updateTextView();
            handler.postDelayed(runnable, 1000);
        }
    }

    @Subscribe
    public void onEventFail(EventFail eventFail) {
        showAlertDialogMessage(eventFail.getError());
        stopPing();
    }

    @Subscribe
    public void onEventLoadUrlAvatarSuccess(EventLoadUrlAvatarSuccess eventLoadUrlAvatarSuccess) {
        AvatarImage avatarImage = eventLoadUrlAvatarSuccess.getAvatarImage();
        if (avatarImage.getMessage().isEmpty()) {
            loadAvatarImage(avatarImage.getUrl());
        } else {
            showAlertDialogMessage(avatarImage.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_log_out:
                preferencesManager.setIsLoggedIn(false);
                preferencesManager.resetUser();
                final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_get_ping:
                address = edtIP.getText().toString();

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        ping = DemoPing.getPing(edtIP.getText().toString());
                        arrayListValue.add(new Entry(arrayListValue.size(), ping));
                        updateLineChart();
                        updateTextView();
                        String time = String.valueOf(System.currentTimeMillis());
                        myDataBase.savePing(ping, address, user.getId(), false, time);
                        addSimplePing(new SimplePing(ping, address, time));

//                        handler.postDelayed(runnable, 1000);
//                        savePingToServer();
                    }
                };
                handler.post(runnable);

                break;
            case R.id.btn_stop:
                stopPing();
                break;
            case R.id.img_avatar:
                Intent intent1 = new Intent(MainActivity.this, InforActivity.class);
                startActivity(intent1);
                break;

            default:
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
}
