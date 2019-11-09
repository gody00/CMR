package com.sungshin.labsoon.cmr.GenreActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sungshin.labsoon.cmr.Global.ApplicationController;
import com.sungshin.labsoon.cmr.Main.MainActivity;
import com.sungshin.labsoon.cmr.Main.MainAdapter;
import com.sungshin.labsoon.cmr.Main.MainDatas;
import com.sungshin.labsoon.cmr.Main.MovieDataResult;
import com.sungshin.labsoon.cmr.NaverService.NaverService;
import com.sungshin.labsoon.cmr.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenreAnimationActivity extends AppCompatActivity {
    ImageView home_btn;
    String search[] = {"의", "이"};
    NaverService naverService;

    RecyclerView recyclerView;
    ArrayList<MainDatas> mDatas = new ArrayList<MainDatas>();
    MainAdapter adapter;
    LinearLayoutManager mLayoutManager;
    View dialogView;
    TextView textDirector, textActor, textpubDate, textStory;
    MovieDataResult result;

    String query;
    int startQuery;
    int total;


    GregorianCalendar today = new GregorianCalendar();
    int MaxDate = today.get(today.YEAR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_animation);
        home_btn = (ImageView) findViewById(R.id.home_btn);
        dialogView = (View) View.inflate(GenreAnimationActivity.this, R.layout.dialog2, null);
        textDirector = (TextView)dialogView.findViewById(R.id.textDirector);
        textActor = (TextView)dialogView.findViewById(R.id.textActor);
        textpubDate = (TextView)dialogView.findViewById(R.id.textpubDate);
        textStory = (TextView)dialogView.findViewById(R.id.textStory);

        naverService = ApplicationController.getInstance().getNaverService();

        home_btn.setOnClickListener(clickListener);
        /**
         * 1. recyclerview 초기화
         */
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerview);
        //각 item의 크기가 일정할 경우 고정
        recyclerView.setHasFixedSize(true);
        mDatas = new ArrayList<MainDatas>();


        adapter = new MainAdapter(mDatas, recyclerClickListener);
        // LayoutManager 초기화
        // layoutManager 설정
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        /**
         * 3. Adapter 생성 후 recyclerview에 지정
         */
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        for (int i = 0; i < search.length; i++) {
            Boolean Check = CheckString(search[i]);
            if (Check) {

                //mProgressDialog.show();
                query = search[i];
                mLayoutManager.scrollToPositionWithOffset(0, 10);
                /**
                 * 2. recyclerview에 보여줄 data
                 */

                Call<MovieDataResult> getMovieData = naverService.getMovieDataResult(query, 1, 40, MaxDate, 2014, "15");
                getMovieData.enqueue(new Callback<MovieDataResult>() {
                    @Override
                    public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                        startQuery = 0;
                        //Log.i("MaxDate", String.valueOf(MaxDate));
                        if (response.isSuccessful()) {
                            result = response.body();
                            Log.i("qwerty111", "onResponse: 들어옴");
                            Log.i("qwerty111", "onResponse: result:" + result);


                            if (result.items.size() == 0) {
                            } else {
                                total = result.total;
                                for (int i = 0; i < result.items.size(); i++) {
                                    MainDatas detail =
                                            new MainDatas
                                                    (RemoveHTMLTag(result.items.get(i).title),
                                                            result.items.get(i).link,
                                                            result.items.get(i).image,
                                                            result.items.get(i).pubDate,
                                                            RemoveHTMLTag(result.items.get(i).director.replaceAll("[|]", ",")),
                                                            result.items.get(i).actor,
                                                            result.items.get(i).userRating);
                                    mDatas.add(detail);
                                }
                                startQuery = startQuery + 10;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        // mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<MovieDataResult> call, Throwable t) {
                        Log.i("qwerty111", "onFail: 들어옴");

                        Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private View.OnClickListener recyclerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                final int position = recyclerView.getChildLayoutPosition(v);

                AlertDialog.Builder dlg = new AlertDialog.Builder(GenreAnimationActivity.this);
                dlg.setTitle(mDatas.get(position).title);
                textDirector.setText("감독 : "+ RemoveHTMLTag(mDatas.get(position).director.replaceAll("[|]", ",")));
                textActor.setText("배우 : "+RemoveHTMLTag(mDatas.get(position).actor.replaceAll("[|]", ",")));
                textpubDate.setText("개봉연도 : "+mDatas.get(position).pubDate);
                textStory.setText("별점 : "+mDatas.get(position).userRating);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ViewGroup)dialogView.getParent()).removeView(dialogView);
                    }
                });
                dlg.show();
            }catch (Exception e){
                ((ViewGroup)dialogView.getParent()).removeView(dialogView);
            }

        }
    };
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_btn:
                    Intent intentHome = new Intent(GenreAnimationActivity.this, MainActivity.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentHome);
                    break;

            }
        }
    };

    //태그제거 메서드
    public String RemoveHTMLTag(String changeStr) {
        if (changeStr != null && !changeStr.equals("")) {
            changeStr = changeStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        } else {
            changeStr = "";
        }
        return changeStr;
    }

    public Boolean CheckString(String str) {
        str = str.trim();
        if (str != null && str.length() >= 1) {
            return true;
        } else {
            return false;
        }
    }
}
