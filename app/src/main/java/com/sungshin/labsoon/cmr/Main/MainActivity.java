package com.sungshin.labsoon.cmr.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sungshin.labsoon.cmr.CheckListActivity;
import com.sungshin.labsoon.cmr.Global.ApplicationController;
import com.sungshin.labsoon.cmr.NaverService.NaverService;
import com.sungshin.labsoon.cmr.R;

import com.sungshin.labsoon.cmr.GenreActivity.GenreActionActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreAdventureActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreAnimationActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreComedyActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreDramaActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreFamilyActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreHorrorActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreRomanceActivity;
import com.sungshin.labsoon.cmr.GenreActivity.GenreThrillerActivity;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ImageView home_btn, checklist_btn;
    EditText search_edit;
    Button search_btn;

    NaverService naverService;

    RecyclerView recyclerView;
    ArrayList<MainDatas> mDatas = new ArrayList<MainDatas>();
    MainAdapter adapter;
    LinearLayoutManager mLayoutManager;

    MovieDataResult result;

    Button  GenreBtn_Action, GenreBtn_Drama, GenreBtn_Romance, GenreBtn_Comedy, GenreBtn_Family,
            GenreBtn_Animation,GenreBtn_Horror,GenreBtn_Thriller,GenreBtn_Adventure;

    String query;
    int startQuery;
    int total;
    View dialogView;
    TextView textDirector, textActor, textpubDate, textStory;


    GregorianCalendar today = new GregorianCalendar( );
    int MaxDate = today.get ( today.YEAR );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home_btn = (ImageView)findViewById(R.id.home_btn);
        checklist_btn = (ImageView)findViewById(R.id.checklist_btn);
        search_edit = (EditText)findViewById(R.id.search_edit);
        search_btn=(Button)findViewById(R.id.search_btn);
        GenreBtn_Action=(Button)findViewById(R.id.GenreBtn_Action);
        GenreBtn_Drama=(Button)findViewById(R.id.GenreBtn_Drama);
        GenreBtn_Romance=(Button)findViewById(R.id.GenreBtn_Romance);
        GenreBtn_Comedy=(Button)findViewById(R.id.GenreBtn_Comedy);
        GenreBtn_Family=(Button)findViewById(R.id.GenreBtn_Family);
        GenreBtn_Animation=(Button)findViewById(R.id.GenreBtn_Animation);
        GenreBtn_Horror=(Button)findViewById(R.id.GenreBtn_Horror);
        GenreBtn_Thriller=(Button)findViewById(R.id.GenreBtn_Thriller);
        GenreBtn_Adventure=(Button)findViewById(R.id.GenreBtn_Adventure);

        dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog2, null);
        textDirector = (TextView)dialogView.findViewById(R.id.textDirector);
        textActor = (TextView)dialogView.findViewById(R.id.textActor);
        textpubDate = (TextView)dialogView.findViewById(R.id.textpubDate);
        textStory = (TextView)dialogView.findViewById(R.id.textStory);


        naverService = ApplicationController.getInstance().getNaverService();

        home_btn.setOnClickListener(clickListener);
        checklist_btn.setOnClickListener(clickListener);
        GenreBtn_Action.setOnClickListener(clickListener);
        GenreBtn_Drama.setOnClickListener(clickListener);
        GenreBtn_Romance.setOnClickListener(clickListener);
        GenreBtn_Comedy.setOnClickListener(clickListener);
        GenreBtn_Family.setOnClickListener(clickListener);
        GenreBtn_Animation.setOnClickListener(clickListener);
        GenreBtn_Horror.setOnClickListener(clickListener);
        GenreBtn_Thriller.setOnClickListener(clickListener);
        GenreBtn_Adventure.setOnClickListener(clickListener);

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
         * 2. recyclerview에 보여줄 data
         */
        //크롤링한 데이터들 여기에 추가하면 됨 !!!!
