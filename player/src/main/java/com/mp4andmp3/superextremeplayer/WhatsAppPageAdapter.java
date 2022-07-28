package com.mp4andmp3.superextremeplayer;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WhatsAppPageAdapter extends FragmentStatePagerAdapter {
    int countTab;

    public WhatsAppPageAdapter(FragmentManager fragmentManager, int i) {
        super(fragmentManager);
        this.countTab = i;
    }

    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        String str = "StatusType";
        if (i == 0) {
            WhatsappVideoListFragment whatsappVideoListFragment = new WhatsappVideoListFragment();
            bundle.putString(str, "Saved");
            whatsappVideoListFragment.setArguments(bundle);
            return whatsappVideoListFragment;
        } else if (i != 1) {
            return null;
        } else {
            WhatsappVideoListFragment whatsappVideoListFragment2 = new WhatsappVideoListFragment();
            bundle.putString(str, "Seen");
            whatsappVideoListFragment2.setArguments(bundle);
            return whatsappVideoListFragment2;
        }
    }

    public int getCount() {
        return this.countTab;
    }
}
