package com.lgfischer.widgethost;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * This activity serves as an example of how to search, add and remove widgets
 * from a window.
 *
 * @author Leonardo Garcia Fischer (http://coderender.blogspot.com/)
 */
public class WidgetHostExampleActivity extends Activity {

	static final String TAG = "WidgetHostExampleActivity";

	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;

	ViewGroup mainlayout;
	ViewGroup layout1;
	ViewGroup layout2;
	ViewGroup layout3;

	/*
		Use of Global Variable:
		http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents

		For situations where you know the activities are running in the same process, you can just
		share data through globals. For example, you could have a global HashMap<String,
		WeakReference<MyInterpreterState>> and when you make a new MyInterpreterState come up with
		a unique name for it and put it in the hash map; to send that state to another activity,
		simply put the unique name into the hash map and when the second activity is started it can
		retrieve the MyInterpreterState from the hash map with the name it receives.
	 */
	ViewGroup mClickedView;


	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	/**
	 * Called on the creation of the activity.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mainlayout = (ViewGroup) findViewById(R.id.main_layout);

		layout1 = (ViewGroup) findViewById(R.id.layout1);
		layout1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Toast.makeText(WidgetHostExampleActivity.this, "layout1 clicked", Toast.LENGTH_SHORT).show();
				selectWidget(layout1);
			}
		});

		layout2 = (ViewGroup) findViewById(R.id.layout2);
		layout2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Toast.makeText(WidgetHostExampleActivity.this, "layout2 clicked", Toast.LENGTH_SHORT).show();
				selectWidget(layout2);
			}
		});

		layout3 = (ViewGroup) findViewById(R.id.layout3);
		layout3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Toast.makeText(WidgetHostExampleActivity.this, "layout3 clicked", Toast.LENGTH_SHORT).show();
				selectWidget(layout3);
			}
		});

		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	/**
	 * Launches the menu to select the widget. The selected widget will be on
	 * the result of the activity.
	 */
	void selectWidget(ViewGroup view) {
		mClickedView = view;
		int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
		Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
		pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
	}

	/**
	 * If the user has selected an widget, the result will be in the 'data' when
	 * this function is called.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
				configureWidget(data);
			} else if (requestCode == R.id.REQUEST_CREATE_APPWIDGET) {
				createWidget(data);
			}
		} else if (resultCode == RESULT_CANCELED && data != null) {
			int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
			if (appWidgetId != -1) {
				mAppWidgetHost.deleteAppWidgetId(appWidgetId);
			}
		}
	}

	/**
	 * Checks if the widget needs any configuration. If it needs, launches the
	 * configuration activity.
	 */
	private void configureWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
		if (appWidgetInfo.configure != null) {
			Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
			intent.setComponent(appWidgetInfo.configure);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			startActivityForResult(intent, R.id.REQUEST_CREATE_APPWIDGET);
		} else {
			createWidget(data);
		}
	}

	/**
	 * Creates the widget and adds to our view layout.
	 */
	public void createWidget(Intent data) {
		Bundle extras = data.getExtras();
		int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

		AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
		hostView.setAppWidget(appWidgetId, appWidgetInfo);
		//mainlayout.addView(hostView);

		mClickedView.addView(hostView);
//		layout3.setPadding(0, 0, 0, 0);


		Log.i(TAG, "The widget size is: " + appWidgetInfo.minWidth + "*" + appWidgetInfo.minHeight);
	}

	/**
	 * Registers the AppWidgetHost to listen for updates to any widgets this app
	 * has.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		mAppWidgetHost.startListening();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"WidgetHostExample Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.lgfischer.widgethost/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	/**
	 * Stop listen for updates for our widgets (saving battery).
	 */
	@Override
	protected void onStop() {
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"WidgetHostExample Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.lgfischer.widgethost/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		mAppWidgetHost.stopListening();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	/**
	 * Removes the widget displayed by this AppWidgetHostView.
	 */
	public void removeWidget(AppWidgetHostView hostView) {
		mAppWidgetHost.deleteAppWidgetId(hostView.getAppWidgetId());
		mainlayout.removeView(hostView);
	}

	/**
	 * Handles the menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "Menu selected is: " + item.getTitle() + "/" + item.getItemId() + "/" + R.id.addWidget);
		switch (item.getItemId()) {
			case R.id.addWidget:
				selectWidget(mainlayout);
				return true;
			case R.id.removeWidget:
				removeWidgetMenuSelected();
				return false;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Handle the 'Remove Widget' menu.
	 */
	public void removeWidgetMenuSelected() {
		int childCount = mainlayout.getChildCount();
		if (childCount > 1) {
			View view = mainlayout.getChildAt(childCount - 1);
			if (view instanceof AppWidgetHostView) {
				removeWidget((AppWidgetHostView) view);
				Toast.makeText(this, R.string.widget_removed_popup, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Toast.makeText(this, R.string.no_widgets_popup, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Creates the menu with options to add and remove widgets.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.widget_menu, menu);
		return true;
	}

}