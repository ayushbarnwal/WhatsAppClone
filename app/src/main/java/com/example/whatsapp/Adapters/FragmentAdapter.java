package com.example.whatsapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whatsapp.Fragments.CallFragment;
import com.example.whatsapp.Fragments.ChatFragment;
import com.example.whatsapp.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case(0): return new ChatFragment();
            case(1): return new StatusFragment();
            case(2): return  new CallFragment();
        }
        return new ChatFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if(position==0) return title="CHATS";
        if(position==1) return title="GROUP CHAT";
        if(position==2) return title="CALL";
        return super.getPageTitle(position);
    }
}
