package com.LiveTv;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.LiveTv.adapter.CategoryFragmentPagerAdapter;
import com.LiveTv.fragment.AboutUsFragment;
import com.LiveTv.fragment.ContactUsFragment;
import com.LiveTv.fragment.MoreappFragment;
import com.LiveTv.fragment.RateusFragment;
import com.chupamobile.android.ratemyapp.RateMyApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;
    public RecyclerView left_drawer;
    public LinearLayoutManager layoutManager;
    public ImageView img_nav;
    public RelativeLayout rel_toolbar;
    private AdView img_add;
    ImageLoader imageLoader = ImageLoader.getInstance();
    boolean mSlideState = false;
    private RateMyApp rate;
    public ViewPager viewPager;
    public PagerTabStrip pagerTabStrip;
    public CategoryFragmentPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fillatv_activity2);
        if (!check_network()) {
            alertDialogforConnetctivity();
        } else {
            setupWindowAnimations();
            rate = new RateMyApp(this, "My Cool Application", 0, 2);
            rate.setTextColor(Color.WHITE);
            rate.setMessage("If you like the application, would you kindly rate it?");
            rate.setTextSize(16);
            rate.start();
            imageLoader.init(GetOption.getConfig(this));
            WebAPIHelper web = new WebAPIHelper(Constant.getaddimage, MainActivity.this, true);
            web.callRequestGet("http://fillatv.com/fillatv/service/get_banners.php");
            rel_toolbar = (RelativeLayout) findViewById(R.id.layout_toolbar);
            rel_toolbar.setVisibility(View.VISIBLE);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
            left_drawer = (RecyclerView) findViewById(R.id.left_drawer);
            left_drawer.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            left_drawer.setLayoutManager(layoutManager);
            img_nav = (ImageView) findViewById(R.id.img_nav);
            img_nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickEventSlide();
                }
            });

            final View framemain = findViewById(R.id.layout_main);
            final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                    R.string.drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    super.onDrawerSlide(drawerView, slideOffset);
                    float moveFactor = (left_drawer.getWidth() * slideOffset);
                    framemain.setTranslationX(moveFactor);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
            mDrawerLayout.setDrawerListener(new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close));
            mDrawerLayout.addDrawerListener(toggle);
            left_drawer.setAdapter(new MainDrawerAdapter());


            img_add = (AdView) findViewById(R.id.img_add);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            img_add.loadAd(adRequest);

            adapter = new CategoryFragmentPagerAdapter(getSupportFragmentManager());

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerStrip);
            pagerTabStrip.setTabIndicatorColor(Color.RED);

            Typeface fontTypeFace = Typeface.createFromAsset(getAssets(),
                    "fonts/MontserratRegular.ttf");
            for (int i = 0; i < pagerTabStrip.getChildCount(); ++i) {
                View nextChild = pagerTabStrip.getChildAt(i);
                if (nextChild instanceof TextView) {
                    TextView textViewToConvert = (TextView) nextChild;
                    textViewToConvert.setTypeface(fontTypeFace);
                }
            }
            viewPager.setOffscreenPageLimit(2);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i2) {

                }

                @Override
                public void onPageSelected(int i) {
                    Log.e("page s", i + "");
                    viewPager.setCurrentItem(i);
                    // here you will get the position of selected page
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }


