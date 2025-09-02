package com.example.dicodingstory.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.isValidEmail

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var lockIcon: Drawable

    init {
        hint = ContextCompat.getString(context, R.string.text_hint_email)
        textAlignment = TEXT_ALIGNMENT_VIEW_START
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        compoundDrawablePadding = 40
        background = ContextCompat.getDrawable(context, R.drawable.custom_edit_text_background)
        lockIcon = ContextCompat.getDrawable(context, R.drawable.baseline_email_24) as Drawable
        setButtonDrawables(startOfTheText = lockIcon)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                error = if (!s.isNullOrEmpty() && !isValidEmail(s)) {
                    ContextCompat.getString(context, R.string.text_invalid_email)
                } else if (s.isNullOrEmpty()) {
                    ContextCompat.getString(context, R.string.text_empty_email)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}