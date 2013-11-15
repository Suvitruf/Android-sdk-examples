package ru.suvitruf.purchasefordisablingadvertising;

import android.content.Context;
import android.widget.RelativeLayout;

public interface AdsControllerBase {
        
        public void createView( RelativeLayout layout);
        public void show(boolean show);
        public void onStart();
        public void onDestroy();
        public void onResume();
        public void onStop();
}