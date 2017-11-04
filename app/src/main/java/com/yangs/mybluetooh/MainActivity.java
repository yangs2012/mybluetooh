package com.yangs.mybluetooh;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.jianhuren)
    public TextView jianhuren;
    @BindView(R.id.status_jilu)
    public TextView status_jilu;
    @BindView(R.id.status_ciji)
    public TextView status_ciji;
    @BindView(R.id.status_off)
    public TextView status_off;
    @BindView(R.id.jilu_battery)
    public TextView jilu_battery;
    @BindView(R.id.jilu_daiji)
    public TextView jilu_daiji;
    @BindView(R.id.jilu_haianxiancanshu)
    public TextView jilu_haianxiancanshu;
    @BindView(R.id.jilu_haianxiancanshu_tv)
    public TextView jilu_haianxiancanshu_tv;
    @BindView(R.id.jilu_chongdian)
    public TextView jilu_chongdian;
    @BindView(R.id.jilu_zuidaxielv)
    public TextView jilu_zuidaxielv;
    @BindView(R.id.jilu_bt_wendu)
    public TextView jilu_bt_wendu;
    @BindView(R.id.jilu_wendu)
    public TextView jilu_wendu;
    @BindView(R.id.ciji_battery)
    public TextView ciji_battery;
    @BindView(R.id.ciji_daiji)
    public TextView ciji_daiji;
    @BindView(R.id.ciji_chongdian)
    public TextView ciji_chongdian;
    @BindView(R.id.fre_up)
    public TextView fre_up;
    @BindView(R.id.fre_down)
    public TextView fre_down;
    @BindView(R.id.fre_tv)
    public TextView fre_tv;
    @BindView(R.id.ciji_wendu)
    public TextView ciji_wendu;
    @BindView(R.id.ciji_wendu_tv)
    public TextView ciji_wendu_tv;
    @BindView(R.id.zkb_up)
    public TextView zkb_up;
    @BindView(R.id.zkb_down)
    public TextView zkb_down;
    @BindView(R.id.zkb_tv)
    public TextView zkb_tv;
    @BindView(R.id.zkjc)
    public TextView zkjc;
    @BindView(R.id.zkjc_tv)
    public TextView zkjc_tv;
    @BindView(R.id.fd_up)
    public TextView fd_up;
    @BindView(R.id.fd_down)
    public TextView fd_down;
    @BindView(R.id.fd_tv)
    public TextView fd_tv;
    @BindView(R.id.kz_cj)
    public TextView kz_cj;
    @BindView(R.id.kz_jl)
    public TextView kz_jl;
    @BindView(R.id.tv_turnoff)
    public TextView tv_turnoff;
    @BindView(R.id.et_send)
    public EditText et_send;
    @BindView(R.id.bt_send)
    public Button bt_send;
    @BindView(R.id.ciji_ll_turn)
    public LinearLayout ciji_ll_turn;
    @BindView(R.id.ciji_ll_turn_v)
    public View ciji_ll_turn_v;
    @BindView(R.id.jili_v1)
    public View jili_v1;
    @BindView(R.id.jili_v2)
    public View jili_v2;
    @BindView(R.id.jili_v3)
    public View jili_v3;
    @BindView(R.id.jilu_iv_battery)
    public ImageView jilu_iv_battery;
    @BindView(R.id.ciji_iv_battery)
    public ImageView ciji_iv_battery;
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final static int REQUEST_ENABLE_SMS = 2;
    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bluetoothLeScanner;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 20000;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattService bluetoothGattService;
    private BluetoothDevice device_ciji;
    private BluetoothDevice device_jilu;
    private BluetoothGattCharacteristic mcharacteristic;
    private final static UUID TEST_SERVICE_UUID = UUID.fromString("0000e0ff-3c17-d293-8e48-14fe2e4da212");      //123bit
    private final static UUID TEST_CHARACTERISTIC_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private int REQUEST_CODE_ACCESS_COARSE_LOCATION = 2;
    private final static String JILU_RECORD = "EEGM";       //记录器EEGM
    private final static String CIJIQI_RECORD = "EEGS";     //刺激器EEGS
    private static String CURRENT_RECORD;
    private static Boolean SCAN_FLAG = false;
    private static Boolean NON_FLAG = true;
    private List<Integer> batIconList;
    private StringBuilder mRxStringBuildler;
    private boolean isUnpackSending = false;
    private ThreadRx rxThread;
    private static Boolean ll_ciji_sw_status = true;
    private int wendu_flag;
    private Boolean ciji_ll_turn_flag = false;
    private Boolean jianhuren_flag = false;
    private String mode;
    private AlertDialog alertDialog;
    private String phone;
    private String text;
    private Boolean has_send_sms_flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        jilu_daiji.setOnClickListener(this);
        jianhuren.setOnClickListener(this);
        jianhuren.setLongClickable(true);
        jianhuren.setOnLongClickListener(this);
        jilu_haianxiancanshu.setOnClickListener(this);
        jilu_chongdian.setOnClickListener(this);
        jilu_zuidaxielv.setOnClickListener(this);
        jilu_bt_wendu.setOnClickListener(this);
        jilu_wendu.setOnClickListener(this);
        ciji_battery.setOnClickListener(this);
        ciji_daiji.setOnClickListener(this);
        ciji_chongdian.setOnClickListener(this);
        fre_up.setOnClickListener(this);
        fre_down.setOnClickListener(this);
        fre_tv.setOnClickListener(this);
        ciji_wendu.setOnClickListener(this);
        ciji_wendu_tv.setOnClickListener(this);
        zkb_up.setOnClickListener(this);
        zkb_down.setOnClickListener(this);
        zkb_tv.setOnClickListener(this);
        zkjc.setOnClickListener(this);
        zkjc_tv.setOnClickListener(this);
        fd_up.setOnClickListener(this);
        fd_down.setOnClickListener(this);
        fd_tv.setOnClickListener(this);
        kz_cj.setOnClickListener(this);
        tv_turnoff.setOnClickListener(this);
        kz_jl.setOnClickListener(this);
        et_send.setOnClickListener(this);
        bt_send.setOnClickListener(this);
        ciji_ll_turn.setOnClickListener(this);
        context = MainActivity.this;
        setHandler();
        ButterKnife.bind(this);
        toolbar.setTitle("无线闭环植入式迷走神经刺激器");
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("设备不支持蓝牙BLE,功能将无法使用!", 1);
        }
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_ENABLE_SMS);
        }
        mRxStringBuildler = new StringBuilder();
        phone = APPAplication.save.getString("phone", "");
        text = APPAplication.save.getString("text", "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                    showToast("未获得蓝牙权限!", 0);
                break;
            case REQUEST_ENABLE_SMS:
                initBroadcastReceiver();
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                    showToast("未授予短信发送权限,癫痫发作将无法通知!", 0);
                break;
        }
    }

    private void showToast(final String s, final int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, s, i).show();
            }
        });
    }

    private void setStatus() {
        status_jilu.setTextColor(ContextCompat.getColor(this, R.color.gray));
        status_ciji.setTextColor(ContextCompat.getColor(this, R.color.gray));
        status_off.setTextColor(ContextCompat.getColor(this, R.color.gray));
        kz_cj.setTextColor(ContextCompat.getColor(this, R.color.black));
        kz_jl.setTextColor(ContextCompat.getColor(this, R.color.black));
        tv_turnoff.setTextColor(ContextCompat.getColor(this, R.color.black));
        if (CURRENT_RECORD.equals(JILU_RECORD)) {
            status_jilu.setTextColor(ContextCompat.getColor(this, R.color.green));
            kz_jl.setTextColor(ContextCompat.getColor(this, R.color.green));
        } else if (CURRENT_RECORD.equals(CIJIQI_RECORD)) {
            status_ciji.setTextColor(ContextCompat.getColor(this, R.color.green));
            kz_cj.setTextColor(ContextCompat.getColor(this, R.color.green));
        } else {
            status_off.setTextColor(ContextCompat.getColor(this, R.color.green));
            tv_turnoff.setTextColor(ContextCompat.getColor(this, R.color.green));
        }
    }

    private void setHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        setStatus();
                        if (null != mcharacteristic) {
                            bluetoothGatt.setCharacteristicNotification(mcharacteristic, true);
                            BluetoothGattDescriptor descriptor = mcharacteristic.
                                    getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            bluetoothGatt.writeDescriptor(descriptor);
                        } else {
                            showToast("开启数据接收失败,将无法收到数据!", 0);
                        }
                        rxThread = new ThreadRx();
                        rxThread.start();
                        break;
                    case 2:
                        CURRENT_RECORD = "";
                        setStatus();
                        break;
                    case 5:
                        showToast("没有找到" + CURRENT_RECORD + "设备,请重试", 0);
                        break;
                    case 6:
                        String src = mRxStringBuildler.toString();
                        mRxStringBuildler.delete(0, mRxStringBuildler.length());
                        String[] save = src.split("\r\n");
                        for (String s : save) {
                            try {
                                explainData(s);
                            } catch (Exception e) {
                            }
                        }
                }
            }
        };
    }

    private Boolean checkBluetooh() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (CURRENT_RECORD.equals(device.getName())) {
                        if (CURRENT_RECORD.equals(CIJIQI_RECORD)) {
                            device_ciji = device;
                            bluetoothGatt = device_ciji.connectGatt(MainActivity.this, false, mgattCallback);
                        } else if (CURRENT_RECORD.equals(JILU_RECORD)) {
                            device_jilu = device;
                            bluetoothGatt = device_jilu.connectGatt(MainActivity.this, false, mgattCallback);
                        }
                        SCAN_FLAG = false;
                        ScanLeDevice(false);
                    }
                }
            });
        }
    };

    private void ScanLeDevice(boolean enable) {
        mHandler.removeCallbacks(mStopLeScan);
        if (enable) {
            if (SCAN_FLAG) {
                return;
            }
            mHandler.postDelayed(mStopLeScan, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        SCAN_FLAG = enable;
    }

    Runnable mStopLeScan = new Runnable() {
        @Override
        public void run() {
            if (SCAN_FLAG)
                mHandler.sendEmptyMessage(5);
            ScanLeDevice(false);
        }
    };
    // MTU size
    private static int MTU_SIZE_EXPECT = 300;
    private static int MTU_PAYLOAD_SIZE_LIMIT = 20;
    private BluetoothGattCallback mgattCallback = new BluetoothGattCallback() {
        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            MTU_PAYLOAD_SIZE_LIMIT = mtu - 3;
            bluetoothGatt.discoverServices();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                service.getUuid();
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    for (BluetoothGattDescriptor descriptor : descriptors) {
                        Log.d("info", descriptor.getUuid() + "");
                    }
                    if (characteristic.getUuid().equals(TEST_CHARACTERISTIC_UUID)) {
                        mcharacteristic = characteristic;
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTING) {
                mHandler.sendEmptyMessage(1);
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    bluetoothGatt.discoverServices();
                } else {
                    bluetoothGatt.requestMtu(MTU_SIZE_EXPECT);
                }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                mHandler.sendEmptyMessage(2);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            final byte[] data;
            if (TEST_CHARACTERISTIC_UUID.equals(characteristic.getUuid())) {
                data = characteristic.getValue();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onDataReceive(data);
                    }
                });
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (bluetoothGatt != null)
            bluetoothGatt.disconnect();
        mcharacteristic = null;
        super.onDestroy();
    }

    public void onDataReceive(byte[] data) {
        mRxStringBuildler.append(StringByteTrans.Byte2String(data));
    }

    private void initBroadcastReceiver() {
        IntentFilter sendIntentFilter = new IntentFilter();
        sendIntentFilter.addAction("SENT_SMS_ACTION");
        BroadcastReceiver sendBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("发送短信状态");
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        showToast("短信发送成功", 0);
                        break;
                    default:
                        showToast("短信发送失败", 0);
                        break;
                }
            }
        };
        registerReceiver(sendBroadcastReceiver, sendIntentFilter);
        IntentFilter deliverIntentFilter = new IntentFilter();
        deliverIntentFilter.addAction("DELIVERED_SMS_ACTION");
        BroadcastReceiver deliverBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showToast("收信人已经成功接收", 0);
            }
        };
        registerReceiver(deliverBroadcastReceiver, deliverIntentFilter);
    }

    private void explainData(String src) {
        if (src.contains("RES") || src.contains("BAT") || src.contains("FRQ") || src.contains("ZKB") ||
                src.contains("VOT") || src.contains("TMP") || src.contains("SLP") || src.contains("CST")) {
            String type = src.substring(0, 3);
            String new_msg = src.replace(type, "");
            switch (type) {
                case "RES":
                    zkjc_tv.setText(new_msg + " Ω");
                    break;
                case "BAT":
                    int number = 0;
                    try {
                        number = Integer.parseInt(new_msg);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    List<Integer> list = new ArrayList<>();
                    list.add(R.drawable.ic_battery_0);
                    list.add(R.drawable.ic_battery_1);
                    list.add(R.drawable.ic_battery_2);
                    list.add(R.drawable.ic_battery_3);
                    list.add(R.drawable.ic_battery_4);
                    int level = 0;
                    if (number == 0)
                        level = 0;
                    else if (number > 0 && number <= 25)
                        level = 1;
                    else if (number > 25 && number <= 50)
                        level = 2;
                    else if (number > 50 && number <= 75)
                        level = 3;
                    else if (number > 75 && number <= 100)
                        level = 4;
                    if (CURRENT_RECORD.equals(JILU_RECORD)) {
                        jilu_battery.setText(number + "%");
                        jilu_iv_battery.setBackgroundResource(list.get(level));
                    } else if (CURRENT_RECORD.equals(CIJIQI_RECORD)) {
                        ciji_battery.setText(number + "%");
                        ciji_iv_battery.setBackgroundResource(list.get(level));
                    }
                    break;
                case "FRQ":
                    if (ciji_ll_turn_flag)
                        fre_tv.setText(new_msg + " Hz");
                    break;
                case "ZKB":
                    if (ciji_ll_turn_flag)
                        zkb_tv.setText(new_msg + " %");
                    break;
                case "VOT":
                    if (ciji_ll_turn_flag)
                        fd_tv.setText(new_msg + " mA");
                    break;
                case "TMP":
                    if (wendu_flag == 1) {
                        jilu_wendu.setText(new_msg + " ℃");
                    } else if (wendu_flag == 2) {
                        ciji_wendu_tv.setText(new_msg + " ℃");
                    }
                    break;
                case "SLP":
                    jilu_zuidaxielv.setText(new_msg + " mV/mS");
                    break;
                case "CST":
                    jilu_haianxiancanshu_tv.setText(new_msg + " mV");
                    break;
            }
        } else {
            switch (src) {
                case "k":
                    jili_v1.setBackgroundResource(R.drawable.lay_5);
                    jili_v2.setBackgroundResource(R.drawable.lay_6);
                    jili_v3.setBackgroundResource(R.drawable.lay_6);
                    has_send_sms_flag = false;
                    break;
                case "y":
                    jili_v1.setBackgroundResource(R.drawable.lay_6);
                    jili_v2.setBackgroundResource(R.drawable.lay_5);
                    jili_v3.setBackgroundResource(R.drawable.lay_6);
                    has_send_sms_flag = false;
                    break;
                case "i":
                    jili_v1.setBackgroundResource(R.drawable.lay_6);
                    jili_v2.setBackgroundResource(R.drawable.lay_6);
                    jili_v3.setBackgroundResource(R.drawable.lay_5);
                    if (jianhuren_flag) {
                        if (has_send_sms_flag) {
                            return;
                        }
                        if (phone == null || phone.equals("") || text == null || text.equals("")) {
                            APPAplication.showToast("未设置短信发送内容\n长按 监护人提醒 来设置!", 0);
                        } else {
                            showToast("检测到癫痫发作,正在发送短信通知...", 0);
                            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
                            List<String> divideContents = smsManager.divideMessage(text);
                            for (String text : divideContents) {
                                smsManager.sendTextMessage(phone, null,
                                        text, null, null);
                            }
                            has_send_sms_flag = true;
                        }
                    }
                    break;
                case "DJON":
                    if ("ciji".equals(mode)) {
                        ciji_daiji.setTextColor(ContextCompat.getColor(this, R.color.green));
                    } else {
                        jilu_daiji.setTextColor(ContextCompat.getColor(this, R.color.green));
                    }
                    break;
                case "DJOF":
                    if ("ciji".equals(mode)) {
                        ciji_daiji.setTextColor(ContextCompat.getColor(this, R.color.white));
                    } else {
                        jilu_daiji.setTextColor(ContextCompat.getColor(this, R.color.white));
                    }
                    break;
                case "CDON":
                    if ("ciji".equals(mode)) {
                        ciji_chongdian.setTextColor(ContextCompat.getColor(this, R.color.green));
                    } else {
                        jilu_chongdian.setTextColor(ContextCompat.getColor(this, R.color.green));
                    }
                    break;
                case "CDOF":
                    if ("ciji".equals(mode)) {
                        ciji_chongdian.setTextColor(ContextCompat.getColor(this, R.color.white));
                    } else {
                        jilu_chongdian.setTextColor(ContextCompat.getColor(this, R.color.white));
                    }
                    break;
                case "OK":
                    if (ciji_ll_turn_flag) {
                        ciji_ll_turn_v.setBackgroundResource(R.drawable.lay_6);
                        ciji_ll_turn_flag = false;
                        fre_tv.setText("Hz");
                        zkb_tv.setText("%");
                        fd_tv.setText("mA");
                    } else {
                        ciji_ll_turn_v.setBackgroundResource(R.drawable.lay_5);
                        ciji_ll_turn_flag = true;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        byte[] data;
        switch (view.getId()) {
            case R.id.jianhuren:
                if (jianhuren_flag) {
                    jianhuren_flag = false;
                    jianhuren.setText("监护人提醒: 关");
                    jianhuren.setTextColor(ContextCompat.getColor(this, R.color.gray));
                } else {
                    jianhuren_flag = true;
                    jianhuren.setText("监护人提醒: 开");
                    jianhuren.setTextColor(ContextCompat.getColor(this, R.color.green));
                }
                break;
            case R.id.ciji_ll_turn:
                if (ciji_ll_turn_flag) {
                    data = StringByteTrans.Str2Bytes("k");
                } else {
                    data = StringByteTrans.Str2Bytes("i");
                }
                new ThreadUnpackSend(data).start();
                break;
            case R.id.zkjc:
                data = StringByteTrans.Str2Bytes("r");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.jilu_daiji:
                data = StringByteTrans.Str2Bytes("s");
                new ThreadUnpackSend(data).start();
                mode = "jilu";
                break;
            case R.id.jilu_chongdian:
                data = StringByteTrans.Str2Bytes("u");
                new ThreadUnpackSend(data).start();
                mode = "jilu";
                break;
            case R.id.ciji_daiji:
                data = StringByteTrans.Str2Bytes("s");
                new ThreadUnpackSend(data).start();
                mode = "ciji";
                break;
            case R.id.ciji_chongdian:
                data = StringByteTrans.Str2Bytes("u");
                new ThreadUnpackSend(data).start();
                mode = "ciji";
                break;
            case R.id.fre_up:
                data = StringByteTrans.Str2Bytes("a");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.fre_down:
                data = StringByteTrans.Str2Bytes("b");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.zkb_up:
                data = StringByteTrans.Str2Bytes("c");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.zkb_down:
                data = StringByteTrans.Str2Bytes("d");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.fd_up:
                data = StringByteTrans.Str2Bytes("h");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.fd_down:
                data = StringByteTrans.Str2Bytes("g");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.jilu_bt_wendu:
                wendu_flag = 1;
                data = StringByteTrans.Str2Bytes("t");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.ciji_wendu:
                wendu_flag = 2;
                data = StringByteTrans.Str2Bytes("t");
                new ThreadUnpackSend(data).start();
                break;
            case R.id.tv_turnoff:
                if (CURRENT_RECORD == null)
                    return;
                if (CURRENT_RECORD.equals(JILU_RECORD)) {
                    data = StringByteTrans.Str2Bytes("2m");
                    new ThreadUnpackSend(data).start();
                } else {
                    if (bluetoothGatt != null)
                        bluetoothGatt.disconnect();
                    if (mcharacteristic != null)
                        mcharacteristic = null;
                }
                break;
            case R.id.kz_cj:
                if (!checkBluetooh()) {
                    showToast("蓝牙未打开!", 0);
                    return;
                }
                CURRENT_RECORD = CIJIQI_RECORD;
                NON_FLAG = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                SCAN_FLAG = false;
                if (null != bluetoothGatt) {
                    bluetoothGatt.close();
                }
                if (null != mcharacteristic)
                    mcharacteristic = null;
                showToast("连接刺激器中...", 0);
                if (device_ciji != null) {
                    bluetoothGatt = device_ciji.connectGatt(MainActivity.this, false, mgattCallback);
                } else
                    ScanLeDevice(true);
                break;
            case R.id.kz_jl:
                if (!checkBluetooh()) {
                    showToast("蓝牙未打开!", 0);
                    return;
                }
                CURRENT_RECORD = JILU_RECORD;
                NON_FLAG = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                SCAN_FLAG = false;
                if (null != bluetoothGatt) {
                    bluetoothGatt.close();
                }
                if (null != mcharacteristic)
                    mcharacteristic = null;
                showToast("连接记录器中...", 0);
                if (device_jilu != null) {
                    bluetoothGatt = device_jilu.connectGatt(MainActivity.this, false, mgattCallback);
                } else
                    ScanLeDevice(true);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.jianhuren:
                if (alertDialog == null) {
                    View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.alertdialog_layout, null);
                    final EditText et_phone = view2.findViewById(R.id.dialog_et_phone);
                    final EditText et_text = view2.findViewById(R.id.dialog_et_text);
                    et_phone.setText(phone);
                    et_text.setText(text);
                    alertDialog = new AlertDialog.Builder(MainActivity.this).setView(view2)
                            .setTitle("设置短信").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    phone = et_phone.getText().toString().trim();
                                    text = et_text.getText().toString().trim();
                                    APPAplication.save.edit().putString("phone", phone)
                                            .putString("text", text).apply();
                                    APPAplication.showToast("保存成功", 0);
                                    dialogInterface.cancel();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create();
                }
                alertDialog.show();
                break;
        }
        return true;
    }

    public class ThreadRx extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // send the msg, here may send less times MSG, so we use the StringBuildler
                Message message = new Message();
                message.what = 6;
                mHandler.sendMessage(message);
            }
        }
    }

    public class ThreadUnpackSend extends Thread {
        byte[] sendData;

        ThreadUnpackSend(byte[] data) {
            sendData = data;
        }

        public void run() {
            // set the unpack sending flag
            isUnpackSending = true;
            // send data to the remote device
            if (null != mcharacteristic) {
                // unpack the send data, because of the MTU size is limit
                int length = sendData.length;
                int unpackCount = 0;
                byte[] realSendData;
                do {
                    if (length <= MTU_PAYLOAD_SIZE_LIMIT) {
                        realSendData = new byte[length];
                        System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, length);
                        length = 0;
                    } else {
                        realSendData = new byte[MTU_PAYLOAD_SIZE_LIMIT];
                        System.arraycopy(sendData, unpackCount * MTU_PAYLOAD_SIZE_LIMIT, realSendData, 0, MTU_PAYLOAD_SIZE_LIMIT);
                        // update length value
                        length = length - MTU_PAYLOAD_SIZE_LIMIT;
                    }
                    SendData(realSendData);
                    // unpack counter increase
                    unpackCount++;
                } while (length != 0);

                // set the unpack sending flag
                isUnpackSending = false;
            }
        }
    }

    private void SendData(byte[] realData) {
        // Set the send type(command)
        mcharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        // include the send data
        mcharacteristic.setValue(realData);
        // send the data
        bluetoothGatt.writeCharacteristic(mcharacteristic);

    }
}
