package es.iessaladillo.pedrojoya.exchange

import androidx.annotation.DrawableRes

enum class Currency(
    val symbol: String,
    @field:DrawableRes val drawableResId: Int,  // Icon
    private val asDollar: Double // The value in dollars of 1 unit of the currency
) {

    DOLLAR("$", R.drawable.ic_dollar, 1.0),
    EURO("€", R.drawable.ic_euro, 1.17),
    POUND("£", R.drawable.ic_pound, 1.31);

    // TODO: Method to convert the currency to dollar.
    // fun toDollar(amount: Double): Double

    // TODO: Method to convert a dollar to the currency
    // fun fromDollar(amount: Double): Double

}