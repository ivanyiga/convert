package com.ivanyiga.convert.viewmodels

import androidx.lifecycle.ViewModel
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ivanyiga.convert.models.currenciesModel
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

import com.ivanyiga.convert.DBHandler
import org.json.JSONArray
import org.json.JSONException
import java.io.UnsupportedEncodingException


class mainViewModel: ViewModel() {

    var currencyLiveData = MutableLiveData<MutableList<currenciesModel>>()
    var newcurrencylist = mutableListOf<currenciesModel>()
    var exchangeList = arrayListOf<JSONObject>()
    private var dbHandler: DBHandler? = null
    var doneRateFetch = MutableLiveData<Boolean>()

    var selectedSymbol = "USD"
    var amount = "1.0"

    /// Increase the number of desired currencies (Max 20)
    var currLimit = 20

    //GET CURRENCY EXCHANGE FOR USD
    fun getUSDExchangeRates(activity: Context, symbol:String){

        val queue = Volley.newRequestQueue(activity)

        val requestUrl = "https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=1&symbol=USD&convert=$symbol"
        val apiKey = "60bbbb20-0afc-4bfc-affc-65e601d1d9d3"
//        val apiKey = "b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c"

        val jsonObjReq = object: JsonObjectRequest(
            Request.Method.GET,
            requestUrl, null,
            Response.Listener { response ->
//                Log.d("jOBbbb", "$response")
                val rate = JSONObject()
                rate.put("from", "USD")
                rate.put("symbol", symbol)
                rate.put("rate", response.getJSONObject("data").getJSONObject("quote").getJSONObject(symbol)["price"])
//                Log.d("jOBbbb", "$rate")
                exchangeList.add(rate)
                getEURExchangeRates(activity,symbol)
            },
            Response.ErrorListener { e ->
                try {
                    val responseBody =
                        String(e.networkResponse.data, charset("utf-8"))
                    val jsonObject = JSONObject(responseBody)
//                    err.text = jsonObject.getString("error")
                    Log.e("jOB",jsonObject.toString())

                } catch (e: JSONException) { //Handle a malformed json response
//                    Log.e("mobilesErr",e.toString())
                } catch (error: UnsupportedEncodingException) {
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json")
//                headers.put("Content-Type", "application/json")
                headers.put("X-CMC_PRO_API_KEY", "$apiKey")

                return headers
            }
        }
        jsonObjReq.setRetryPolicy(
            DefaultRetryPolicy(
                60000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
//        App.getInstance().addToRequestQueue(jsonObjReq, "paymentRequest")
        queue.add(jsonObjReq)
    }

    //GET CURRENCY EXCHANGE FOR EUR
    fun getEURExchangeRates(activity: Context, symbol:String){

        val queue = Volley.newRequestQueue(activity)

        val requestUrl = "https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=1&symbol=EUR&convert=$symbol"
        val apiKey = "60bbbb20-0afc-4bfc-affc-65e601d1d9d3"
//        val apiKey = "b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c"

        val jsonObjReq = object: JsonObjectRequest(
            Request.Method.GET,
            requestUrl, null,
            Response.Listener { response ->
//                Log.d("jOBbbb", "$response")
                val rate = JSONObject()
                rate.put("from", "EUR")
                rate.put("symbol", symbol)
                rate.put("rate", response.getJSONObject("data").getJSONObject("quote").getJSONObject(symbol)["price"])
//                Log.d("jOBbbb", "$rate")
                exchangeList.add(rate)
                getUGXExchangeRates(activity, symbol)
            },
            Response.ErrorListener { e ->
                try {
                    val responseBody =
                        String(e.networkResponse.data, charset("utf-8"))
                    val jsonObject = JSONObject(responseBody)
//                    err.text = jsonObject.getString("error")
                    Log.e("jOB",jsonObject.toString())

                } catch (e: JSONException) { //Handle a malformed json response
//                    Log.e("mobilesErr",e.toString())
                } catch (error: UnsupportedEncodingException) {
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json")
//                headers.put("Content-Type", "application/json")
                headers.put("X-CMC_PRO_API_KEY", "$apiKey")

                return headers
            }
        }
        jsonObjReq.setRetryPolicy(
            DefaultRetryPolicy(
                60000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
//        App.getInstance().addToRequestQueue(jsonObjReq, "paymentRequest")
        queue.add(jsonObjReq)
    }

    //GET CURRENCY EXCHANGE FOR UGX
    fun getUGXExchangeRates(activity: Context, symbol:String){

        val queue = Volley.newRequestQueue(activity)

        val requestUrl = "https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=1&symbol=UGX&convert=$symbol"
        val apiKey = "60bbbb20-0afc-4bfc-affc-65e601d1d9d3"
//        val apiKey = "b54bcf4d-1bca-4e8e-9a24-22ff2c3d462c"


        val jsonBody = JSONObject()
        jsonBody.put("amount",1)
        jsonBody.put("id","1")
        val jsonObjReq = object: JsonObjectRequest(
            Request.Method.GET,
            requestUrl, null,
            Response.Listener { response ->
                Log.d("jOBbbb", "$response")
                val rate = JSONObject()
                rate.put("from", "UGX")
                rate.put("symbol", symbol)
                rate.put("rate", response.getJSONObject("data").getJSONObject("quote").getJSONObject(symbol)["price"])
                exchangeList.add(rate)
//                Log.d("jOBbbb", "$exchangeList")
                storeRates(exchangeList.toString(),activity)
                getCurrencyRates(activity)
                doneRateFetch.value = true
            },
            Response.ErrorListener { e ->
                try {
                    val responseBody =
                        String(e.networkResponse.data, charset("utf-8"))
                    val jsonObject = JSONObject(responseBody)
//                    err.text = jsonObject.getString("error")
                    Log.e("jOB",jsonObject.toString())

                } catch (e: JSONException) { //Handle a malformed json response
//                    Log.e("mobilesErr",e.toString())
                } catch (error: UnsupportedEncodingException) {
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json")
//                headers.put("Content-Type", "application/json")
                headers.put("X-CMC_PRO_API_KEY", "$apiKey")

                return headers
            }
        }
        jsonObjReq.setRetryPolicy(
            DefaultRetryPolicy(
                60000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
//        App.getInstance().addToRequestQueue(jsonObjReq, "paymentRequest")
        queue.add(jsonObjReq)
    }

    //GET CURRENCIES FROM THE SERVER
    fun getCryptoCurrencies(activity: Activity, context: Context){

        val queue = Volley.newRequestQueue(activity)

        val requestUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val apiKey = "60bbbb20-0afc-4bfc-affc-65e601d1d9d3"

        val jsonBody = JSONObject()
        jsonBody.put("start","1")
        jsonBody.put("limit",currLimit)
        jsonBody.put("convert","UGX")
        val jsonObjReq = object: JsonObjectRequest(
            Request.Method.GET,
            requestUrl, jsonBody,
            Response.Listener { response ->
//                Log.d("jOB", "${response.getJSONArray("data")}")
                newcurrencylist.clear()
                val reducedCurrencies = JSONArray()
                for (i in 0 until response.getJSONArray("data").length()) {
                    if(i<currLimit) {
                        val item = response.getJSONArray("data").getJSONObject(i)
                        reducedCurrencies.put(item)
                    }
                }
//                currencyLiveData.value = newcurrencylist
                storeCurrencies(reducedCurrencies.toString(),context)
                getCurrencies(context)
            },
            Response.ErrorListener { e ->
                Log.e("jOB", "${e.networkResponse}")
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json")
//                headers.put("Content-Type", "application/json")
                headers.put("X-CMC_PRO_API_KEY", "$apiKey")

                return headers
            }
        }
        jsonObjReq.setRetryPolicy(
            DefaultRetryPolicy(
                60000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        queue.add(jsonObjReq)
    }

    //STORE CURRENCIES TO THE LOCAL DB
    private fun storeCurrencies(currencies: String, context: Context){
        dbHandler =  DBHandler(context)
        dbHandler?.addNewCurrencies(currencies)
    }

    //STORE CURRENCY RATES TO THE LOCAL STORAGE
    private fun storeRates(currencyRates: String, context: Context){
        val sharedPref: SharedPreferences = context.getSharedPreferences("rates", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("currencyRates", currencyRates)
        editor.apply()
    }

    //GET CURRENCY RATES FROM LOCAL STORAGE
    fun getCurrencyRates(context: Context):JSONArray {
        val sharedPref: SharedPreferences = context.getSharedPreferences("rates", MODE_PRIVATE)
        val defaultValue: String? = null
        val rates = sharedPref.getString("currencyRates", defaultValue)
//        Log.d("jOBB", "$rates")
        return try {
            JSONArray(rates)
        } catch (e: Exception) {
            JSONArray()
        }

    }

    //GET CURRENCIES FROM THE LOCAL DB
    fun getCurrencies(context: Context){
        dbHandler =  DBHandler(context)
        val response = dbHandler?.currencies

//        Log.d("jOBB", "$response")


        try {
            for (i in 0 until JSONArray(response).length()) {
//                if(i<21) {
                    val item = JSONArray(response).getJSONObject(i)
                    val itemModel = currenciesModel(
                        "${item["name"]}",
                        "${item["symbol"]}",
                        "${item["last_updated"]}",
                        item.getJSONObject("quote")
                    )
                    newcurrencylist.add(itemModel)
//                }
            }
            currencyLiveData.value = newcurrencylist
        } catch (e: Exception) {
        }
    }

    //CONVERT CRYPTO CURRENCIES BASING ON THE SELECTION
    fun convertCurrency(currSymbol: String, context: Context): Double {
        var rate = 0.0
        for (i in 0 until getCurrencyRates(context).length()) {
            val item = getCurrencyRates(context).getJSONObject(i)
            if (item.getString("from") == selectedSymbol && item.getString("symbol") == currSymbol) {
                rate = item.getDouble("rate")
            }
        }
        return rate * (amount.toDouble())
    }

}