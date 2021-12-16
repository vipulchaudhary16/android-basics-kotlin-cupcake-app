package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 20.0
private const val PRICE_FOR_SAME_DAY_PICKUP = 30.0

class OrderViewModel : ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private val _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOption = getPickupOption()

    init {
        resetOrder()
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(_date.value == dateOption[0]){
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    fun setQuantity(numberCupcakes: Int) {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun hasNoFlavorSet() : Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOption() : List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d" , Locale.getDefault())
        val calendar = Calendar.getInstance()
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE , 1)
        }
        return options
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        //if customer will choose the same day pickup then there will be 30 extra charge so we have to update the total
        updatePrice()
    }

    private fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOption[0]
        _price.value = 0.0
    }
}