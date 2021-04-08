package com.evangelidis.movieramanew

import android.util.Log
import timber.log.Timber

/**
 * [Log.INFO] logs will be logged to AppCenter in release builds. Do not log any responses/sensitive data using this method.
 */
inline fun infoLog(message: () -> String) = Timber.i(message())

inline fun verboseLog(message: () -> String) = Timber.v(message())
inline fun debugLog(throwable: Throwable? = null, message: () -> String) = Timber.d(throwable, message())
inline fun warnLog(throwable: Throwable? = null, message: () -> String) = Timber.w(throwable, message())
inline fun errorLog(throwable: Throwable? = null, message: () -> String) = Timber.e(throwable, message())
