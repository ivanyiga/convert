package com.ivanyiga.convert.views.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ivanyiga.convert.R
import com.ivanyiga.convert.models.currenciesModel
import com.ivanyiga.convert.viewmodels.mainViewModel

class currencyAdapter(val viewModel: mainViewModel, val arrayList: MutableList<currenciesModel>, val context: Context): RecyclerView.Adapter<currencyAdapter.CurrencyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): currencyAdapter.CurrencyViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.currency_item,parent,false)
        return CurrencyViewHolder(root)
    }

    override fun onBindViewHolder(holder: currencyAdapter.CurrencyViewHolder, position: Int) {
        holder.bind(arrayList[position])
        val curr: currenciesModel = arrayList[position]

        if ((position % 2) == 0) {
            holder.item.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }else {
            holder.item.setBackgroundColor(Color.parseColor("#ECECEC"))
        }

        holder.title.text = curr.currName
        holder.currency_symbol.text = curr.currSymbol
        holder.date.text = curr.last_updated
        holder.exchangeRate.text = curr.currSymbol?.let { String.format("%.6f", viewModel.convertCurrency(it,context)) }

        //FETCH CURRENCY EXCHANGE RATES (LIMITED BY FREE ACCOUNT)
        //UNCOMMENT THIS LINE FETCHES THE LIST OF ALL THE EXCHANGE RATES ONE BY ONE (LIMITED BY FREE PLAN)
//        curr.currSymbol?.let { viewModel.getUSDExchangeRates(context, it) }
    }

    override fun getItemCount(): Int {
        if(arrayList.size==0){
//            Toast.makeText(context,"List is empty",Toast.LENGTH_LONG).show()
        }else{

        }
        return arrayList.size
    }


    inner  class CurrencyViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        val title = itemView.findViewById(R.id.currency_title) as TextView
        val currency_symbol = itemView.findViewById(R.id.currency_symbol) as TextView
        val date = itemView.findViewById(R.id.date) as TextView
        val exchangeRate = itemView.findViewById(R.id.exchange_rate) as TextView
        val item = itemView.findViewById(R.id.item) as LinearLayout

        fun bind(currency: currenciesModel){
        }

    }

}