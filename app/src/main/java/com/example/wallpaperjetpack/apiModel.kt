package com.example.wallpaperjetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class apiModel {

    @Composable
    fun getStuff(): String{
        val context = LocalContext.current
        val queue = Volley.newRequestQueue(context)
        val url = "http://www.google.com"

        val returnVal = remember {mutableStateOf("")}

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                returnVal.value = "Response is: ${response.substring(0, 500)}"
            },
            Response.ErrorListener {error -> returnVal.value = "That didn't work! $error" })

        // Add the request to the RequestQueue.
        queue.add(stringRequest)

        return returnVal.value;

    }
}