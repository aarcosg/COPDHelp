package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.service.AchievementsHelper;
import com.aarcosg.copdhelp.ui.fragment.achievement.AchievementUnlockedFragment;

public class AchievementActivity extends BaseActivity {

    private static final String TAG = AchievementActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_content);
        addFragment(R.id.fragment_container,
                AchievementUnlockedFragment.newInstance(getIntent().getLongExtra(AchievementsHelper.EXTRA_ID,0)));
    }

    public static void launch(Activity activity, long achievementId) {
        Intent intent = new Intent(activity, AchievementActivity.class);
        intent.putExtra(AchievementsHelper.EXTRA_ID, achievementId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
