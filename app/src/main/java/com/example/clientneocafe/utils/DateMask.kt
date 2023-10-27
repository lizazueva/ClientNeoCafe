package com.example.clientneocafe.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.view.KeyEvent

class DateMask(private val editText: EditText) : TextWatcher {
    private var isFormatting: Boolean = false

    companion object {
        private const val MASK_FORMAT = "##.##.####"
        private const val MASK_CHARACTER = '#'
    }

    init {
        editText.addTextChangedListener(this)
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
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
                    val previousChar = currentText[selectionStart - 1]
                    if (previousChar == '.') {
                        val editableText = currentText.delete(selectionStart - 2, selectionStart)
                        editText.text = editableText
                        editText.setSelection(selectionStart - 2)
                    } else {
                        val editableText = currentText.delete(selectionStart - 1, selectionStart)
                        editText.text = editableText
                        editText.setSelection(selectionStart - 1)
                    }
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

        val date = s.toString().replace("[^\\d]".toRegex(), "")
        val formattedDate = StringBuilder()

        var maskIndex = 0
        var dateIndex = 0

        while (maskIndex < MASK_FORMAT.length && dateIndex < date.length) {
            if (MASK_FORMAT[maskIndex] == MASK_CHARACTER) {
                formattedDate.append(date[dateIndex])
                dateIndex++
            } else {
                formattedDate.append(MASK_FORMAT[maskIndex])
            }
            maskIndex++
        }

        while (maskIndex < MASK_FORMAT.length) {
            if (MASK_FORMAT[maskIndex] != MASK_CHARACTER) {
                formattedDate.append(MASK_FORMAT[maskIndex])
            }
            maskIndex++
        }

        editText.setText(formattedDate.toString())
        editText.setSelection(formattedDate.length)
        isFormatting = false
    }
}