package com.neobis.clientneocafe.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesBranch (context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("BranchPrefs", Context.MODE_PRIVATE)

    fun saveSelectedBranchId(branchId: Int) {
        sharedPreferences.edit().putInt("selectedBranchId", branchId).apply()
    }

    fun loadSelectedBranchId(): Int {
        return sharedPreferences.getInt("selectedBranchId", DEFAULT_BRANCH_ID)
    }

    companion object {
        private const val DEFAULT_BRANCH_ID = 0
    }
}