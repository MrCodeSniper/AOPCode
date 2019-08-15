package mrcodesniper.me.transformcode;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.me.codesniper.CodeSniper;

import mrcodesniper.me.apt_api.BV;
import mrcodesniper.me.apt_api.Test;
import mrcodesniper.me.transformcode.ui.main.SectionsPagerAdapter;
@Test
public class MainActivity extends AppCompatActivity {

    @BV(R.id.title)
    TextView mTvTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeSniper.inject(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show(); }
        });

    }

    @TestAnnoTrace(value = "chenhong_test", type = 1)
    public void test(View view){
        Log.d("chenhong","clicktest");
//        int i=5/0;
    }

}