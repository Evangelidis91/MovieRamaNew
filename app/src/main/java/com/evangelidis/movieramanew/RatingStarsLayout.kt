package com.evangelidis.movieramanew

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.evangelidis.movieramanew.databinding.ItemRatingStarBinding
import kotlin.math.round

class RatingStarsLayout : LinearLayout {

    companion object {
        const val RATING_BAR_LAST_POSITION = 5
        const val HALF_STAR_VALUE = 0.5
        const val QUARTER_STAR_VALUE = 0.25
        const val THREE_QUARTERS_STAR_VALUE = 0.75
        const val EMPTY_STAR_VALUE = 0.0
        const val HELPER_NUMBER_TO_ROUND_RATING = 4
    }

    private var currentRow: LinearLayout?

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        currentRow = createLayout()
    }

    private fun createLayout() : LinearLayout = LinearLayout(context).also { addView(it) }

    fun setRatings(rating: Double, starsSize: Int){
        currentRow?.removeAllViews()

        var roundedRatingNumber = round(rating * HELPER_NUMBER_TO_ROUND_RATING) / HELPER_NUMBER_TO_ROUND_RATING

        repeat(RATING_BAR_LAST_POSITION) {
            val item = ItemRatingStarBinding.inflate(LayoutInflater.from(context))
            item.ratingBarIcon.layoutParams = LayoutParams(starsSize, starsSize)

            if (roundedRatingNumber >= 1) {
                item.ratingBarIcon.setImageResource(R.drawable.icn_star_full)
                roundedRatingNumber--
            } else {
                when (roundedRatingNumber) {
                    HALF_STAR_VALUE -> item.ratingBarIcon.setImageResource(R.drawable.icn_star_half)
                    QUARTER_STAR_VALUE -> item.ratingBarIcon.setImageResource(R.drawable.icn_star_quarter)
                    THREE_QUARTERS_STAR_VALUE -> item.ratingBarIcon.setImageResource(R.drawable.icn_star_3_quarters)
                    EMPTY_STAR_VALUE -> item.ratingBarIcon.setImageResource(R.drawable.icn_star_empty)
                }
                roundedRatingNumber = EMPTY_STAR_VALUE
            }

            currentRow?.addView(item.root)
        }
    }

    fun clearLayout() {
        currentRow?.removeAllViews()
        currentRow = createLayout()
    }
}