//        PagerTabStrip tabLayout = (PagerTabStrip) findViewById(R.id.tablayout);
//        tabLayout.setupWithViewPager(viewPager);
    }

    public void alertDialogforConnetctivity() {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            alertDialog.setTitle("Connection not available");
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setCancelable(false);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (check_network()){
                        alertDialog.hide();
                        Intent i = new Intent(MainActivity.this,SplashActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        alertDialogforConnetctivity();
                    }


                }
            });

            alertDialog.show();
        }
        catch(Exception e)
        {
            Log.d("ERROR OCUURED", "Show Dialog: "+e.getMessage());
        }
    }


    public boolean check_network() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info == null)
                return false;
            else if (!info.isConnected())
                return false;
            else return info.isAvailable();

        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(1000);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    public void clickEventSlide() {
        if (mSlideState) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (img_add != null) {
            img_add.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (img_add != null) {
            img_add.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (img_add != null) {
            img_add.pause();
        }
//        if (check_network()) {
//            if (rate.getDialog() != null && rate.getDialog().isShowing()) {
//                rate.getDialog().dismiss();
//            }
//        }

    }

    public void onBackClicked() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (fragment instanceof RateusFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int i = 0; i < count; i++) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        } else if (fragment instanceof MoreappFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int i = 0; i < count; i++) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        } else if (fragment instanceof AboutUsFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int i = 0; i < count; i++) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        } else if (fragment instanceof ContactUsFragment) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                for (int i = 0; i < count; i++) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClicked();
    }

    public void viewdata(String data) {
        String img;
        try {
            JSONArray j_array = new JSONArray(data);
            for (int i = 0; i < j_array.length(); i++) {
                JSONObject j_obj = j_array.getJSONObject(i);
                System.out.print(j_obj.optString("status"));
                System.out.print(j_obj.optString("id"));
                System.out.print(j_obj.optString("Image"));
                img = j_obj.optString("Image");


                // imageLoader.displayImage(img, img_add, GetOption.getOption());
            }
        } catch (Exception e) {
        }
    }
    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" +getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    class MainDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> nav_drawer_item = new ArrayList<String>();
        private ArrayList<Integer> nav_drawer_images = new ArrayList<Integer>();

        public MainDrawerAdapter() {
            nav_drawer_item.add("About us");

            nav_drawer_item.add("Home");
            nav_drawer_item.add("Rate us");
            nav_drawer_item.add("More apps");
            nav_drawer_item.add("About us");
            nav_drawer_item.add("Contact us");

            nav_drawer_images.add(R.drawable.home);
            nav_drawer_images.add(R.drawable.home);
            nav_drawer_images.add(R.drawable.rate);
            nav_drawer_images.add(R.drawable.more_apps);
            nav_drawer_images.add(R.drawable.about);
            nav_drawer_images.add(R.drawable.contact);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (!imageLoader.isInited())
                imageLoader.init(GetOption.getConfig(MainActivity.this));
            View v;
            if (viewType == 0) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.drawer_header, null, true);
                return new ViewHeader(v);
            } else {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.drawer_list, null, true);
                return new ViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof ViewHolder) {
                final ViewHolder holderDrawer = (ViewHolder) holder;

                holderDrawer.img_drawer.setImageResource(nav_drawer_images.get(position));
                holderDrawer.txt_drawer.setText(nav_drawer_item.get(position));
                holderDrawer.rel_drawer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("posssss", position + "");
                        switch (position) {
                            case 0:
                                clickEventSlide();
                                break;
                            case 2:
                                launchMarket();
                                break;
                            case 3:
                                MoreappFragment fragmenttab3 = new MoreappFragment();
                                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                                ft2.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_from_left);
                                ft2.add(R.id.frame_container, fragmenttab3);
                                ft2.addToBackStack(null);
                                ft2.commit();
                                Log.e("pos", position + "");
                                break;
                            case 4:
                                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                                ft4.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_from_left);
                                ft4.add(R.id.frame_container, aboutUsFragment);
                                ft4.addToBackStack(null);
                                ft4.commit();
                                Log.e("pos", position + "");
                                break;
                            case 5:
                                ContactUsFragment fragmenttab5 = new ContactUsFragment();
                                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                                ft5.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_from_left);
                                ft5.add(R.id.frame_container, fragmenttab5);
                                ft5.addToBackStack(null);
                                ft5.commit();
                                Log.e("pos", position + "");
                                break;
                        }

                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                    }

                });
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else {
                return position;
            }
        }

        @Override
        public int getItemCount() {
            return nav_drawer_item.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txt_drawer;
            public ImageView img_drawer;
            RelativeLayout rel_drawer;


            public ViewHolder(View view) {
                super(view);
                txt_drawer = (TextView) view.findViewById(R.id.txt_drawer);
                img_drawer = (ImageView) view.findViewById(R.id.img_drawer);
                rel_drawer = (RelativeLayout) view.findViewById(R.id.rel_drawer);
            }
        }

        private class ViewHeader extends RecyclerView.ViewHolder {
            public ImageView img_header;
            View view;

            public ViewHeader(View v) {
                super(v);
                view = v;
                img_header = (ImageView) view.findViewById(R.id.img_header);
            }
        }
    }
}


