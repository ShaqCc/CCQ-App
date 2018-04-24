//package com.ccq.app.weidget;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.ccq.app.R;
//import com.ccq.app.ui.publish.DoubleListLeftAdapter;
//
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * 带有两个并列的listview的  dialog
// *
// * @author wtw
// *         2016-1-14下午2:05:49
// */
//public class DoubleListDialog extends Dialog {
//
//    @BindView(R.id.lv_list_left)
//    ListView lvListLeft;
//    @BindView(R.id.lv_list_right)
//    ListView lvListRight;
//
//    private Context context;
//    public static View partOne;//listview的item
//
//    private ArrayList<String> listData1;//list数据一
//    private ArrayList<ArrayList<String>> AlllistData2;//list数据二
//    private ArrayList<String> listData2 = new ArrayList<String>();//list数据二
//    private String data1;//选择的数据一
//    private String data2;//选择的数据二
//
//    private CallBack callBack;//点击后的数据返回
//
//    public DoubleListDialog(Context context, ArrayList<String> listData1, ArrayList<ArrayList<String>> AlllistData2, CallBack callBack) {
//        super(context );
//        this.context = context;
//        this.listData1 = listData1;
//        this.AlllistData2 = AlllistData2;
//        this.callBack = callBack;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//    }
//
//    private void initView() {
//        data1 = listData1.get(0);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.listview_double_layout, null);
//        setContentView(view);
//        ButterKnife.bind(this,view);
//
//
//        //设置dialog的大小
//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//        lp.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕宽度的 90%
//        dialogWindow.setAttributes(lp);
//
//
//        DoubleListLeftAdapter adapter1 = new DoubleListLeftAdapter(context, listData1);
//        lvListLeft.setAdapter(adapter1);
//        listData2.addAll(AlllistData2.get(0));
//        final DoubleListLeftAdapter adapter2 = new DoubleListLeftAdapter(context, listData2);
//        lvListRight.setAdapter(adapter2);
//        lvListLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                if (partOne != null) {
//                    partOne.setBackgroundColor(context.getResources().getColor(R.color.white));
//                }
//                partOne = arg1;
//                partOne.setBackgroundColor(context.getResources().getColor(R.color.lightgrey));
//                data1 = listData1.get(arg2);
//                listData2.clear();
//                listData2.addAll(AlllistData2.get(arg2));
//                adapter2.notifyDataSetChanged();
//            }
//        });
//        lvListRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                data2 = listData2.get(arg2);
//                callBack.clickResult(data1, (data2.equals("全部") ? "" : data2));
//            }
//        });
//    }
//
//    public interface CallBack {
//        public void clickResult(String data1, String data2);
//    }
//
//}
