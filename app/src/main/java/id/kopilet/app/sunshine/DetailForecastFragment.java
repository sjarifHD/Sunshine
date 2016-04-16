package id.kopilet.app.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.kopilet.app.sunshine.model.WeatherRealm;
import id.kopilet.app.sunshine.utils.DataManager;
import id.kopilet.app.sunshine.utils.Utility;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailForecastFragment extends Fragment {

    protected MyApplication app;

    private static final String LOG_TAG = DetailForecastFragment.class.getSimpleName();

    private Context mContext;

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecast;
    private ShareActionProvider mShareActionProvider;

    public DetailForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        app = (MyApplication) mContext.getApplicationContext();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);

        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail_forecast, container, false);

        TextView mTxtDetailForecast = (TextView) rootView.findViewById(R.id.txt_detail_forecast);

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            int position = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
            //            mForecast = intent.getStringExtra(Intent.EXTRA_TEXT);


//            WeatherRealm weather = DataManager.findWeatherAt(mContext, position);
            WeatherRealm weather = DataManager.findWeatherAt(position);
            mForecast = Utility.getReadableDateString(weather.getDt()) + " - " + weather.getWeather().get(0).getMain()
                    + " - " + Utility.formatHighLows(weather.getTemp().getMax(), weather.getTemp().getMin());

            mTxtDetailForecast.setText(mForecast);
        }

        return rootView;
    }
}