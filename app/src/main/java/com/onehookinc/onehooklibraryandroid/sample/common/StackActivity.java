package com.onehookinc.onehooklibraryandroid.sample.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.WindowManager;

import com.onehookinc.onehooklibraryandroid.R;

/**
 * A thin activity that hosts any fragment caller wish to host, all the
 * extra(args) will be provided to fragment for convenience.
 *
 * @author EagleDiao
 */
public class StackActivity extends BaseActivity {

    /**
     * Tag for hosting fragment.
     */
    protected static final String TAG_FRAGMENT = "fragment";

    /**
     * Argument key for storing the class(fragment) type.
     */
    protected static final String ARG_FRAGMENT_CLASS = "fragmentClass";

    /**
     * Produce a new intent for providing the fragment class. Arguments that
     * needed by the fragment should be put into the intent returned.
     *
     * @param context       context
     * @param fragmentClass fragment type
     * @return new intent
     */
    public static Intent intent(final Context context,
                                final Class<? extends Fragment> fragmentClass) {
        return intent(context, fragmentClass, new Bundle());
    }

    /**
     * Produce a new intent for providing the fragment class. Arguments that
     * needed by the fragment should be put into the intent returned.
     *
     * @param context       context
     * @param fragmentClass fragment type
     * @return new intent
     */
    public static Intent intent(final Context context,
                                final Class<? extends Fragment> fragmentClass, final Bundle args) {
        final Intent intent = new Intent(context, StackActivity.class);
        args.putSerializable(ARG_FRAGMENT_CLASS, fragmentClass);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        /*
         * Init the fragment if not yet init.
         */
        if (getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT) == null) {
            try {
                @SuppressWarnings("unchecked") final Class<Fragment> fragmentClass =
                        (Class<Fragment>) getIntent().getExtras().getSerializable(ARG_FRAGMENT_CLASS);
                final Fragment fragment = fragmentClass.newInstance();
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.id_common_fragment,
                        fragment, TAG_FRAGMENT).commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
                finish();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                finish();
            } catch (NullPointerException e) {
                e.printStackTrace();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void finish() {
        finishActivityAsStack();
    }
}
