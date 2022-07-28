

package com.smartsocial.hdvideodownloader.model;

import androidx.fragment.app.Fragment;

import com.smartsocial.hdvideodownloader.VDApp;
import com.smartsocial.hdvideodownloader.activity.MainActivity;

public class VDFragment extends Fragment {

    public MainActivity getVDActivity() {
        return (MainActivity) getActivity();
    }

    public VDApp getVDApp() {
        return (VDApp) getActivity().getApplication();
    }
}
