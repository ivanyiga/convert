package com.ivanyiga.convert.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ivanyiga.convert.R
import com.ivanyiga.convert.viewmodels.MainViewModelFactory
import com.ivanyiga.convert.viewmodels.mainViewModel
import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler

import android.view.View
import android.widget.*

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivanyiga.convert.models.currenciesModel
import com.ivanyiga.convert.views.adapters.currencyAdapter
import android.text.Editable

import android.text.TextWatcher
import android.util.Log
import java.util.*


@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: mainViewModel
    var country = arrayOf("USD", "UGX", "EUR")
    private var currency_Adapter: currencyAdapter? = null
    private val currencyList: MutableList<currenciesModel> = mutableListOf()
    lateinit var amtInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val application = requireNotNull(this).application
        val factory = MainViewModelFactory()
//        viewModel = ViewModelProvider.of(this,factory).get(mainViewModel::class.java)
        viewModel = ViewModelProvider(this, factory).get(mainViewModel::class.java)
        amtInput = findViewById(R.id.amt)
        amtInput.setText("1")

        observeCurrencies()
        //Get exchange rates from the server
        viewModel.getCurrencies(this)
        viewModel.exchangeList.clear()

        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            viewModel.getCryptoCurrencies(this, this)
        }

        //FETCH RATES FROM THE SERVER: This is intentionally limited to 4 due to the free plan of the currency
        //Exchange provider (COMMENT THIS LINE OUT IF YOU WISH TO FETCH THE WHOLE LIST OF 20 EXCHANGE RATES, GO TO THE ADAPTER AND UNCOMMENT LINE 43)

                fetchRates()

        viewModel.getCurrencyRates(this)


        init()


    }

    fun fetchRates(){
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
        if (isConnected) {
            viewModel.getUSDExchangeRates(this,"BTC")
            viewModel.getUSDExchangeRates(this,"ETH")
            viewModel.getUSDExchangeRates(this,"USDT")
            viewModel.getUSDExchangeRates(this,"BNB")
//            viewModel.getUSDExchangeRates(this,"SOL")
            viewModel.doneRateFetch.observe(this, { done ->
                if (done){
                    currency_Adapter?.notifyDataSetChanged()
                    viewModel.doneRateFetch.value = false
                }
            })
        }
        Handler().postDelayed({
            fetchRates()
        }, 60000)
    }

    fun init(){
        //Currency Spinner
        val spin = findViewById<View>(R.id.spinner) as Spinner
        spin.onItemSelectedListener = this
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = aa

        //Initialize Currency Recyncler & Adapter
        val currencyRecycler = findViewById<RecyclerView>(R.id.currencyRecycler) as RecyclerView
        currency_Adapter = currencyAdapter(viewModel,currencyList,this)
        currencyRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        currencyRecycler.adapter = currency_Adapter

        //Listen to amount change
        amtInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    viewModel.amount = s.toString()
                } else {
                    viewModel.amount = "1.0"
                }
                currency_Adapter?.notifyDataSetChanged()
            }
        })

    }

    fun observeCurrencies(){
        viewModel.currencyLiveData.observe(this, { currencies ->
            val loader = findViewById<ProgressBar>(R.id.loader)
            if (currencies.size>0){
//                Log.d("jOBbb","${currencies[0].quote}")
                loader.visibility = View.GONE
                currencyList.clear()
                currencyList.addAll(currencies)
                currency_Adapter?.notifyDataSetChanged()
            }else{
                loader.visibility = View.VISIBLE
            }
        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Log.d("jOBbb","${p2}")
        if (p2 == 0){
            viewModel.selectedSymbol = "USD"
        } else if (p2 == 1){
            viewModel.selectedSymbol = "UGX"
        } else if (p2 == 2){
            viewModel.selectedSymbol = "EUR"
        }
        currency_Adapter?.notifyDataSetChanged()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}