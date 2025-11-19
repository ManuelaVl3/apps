package com.example.app.ui.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.app.utils.RequestResult
import kotlinx.coroutines.delay

@Composable
fun OperationResultHandler(
    requestResult: RequestResult?,
    onSuccess: suspend() -> Unit,
    onFailure: suspend() -> Unit
) {

    when (requestResult) {
        is RequestResult.Loading -> {
            LinearProgressIndicator()
        }
        is RequestResult.Success -> {
            Text(
                text = requestResult.message
            )
        }
        is RequestResult.Failure -> {
            Text(
                text = requestResult.errorMessage
            )
        }
        else -> {}
    }

    LaunchedEffect(requestResult) {
        when (requestResult) {
            is RequestResult.Success -> {
                delay(2000)
                onSuccess()
            }
            is RequestResult.Failure -> {
                delay(2000)
                onFailure()
            }
            else -> {}
        }
    }
}