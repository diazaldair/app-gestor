package com.gestorplus.appgestor.core.util

import android.app.Activity
import java.lang.ref.WeakReference

interface ActivityProvider {
    fun attachActivity(activity: Activity)
    fun detachActivity(activity: Activity)
    fun getActivity(): Activity?
}

class ActivityProviderImpl : ActivityProvider {
    private var activityReference: WeakReference<Activity>? = null

    override fun attachActivity(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    override fun detachActivity(activity: Activity) {
        if (activityReference?.get() == activity) {
            activityReference?.clear()
            activityReference = null
        }
    }

    override fun getActivity(): Activity? = activityReference?.get()
}