//
//        mDatas.add(new MainDatas("아가씨", "www.naver.com", "", "2016", "박찬 머더라", "하정우, 김민희", "10"));
//        mDatas.add(new MainDatas("아저씨", "www.naver.com", "", "2016", "박찬 머더라", "하정우, 김민희", "4"));
//        mDatas.add(new MainDatas("아버지가 이상해", "www.naver.com", "0", "2016", "박찬 머더라", "하정우, 김민희", "8"));
//        mDatas.add(new MainDatas("미녀와 야수", "www.naver.com", "", "2016", "박찬 머더라", "하정우, 김민희", "6"));
//        mDatas.add(new MainDatas("분노의 질주", "www.naver.com", "", "2016", "박찬 머더라", "하정우, 김민희", "3"));
//        mDatas.add(new MainDatas("졸려", "www.naver.com", "0", "2016", "박찬 머더라", "하정우, 김민희", "1"));
        /**
         * 3. Adapter 생성 후 recyclerview에 지정
         */
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //네이버 네트워킹 테스트
                Boolean Check = CheckString(search_edit.getText().toString());
                if (Check) {

                    //mProgressDialog.show();
                    query = search_edit.getText().toString();
                    mLayoutManager.scrollToPositionWithOffset(0, 10);
                    /**
                     * 2. recyclerview에 보여줄 data
                     */

                    Call<MovieDataResult> getMovieData = naverService.getMovieDataResult(query, 1, 10, MaxDate, 1900,"*");
                    getMovieData.enqueue(new Callback<MovieDataResult>() {
                        @Override
                        public void onResponse(Call<MovieDataResult> call, Response<MovieDataResult> response) {
                            startQuery = 0;
                            //Log.i("MaxDate", String.valueOf(MaxDate));
                            if (response.isSuccessful()) {
                                result = response.body();
                                Log.i("qwerty111", "onResponse: 들어옴");
                                Log.i("qwerty111", "onResponse: result:"+result);

                                mDatas.clear();
                                if (result.items.size() == 0) {
                                    search_edit.setText("");
                                    search_edit.requestFocus();
//                                    searchKorean.setText("검색결과가 없습니다.");
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
                                } else {
//                                    searchKorean.setText("");
//                                    search_nosearch.setImageResource(R.drawable.search_nosearchimage);
//                                    search_nosearch.setVisibility(View.INVISIBLE);
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
                } else {
                    search_edit.setText("");
                    search_edit.requestFocus();
                    Toast.makeText(getApplicationContext(), "1자이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private View.OnClickListener recyclerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                final int position = recyclerView.getChildLayoutPosition(v);
//            Toast.makeText(getApplicationContext(), position+"번째 선택", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
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
        }
    };


    View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.home_btn :
                    break;
                case R.id.checklist_btn :
                    Intent checkIntent = new Intent(MainActivity.this, CheckListActivity.class);
                    startActivity(checkIntent);
                    break;
                case R.id.GenreBtn_Action :
                    Intent actionGenreIntent = new Intent(MainActivity.this, GenreActionActivity.class);
                    startActivity(actionGenreIntent);
                    break;
                case R.id.GenreBtn_Drama:
                    Intent dramaGenreIntent = new Intent(MainActivity.this, GenreDramaActivity.class);
                    startActivity(dramaGenreIntent);
                    break;
                case R.id.GenreBtn_Romance:
                    Intent romanceGenreIntent = new Intent(MainActivity.this, GenreRomanceActivity.class);
                    startActivity(romanceGenreIntent);
                    break;
                case R.id.GenreBtn_Comedy:
                    Intent comedyGenreIntent = new Intent(MainActivity.this, GenreComedyActivity.class);
                    startActivity(comedyGenreIntent);
                    break;
                case R.id.GenreBtn_Family:
                    Intent familyGenreIntent = new Intent(MainActivity.this,GenreFamilyActivity.class);
                    startActivity(familyGenreIntent);
                    break;
                case R.id.GenreBtn_Animation:
                    Intent animationGenreIntent = new Intent(MainActivity.this, GenreAnimationActivity.class);
                    startActivity(animationGenreIntent);
                    break;
                case R.id.GenreBtn_Horror:
                    Intent horrorGenreIntent = new Intent(MainActivity.this, GenreHorrorActivity.class);
                    startActivity(horrorGenreIntent);
                    break;
                case R.id.GenreBtn_Thriller:
                    Intent thrillerGenreIntent = new Intent(MainActivity.this, GenreThrillerActivity.class);
                    startActivity(thrillerGenreIntent);
                    break;
                case R.id.GenreBtn_Adventure:
                    Intent adventureGenreIntent = new Intent(MainActivity.this, GenreAdventureActivity.class);
                    startActivity(adventureGenreIntent);
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

    //특수문자 제거
    public String RemoveSpecitialCharacter(String str) {
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        str = str.replaceAll(match, " ");
        return str;
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
