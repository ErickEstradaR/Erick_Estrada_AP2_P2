package com.example.erick_estrada_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import com.example.erick_estrada_ap2_p2.domain.useCases.GastosUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val useCases: GastosUseCases
): ViewModel() {
}
