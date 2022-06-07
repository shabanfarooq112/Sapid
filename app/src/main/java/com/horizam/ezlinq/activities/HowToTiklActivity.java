package com.horizam.ezlinq.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.horizam.ezlinq.R;

public class HowToTiklActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ChangeLanguage.Companion.setLocale();


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_howtotikl);
        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);



        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.layout_how_to_pop_one,
                R.layout.layout_how_to_pop_two,
                R.layout.layout_how_to_pop_three,
                R.layout.layout_how_to_pop_four,
                R.layout.layout_how_to_pop_five,
                R.layout.layout_how_to_pop_six};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    //

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(HowToTiklActivity.this, MainActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT

            } else {

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ////page adapter
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            int i=0;

            while (i!=6){
            if (position==i) {
                if (i==0){
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.gif_popl_to_iphone)
                            .into(gif);
                }else if(i==1){
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.gif_read_activate)
                            .into(gif);
                }else if(i==2){
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.gif_popl_direct)
                            .into(gif);
                } else if(i==3){
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.gif_popl_direct)
                            .into(gif);
                }
                else if(i==4){
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.gif_qr)
                            .into(gif);
                }

                if (i == 5) {
                    ImageView gif=view.findViewById(R.id.iv_gif);
                    Glide.with(getApplicationContext())
                            .load(R.raw.lets_get_popin)
                            .into(gif);
                    TextView textViewContinue = view.findViewById(R.id.tv_continue_layout_one);
                    textViewContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(HowToTiklActivity.this, ActivateActivity.class));
                            finish();
                        }
                    });
                    ImageView closeScreen=view.findViewById(R.id.civ_close_screen_six);
                    closeScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(HowToTiklActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }
                else {
                    TextView textViewContinue = view.findViewById(R.id.tv_continue_layout_one);
                    textViewContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int current = getItem(1);
                            if (current < layouts.length) {
                                viewPager.setCurrentItem(current);
                            }
                        }
                           });
                }
            }
                i++;
             }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}