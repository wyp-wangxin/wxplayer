package com.wyp.wxplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.fragment.homepage.HomeFragment;
import com.wyp.wxplayer.fragment.TestFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private SparseArray<Fragment> mSparseArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 将 ToolBar 设置为标题栏
        setSupportActionBar(mToolbar);

        // 初始化 Fragment 集合
        mSparseArray = new SparseArray<>();//ctrl+alt+F : 换成成员变量

        mSparseArray.append(R.id.bottombar_home, new HomeFragment());//Ctrl + D :在下面复制当前行
        mSparseArray.append(R.id.bottombar_mv, TestFragment.newInstance("MV"));
        mSparseArray.append(R.id.bottombar_vbang, TestFragment.newInstance("V榜"));
        mSparseArray.append(R.id.bottombar_yuedan, TestFragment.newInstance("悦单"));

        // 处理底部栏
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);//ctrl+alt+v :快捷键生成变量名
        bottomBar.setItemsFromMenu(R.menu.bottombar, new OnMainMenuTabClickListener());//OnMainMenuTabClickListener Refactor提取为内部类
    }

    @Override
    /*
    Android一共有三种形式的菜单:
    1.选项菜单（optinosMenu）
    2.上下文菜单（ContextMenu）
    3.子菜单(subMenu)
    其中最常用的就是选项菜单(optionsMenu), 该菜单在点击 menu 按键 后会在对应的Activity底部显示出来。
    * */
    public boolean onCreateOptionsMenu(Menu menu) {//选项菜单
        // 创建 menu 菜单，这个菜单会依附到 ToolBar 上，ToolBar的特性
        getMenuInflater().inflate(R.menu.activity_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// 处理 menu 菜单的点击监听
        switch (item.getItemId()){
            case R.id.menu_main_settings:
                Intent intent = new Intent(this,WXSetting_Activity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OnMainMenuTabClickListener implements OnMenuTabClickListener {
        @Override
        /*
        * 选中了某一个 Tab
        * */
        public void onMenuTabSelected(int menuItemId) {

            Fragment fragment = mSparseArray.get(menuItemId);
            switchFragment(fragment);
            
        }

        @Override
        /*
            重复选中了某一个 Tab
        * */
        public void onMenuTabReSelected(int menuItemId) {
            //Toast.makeText(MainActivity.this,"onMenuTabReSelected",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *将参数里的 Fragment 显示出来
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.container,fragment);

        transaction.commit();
    }
}
