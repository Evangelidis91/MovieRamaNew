package com.evangelidis.movieramanew


/**
 * Ahsen Saeed}
 * ahsansaeed067@gmail.com}
 * 10/24/19}
 */
open class Event<out T>(private val content: T) {

    var consumed = false
        private set // Allow external read but not write

    /**
     * Consumes the content if it's not been consumed yet.
     * @return The unconsumed content or `null` if it was consumed already.
     */
    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }

}