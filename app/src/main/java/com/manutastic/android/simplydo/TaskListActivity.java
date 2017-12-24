package com.manutastic.android.simplydo;

import android.support.v4.app.Fragment;

/**
 * Created by manut on 7/23/2017.
 */

public class TaskListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

}
