package com.job.darasalecturer.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;


/**
 * Created by Job on Friday : 8/3/2018.
 */
public class DoSnack {

    private Context context;
    private Activity activity;

    public DoSnack(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    public void showSnackbar(final int mainTextStringId, final int actionStringId,
                             android.view.View.OnClickListener listener) {
        Snackbar.make(
                activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getString(actionStringId), listener).show();
    }

    public void showSnackbar(final String mainTextStringId, final String actionStringId,
                             View.OnClickListener listener) {
        Snackbar.make(
                activity.findViewById(android.R.id.content),
                mainTextStringId,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionStringId, listener).show();
    }
}
