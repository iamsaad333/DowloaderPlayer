package com.mp4andmp3.superextremeplayer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener;

public class WhatsappVideoList extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_whatsapp_video_list);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText((CharSequence) "Saved Status"));
        tabLayout.addTab(tabLayout.newTab().setText((CharSequence) "Seen Status"));
        tabLayout.setTabGravity(0);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new WhatsAppPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabReselected(Tab tab) {
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }
}
