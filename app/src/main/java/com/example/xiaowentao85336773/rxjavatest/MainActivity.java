package com.example.xiaowentao85336773.rxjavatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String emitter="aaaaaaaaaaaaa";
                e.onNext(emitter);
                Log.d("aaaaaaaaaaaaa===",Thread.currentThread().getName());
                e.onNext("bbbbbbbbbbbbb");

            }
        }).subscribeOn(Schedulers.newThread())
          .doOnSubscribe(new Consumer<Disposable>() {
              @Override
              public void accept(Disposable disposable) throws Exception {
                  Log.d("doOnSubscribe",Thread.currentThread().getName());
              }
          })
                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        List<String> list=new ArrayList<>();
                        list.add(s+"转换了");
                        return Observable.fromIterable(list);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("快关闭 ",Thread.currentThread().getName());
                    }
                })
          .observeOn(AndroidSchedulers.mainThread())

           .subscribe(new Consumer<String>() {
               @Override
               public void accept(String s) throws Exception {
                   Log.d("成功啦 ",s+Thread.currentThread().getName());

               }
           }, new Consumer<Throwable>() {
               @Override
               public void accept(Throwable throwable) throws Exception {
                   Log.d("错误啦 ",Thread.currentThread().getName());
               }
           })


        ;
    }
}
