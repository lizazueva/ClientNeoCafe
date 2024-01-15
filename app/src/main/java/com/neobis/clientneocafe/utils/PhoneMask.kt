package com.neobis.clientneocafe.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.view.KeyEvent.ACTION_DOWN

class PhoneMask(private val editText: EditText) : TextWatcher {
    private var isFormatting: Boolean = false

    companion object {
        private const val MASK_FORMAT = "000 000 0000"
        private const val MASK_CHARACTER = '0'
    }

    init {
        editText.addTextChangedListener(this)
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                handleBackspace()
                true
            } else {
                false
            }
        }
    }

    private fun handleBackspace() {
        val currentText = editText.text
        if (currentText.isNotEmpty()) {
            val selectionStart = editText.selectionStart
            val selectionEnd = editText.selectionEnd

            if (selectionStart == selectionEnd) {
                if (selectionStart > 0) {
                    val editableText = currentText.delete(selectionStart - 1, selectionStart)
                    editText.text = editableText
                    editText.setSelection(selectionStart - 1)
                } else {
                    val editableText = currentText.delete(selectionStart, selectionEnd)
                    editText.text = editableText
                    editText.setSelection(selectionStart)
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return

        isFormatting = true

        val phone = s.toString().replace("[^\\d]".toRegex(), "")
        val formattedPhone = StringBuilder()

        var maskIndex = 0
        var phoneIndex = 0

        while (maskIndex < MASK_FORMAT.length && phoneIndex < phone.length) {
            if (MASK_FORMAT[maskIndex] == MASK_CHARACTER) {
                formattedPhone.append(phone[phoneIndex])
                phoneIndex++
            } else {
                formattedPhone.append(MASK_FORMAT[maskIndex])
            }
            maskIndex++
        }

        while (maskIndex < MASK_FORMAT.length) {
            if (MASK_FORMAT[maskIndex] != MASK_CHARACTER) {
                formattedPhone.append(MASK_FORMAT[maskIndex])
            }
            maskIndex++
        }

        editText.setText(formattedPhone.toString())
        editText.setSelection(formattedPhone.length)
        isFormatting = false
    }
}