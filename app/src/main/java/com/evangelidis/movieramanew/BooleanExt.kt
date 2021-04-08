package com.evangelidis.movierama

/**
 * Wrapper for an on line if else statement that makes use of the elvis operator
 *
 * Warning: this method is more expensive due to it having to execute the value of param
 * in order for it to be passed through
 */
@Deprecated("Prefer lambda version as it doesn't execute `param` unnecessarily", ReplaceWith("then { param }"))
infix fun <T> Boolean.then(param: T): T? = if (this) param else null

infix fun <T> Boolean.then(param: (() -> T)): T? = if (this) param() else null

fun Boolean?.orFalse(): Boolean = this ?: false

fun Boolean?.orTrue(): Boolean = this ?: true