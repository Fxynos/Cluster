package com.vl.cluster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.vl.cluster.domain.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {

}