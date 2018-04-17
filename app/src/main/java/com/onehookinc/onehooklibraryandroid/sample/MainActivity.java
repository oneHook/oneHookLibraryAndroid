package com.onehookinc.onehooklibraryandroid.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.onehookinc.onehooklibraryandroid.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_LIST_FRAGMENT = "tagListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SampleItem parent = new SampleItem("oneHookLibrarySample",
                new SampleItem("View",
                        new SampleItem("StackLayout", SampleItem.SampleItemType.STACK_LAYOUT)));

        if(getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT) == null) {
            final SampleListFragment fragment = SampleListFragment.newInstance(parent);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.id_common_fragment, fragment, TAG_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
