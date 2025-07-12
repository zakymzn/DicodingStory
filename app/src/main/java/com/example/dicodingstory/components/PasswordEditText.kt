package com.example.dicodingstory.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.dicodingstory.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var isPasswordVisible = false
    private lateinit var lockIcon: Drawable
    private lateinit var visibilityOnIcon: Drawable
    private lateinit var visibilityOffIcon: Drawable

    init {
        hint = ContextCompat.getString(context, R.string.text_hint_password)
        textAlignment = TEXT_ALIGNMENT_VIEW_START
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        compoundDrawablePadding = 40
        background = ContextCompat.getDrawable(context, R.drawable.custom_edit_text_background)

        lockIcon = ContextCompat.getDrawable(context, R.drawable.baseline_lock_24) as Drawable
        visibilityOnIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_24) as Drawable
        visibilityOffIcon = ContextCompat.getDrawable(context, R.drawable.baseline_visibility_off_24) as Drawable

        setButtonDrawables(startOfTheText = lockIcon, endOfTheText = visibilityOffIcon)

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString().length < 8) {
                    setError(context.getString(R.string.text_invalid_password), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null && event != null) {
            val drawableEnd = compoundDrawables[2]
            val drawableWidth = drawableEnd.bounds.width()
            val touchAreaStart = width - paddingEnd - drawableWidth

            if (event.action == MotionEvent.ACTION_UP && event.x >= touchAreaStart) {
                togglePasswordVisibility()
                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        val selection = selectionEnd
        isPasswordVisible = !isPasswordVisible

        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        val icon = if (isPasswordVisible) visibilityOnIcon else visibilityOffIcon
        setButtonDrawables(startOfTheText = lockIcon, endOfTheText = icon)

        setSelection(selection)
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