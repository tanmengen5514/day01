package com.tanmengen.en.tanmengenqi.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tanmengen.en.tanmengenqi.ApiService;
import com.tanmengen.en.tanmengenqi.R;
import com.tanmengen.en.tanmengenqi.adapters.MyAdapter;
import com.tanmengen.en.tanmengenqi.beans.BannerBean;
import com.tanmengen.en.tanmengenqi.beans.FuliBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    private View view;
    private RecyclerView mRel;
    private ArrayList<String> banners;
    private int page = 0;
    private ArrayList<FuliBean.DataBean.DatasBean> list;
    private MyAdapter myAdapter;
    private SmartRefreshLayout mSmart;
    private int mposition;

    public Fragment1() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //下拉刷新
                    list.clear();
                    banners.clear();
                    initBanner();
                    page = 0;
                    initData(page);
                    mSmart.finishRefresh();
                    myAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    //上拉加载
                    page++;
                    initData(page);
                    mSmart.finishLoadMore();
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //找到对象
        mRel = (RecyclerView) view.findViewById(R.id.rel);
        //设置管理器
        mRel.setLayoutManager(new LinearLayoutManager(getActivity()));
        //创建集合
        banners = new ArrayList<>();
        list = new ArrayList<>();
        //创建适配器
        myAdapter = new MyAdapter(getActivity(), banners, list);
        //设置适配器
        mRel.setAdapter(myAdapter);
        initBanner();
        initData(page);
        mSmart = (SmartRefreshLayout) view.findViewById(R.id.smart);
        mSmart.setOnRefreshListener(this);
        mSmart.setOnLoadMoreListener(this);
        myAdapter.setMyOnClickLinstener(new MyAdapter.MyOnClickLinstener() {
            @Override
            public void onClick(int position) {
                mposition = position;
                if (mposition!=0){
                    FuliBean.DataBean.DatasBean datasBean = list.get(mposition);
                    list.remove(datasBean);
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void initData(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.getFuliurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<FuliBean> observable = apiService.getfuli(page);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FuliBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FuliBean fuliBean) {
                        List<FuliBean.DataBean.DatasBean> datas = fuliBean.getData().getDatas();
                        list.addAll(datas);
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initBanner() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.getbannerurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<BannerBean> observable = apiService.getBanner();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BannerBean bannerBean) {

                        List<BannerBean.DataBean> data = bannerBean.getData();
                        for (int i = 0; i < data.size(); i++) {
                            banners.add(data.get(i).getImagePath());
                        }
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        //下拉刷新
        handler.sendEmptyMessage(1);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        //上拉加载
        handler.sendEmptyMessage(2);
    }
}
