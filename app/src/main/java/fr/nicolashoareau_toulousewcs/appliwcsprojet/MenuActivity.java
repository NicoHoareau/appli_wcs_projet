package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity implements RequestFragment.OnFragmentInteractionListener, ValidationRequestFragment.OnFragmentInteractionListener, ActualityFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FloatingActionButton addQcm = findViewById(R.id.btn_float_request);
        addQcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCreateRequest= new Intent(MenuActivity.this, CreateRequestActivity.class);
                MenuActivity.this.startActivity(goToCreateRequest);;
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.name_tab3));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new fr.nicolashoareau_toulousewcs.appliwcsprojet.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }



}
