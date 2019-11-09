package com.sungshin.labsoon.cmr;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class CheckListActivity extends AppCompatActivity {
    ImageView home;
    Button again, ok;
    RadioButton action, adventure, ani, comedy, drama, family, horror, romance, thriller;
    String search_edit;
    NaverService naverService;

    RecyclerView recyclerView;
    ArrayList<MainDatas> mDatas = new ArrayList<MainDatas>();
    MainAdapter adapter;
    LinearLayoutManager mLayoutManager;

    MovieDataResult result;

    String query;
    int startQuery;
    int total;
    static String genre = "0";

    GregorianCalendar today = new GregorianCalendar();
    int MaxDate = today.get(today.YEAR);

    View dialogView2;
    TextView textDirector, textActor, textpubDate, textStory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        View dialogView = (View) View.inflate(CheckListActivity.this, R.layout.dialog1, null);
        action = (RadioButton) dialogView.findViewById(R.id.actionY);
        adventure = (RadioButton) dialogView.findViewById(R.id.adventureY);
        ani = (RadioButton) dialogView.findViewById(R.id.animationY);
        comedy = (RadioButton) dialogView.findViewById(R.id.comedyY);
        drama = (RadioButton) dialogView.findViewById(R.id.dramaY);
        family = (RadioButton) dialogView.findViewById(R.id.familyY);
        horror = (RadioButton) dialogView.findViewById(R.id.horrorY);
        romance = (RadioButton) dialogView.findViewById(R.id.romanceY);
        thriller = (RadioButton) dialogView.findViewById(R.id.thrillerY);
        dialogView2 = (View) View.inflate(CheckListActivity.this, R.layout.dialog2, null);
        textDirector = (TextView)dialogView2.findViewById(R.id.textDirector);
        textActor = (TextView)dialogView2.findViewById(R.id.textActor);
        textpubDate = (TextView)dialogView2.findViewById(R.id.textpubDate);
        textStory = (TextView)dialogView2.findViewById(R.id.textStory);
        search_edit = "의";
        naverService = ApplicationController.getInstance().getNaverService();

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


        AlertDialog.Builder dlg = new AlertDialog.Builder(CheckListActivity.this);
        dlg.setTitle("설문지");
        dlg.setView(dialogView);
        dlg.setPositiveButton("전송", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /**
                 * 3. Adapter 생성 후 recyclerview에 지정
                 */
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

                Boolean Check = CheckString(search_edit);
                if (Check) {

                    //mProgressDialog.show();
                    query = search_edit;
                    mLayoutManager.scrollToPositionWithOffset(0, 10);

                    if (action.isChecked()) {

                        /**
                         * 2. recyclerview에 보여줄 data
                         */
                        Call<MovieDataResult> getMovieData = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "19");

                        getMovieData.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

//                                    mDatas.clear();
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    if (adventure.isChecked()) {


                        Call<MovieDataResult> getMovieData2 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "6");

                        getMovieData2.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    if (ani.isChecked()) {


                        Call<MovieDataResult> getMovieData3 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "15");

                        getMovieData3.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (comedy.isChecked()) {


                        Call<MovieDataResult> getMovieData4 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "11");

                        getMovieData4.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (drama.isChecked()) {


                        Call<MovieDataResult> getMovieData5 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "1");

                        getMovieData5.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    if (family.isChecked()) {


                        Call<MovieDataResult> getMovieData6 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "12");

                        getMovieData6.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (horror.isChecked()) {

                        Call<MovieDataResult> getMovieData7 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "4");

                        getMovieData7.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (romance.isChecked()) {

                        Call<MovieDataResult> getMovieData8 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "5");

                        getMovieData8.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (thriller.isChecked()) {

                        Call<MovieDataResult> getMovieData9 = naverService.getMovieDataResult(query, 1, 15, MaxDate, 2014, "7");

                        getMovieData9.enqueue(new Callback<MovieDataResult>() {
                            @Override
                            public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                                startQuery = 0;
                                //Log.i("MaxDate", String.valueOf(MaxDate));
                                if (response.isSuccessful()) {
                                    result = response.body();
                                    Log.i("qwerty111", "onResponse: 들어옴");
                                    Log.i("qwerty111", "onResponse: result:" + result);

                                    //  mDatas.clear();
                                    if (result.items.size() == 0) {
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
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
                                //mProgressDialog.dismiss();
                                Log.i("qwerty111", "onFail: 들어옴");

                                Toast.makeText(getApplicationContext(), "서비스 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if(action.isChecked()==false&&adventure.isChecked()==false&&ani.isChecked()==false&&comedy.isChecked()==false
                            &&drama.isChecked()==false&&family.isChecked()==false&&horror.isChecked()==false&&romance.isChecked()==false&&thriller.isChecked()==false){
                        Toast.makeText(CheckListActivity.this,"다시하기버튼을 눌러서 설문지를 작성해주세요",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();


        home = (ImageView)

                findViewById(R.id.home_btn);

        again = (Button)

                findViewById(R.id.checklist_againbtn);

        //ok = (Button) findViewById(R.id.checklist_okaybtn);

        home.setOnClickListener(clickListener);
        again.setOnClickListener(clickListener);
        //ok.setOnClickListener(clickListener);

    }

    private View.OnClickListener recyclerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                final int position = recyclerView.getChildLayoutPosition(v);

                AlertDialog.Builder dlg = new AlertDialog.Builder(CheckListActivity.this);
                dlg.setTitle(mDatas.get(position).title);
                textDirector.setText("감독 : "+ RemoveHTMLTag(mDatas.get(position).director.replaceAll("[|]", ",")));
                textActor.setText("배우 : "+RemoveHTMLTag(mDatas.get(position).actor.replaceAll("[|]", ",")));
                textpubDate.setText("개봉연도 : "+mDatas.get(position).pubDate);
                textStory.setText("별점 : "+mDatas.get(position).userRating);
                dlg.setView(dialogView2);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ViewGroup)dialogView2.getParent()).removeView(dialogView2);
                    }
                });
                dlg.show();
            }catch (Exception e){
                ((ViewGroup)dialogView2.getParent()).removeView(dialogView2);
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_btn:
                    Intent intentHome = new Intent(CheckListActivity.this, MainActivity.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentHome);
                    break;

                case R.id.checklist_againbtn:
                    Intent checkIntent = new Intent(CheckListActivity.this, CheckListActivity.class);
                    startActivity(checkIntent);
                    break;
//                case R.id.checklist_okaybtn:
//                    Toast.makeText(getApplicationContext(), "확인", Toast.LENGTH_SHORT).show();
//                    break;
            }
        }
    };
}
