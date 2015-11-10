package com.yezimm.gathering;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.sogou.map.mobile.engine.core.MapView;
import com.sogou.map.mobile.engine.framework.FrameworkService;
import com.yezimm.gathering.mapsdk.ctrl.MapGpsView;
import com.yezimm.gathering.mapsdk.ctrl.MapViewCallBackListener;
import com.yezimm.gathering.mapsdk.ctrl.MapWrapperController;
import com.yezimm.gathering.mapsdk.location.LocBtnManager;
import com.yezimm.gathering.mapsdk.location.LocationController;
import com.yezimm.gathering.mapsdk.location.LocationController.LocationStatus;

import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String TAG = "MainActivity";

    private static MainActivity INSTANCE ;
    private MapWrapperController mMapCtrl;
    private MapView mMapView;
    private ImageButton mGpsButton;
    private LocationController mLocCtrl;
    private LocBtnManager mLocBtnManager;
    private MapViewCallBackListener mMapViewCallBackListener;
    private File sLogDir;

    public static MainActivity getInstance() {
        return INSTANCE;
    }

    public MapWrapperController getMapController() {
        return mMapCtrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        INSTANCE = this ;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapView = (MapView) findViewById(R.id.MapView);
        mGpsButton = (ImageButton) findViewById(R.id.GpsButton);
        mMapCtrl = new MapWrapperController(mMapView);
        mLocCtrl =  LocationController.getInstance();
        mLocBtnManager = new LocBtnManager(this);
        mMapViewCallBackListener = new MapViewCallBackListener();
        mMapCtrl.addMapListener(mMapViewCallBackListener);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        int gpsWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.toolbar_background2).getWidth();
        // 底边距= 底栏高度 + 定位按钮距底栏的距离 + 定位按钮背景图片的透明边距
        params.setMargins(gpsWidth + 2 * (int)getResources().getDimension(R.dimen.common_margin)
                , 0, 0, (int)getResources().getDimension(R.dimen.common_margin) );
        mMapCtrl.setLogoLayoutParam(params, true);// 文字左对齐
        mGpsButton.setOnClickListener(this);
        setLocBtnStatus(LocationStatus.LOCATING);
        mLocCtrl.start(5 * 1000);
        mLocCtrl.addListener(mOnShowPopListener);

        File sdcardDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if(sdcardDir != null){
            sLogDir = new File(sdcardDir, "sogouLocDemo");
        }
        if (!sLogDir.exists()) {
            sLogDir.mkdirs();
        }
        if(sLogDir != null){
            FrameworkService.setAppPath(sLogDir.getAbsolutePath());
        }else if(sdcardDir != null){
            FrameworkService.setAppPath(sdcardDir.getAbsolutePath());
        }

        mMapCtrl.setGpsImg(MapGpsView.getInstance().getGPSBitMap(MapGpsView.GpsViewStateType.common_location_normal));
        mMapCtrl.setGpsDirectionImg();
        mMapCtrl.updateGpsDirectionSize();
        mMapCtrl.setGpsCircleEdgeColor(Color.parseColor("#00ffffff"));
        mMapCtrl.setGpsCircleFillColor(Color.parseColor("#19409bf8"));

        mMapCtrl.setGpsVisibility(false);
        mMapCtrl.setGpsDirectionVisibility(false);
        mMapCtrl.setGpsShadowImg();
        mMapCtrl.setGpsDirectionShdowImg();

        // disable Z rotate
        mMapCtrl.setEnableRotate('z', false);
        mMapCtrl.setEnableRotate('x', false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public interface OnShowPopListener {
        void onStart() ;
        void showPop() ;//显示泡泡层
        void showError(HttpException error) ;
    }

    private OnShowPopListener mOnShowPopListener = new OnShowPopListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void showPop() {

        }
        @Override
        public void showError(HttpException error) {

            Toast.makeText(MainActivity.this, "网络超时，请检查网络", Toast.LENGTH_SHORT).show();
        }
    };

    public void setLocBtnStatus(LocationStatus status) {
        if (mLocCtrl != null)
            mLocCtrl.setLocationStatus(status);

        switch (status) {
            case LOCATING:
                mGpsButton.setImageResource(R.drawable.location_gps_loading);
                AnimationDrawable anim = (AnimationDrawable) mGpsButton.getDrawable();
                anim.start();
                break;
            case BROWS:
                mGpsButton.setImageResource(R.mipmap.maptool_gps_point_normal);
                break;
            case NAV:
                mLocCtrl.setPreLocationStatus(status);
                mGpsButton.setImageResource(R.mipmap.maptool_gps_point_navi);
                break;
            case FOLLOW:
                mLocCtrl.setPreLocationStatus(status);
                mGpsButton.setImageResource(R.mipmap.maptool_gps_point_following);
                break;
            default:
                mGpsButton.setImageResource(R.mipmap.maptool_gps_point_normal);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
