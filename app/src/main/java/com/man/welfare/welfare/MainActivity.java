package com.man.welfare.welfare;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.man.welfare.welfare.db.MySQLiteHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private List<fuliBeen.ResultsBean> data = new ArrayList<>();
    private List<fuliBeen.ResultsBean> datas = new ArrayList<>();
    private RecyclerviewAdapter adapter;
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //根据状态栏颜色来决定状态栏文字用黑色还是白色
        StatusBarUtil.setStatusBarMode(this, true, R.color.grey);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        AppinfoiItemDecoration decor = new AppinfoiItemDecoration();
        recyclerView.addItemDecoration(decor);

        GetData();
        GetDatas();

        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                i = 1;
                GetData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                GetDatas();
                adapter.add(datas);
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    private void GetData() {
        String url = "http://gank.io/api/data/福利/10/1";
        HashMap<String, String> params = new HashMap<>();
        NetRequest.getFormRequest(url, params, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                fuliBeen fuli = gson.fromJson(result, fuliBeen.class);
                data = fuli.getResults();

                if (data.size() != 0) {
                    adapter = new RecyclerviewAdapter(getApplicationContext(), data);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });
    }

    private void GetDatas() {
        i = i + 1;
        String url = "http://gank.io/api/data/福利/10/" + i;
        HashMap<String, String> params = new HashMap<>();
        NetRequest.getFormRequest(url, params, new NetRequest.DataCallBack() {
            @Override
            public void requestSuccess(String result) throws Exception {
                Gson gson = new Gson();
                fuliBeen fuli = gson.fromJson(result, fuliBeen.class);
                datas = fuli.getResults();
            }

            @Override
            public void requestFailure(Request request, IOException e) {

            }
        });
    }
}
