package com.wyp.wxplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.wyp.wxplayer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 将 ToolBar 设置为标题栏
        setSupportActionBar(mToolbar);
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
}
