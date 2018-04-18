package com.onehookinc.onehooklibraryandroid.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseActivity;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;
import com.onehookinc.onehooklibraryandroid.sample.common.StackActivity;
import com.onehookinc.onehooklibraryandroid.sample.samples.DeviceUtilFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.FlipperViewSampleFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.FlowLayoutSampleFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.NotFoundFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.ProgressViewSampleFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.StackLayoutSampleFragment;
import com.onehookinc.onehooklibraryandroid.sample.samples.TagsViewSampleFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG_LIST_FRAGMENT = "tagListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SampleItem parent = new SampleItem("oneHook Samples",
                new SampleItem("Utilities",
                        new SampleItem("Device", SampleItem.SampleItemType.DEVICE)),
                new SampleItem("View",
                        new SampleItem("StackLayout", SampleItem.SampleItemType.STACK_LAYOUT),
                        new SampleItem("FlowLayout", SampleItem.SampleItemType.FLOW_LAYOUT),
                        new SampleItem("TagsView", SampleItem.SampleItemType.TAGS_VIEW),
                        new SampleItem("FlipperView", SampleItem.SampleItemType.FLIPPER_VIEW),
                        new SampleItem("ProgressViews", SampleItem.SampleItemType.PROGRESS_VIEW)
                ),
                new SampleItem("Camera", SampleItem.SampleItemType.CAMERA),
                new SampleItem("Dialogs", SampleItem.SampleItemType.DIALOG));

        if (getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT) == null) {
            final SampleListFragment fragment = SampleListFragment.newInstance(parent, false);
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

        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onItemClicked(final SampleItem item) {
        if (item.getType() == SampleItem.SampleItemType.CATEGORY) {
            /* has sub pages */
            final SampleListFragment fragment = SampleListFragment.newInstance(item, true);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.id_common_fragment, fragment, TAG_LIST_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        } else {
            final Intent intent = createSampleIntent(item.getType());
            startActivityAsStack(intent);
        }
    }

    private Intent createSampleIntent(final SampleItem.SampleItemType type) {
        switch (type) {
            case DEVICE:
                return StackActivity.intent(this, DeviceUtilFragment.class);
            case STACK_LAYOUT:
                return StackActivity.intent(this, StackLayoutSampleFragment.class);
            case FLOW_LAYOUT:
                return StackActivity.intent(this, FlowLayoutSampleFragment.class);
            case TAGS_VIEW:
                return StackActivity.intent(this, TagsViewSampleFragment.class);
            case FLIPPER_VIEW:
                return StackActivity.intent(this, FlipperViewSampleFragment.class);
            case PROGRESS_VIEW:
                return StackActivity.intent(this, ProgressViewSampleFragment.class);
            default:
                return StackActivity.intent(this, NotFoundFragment.class);
        }
    }
}
