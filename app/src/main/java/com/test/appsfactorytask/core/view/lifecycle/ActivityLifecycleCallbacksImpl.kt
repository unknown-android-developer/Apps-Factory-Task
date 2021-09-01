package com.test.appsfactorytask.core.view.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.android.AndroidInjection
import javax.inject.Inject

class ActivityLifecycleCallbacksImpl @Inject constructor() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AndroidInjection.inject(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit
}