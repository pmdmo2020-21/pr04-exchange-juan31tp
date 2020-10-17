package es.iessaladillo.pedrojoya.exchange

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaults()
        setUpViews()
    }

    private fun setDefaults() {
        binding.radioFromDollar.isChecked=true
        binding.imgFromCoin.setImageResource(R.drawable.ic_dollar)
        binding.radioToEuro.isChecked=true
        binding.imgToCoin.setImageResource(R.drawable.ic_euro)
    }

    private fun setUpViews() {
        binding.txtAmount.selectAll()
        binding.rgdFromCurrency.setOnCheckedChangeListener { side, checkId -> currencyPointer(side, checkId) }
        binding.rgdToCurrency.setOnCheckedChangeListener { side, checkId -> currencyPointer(side, checkId) }

        binding.btnExchange.setOnClickListener { exchange() }
        binding.txtAmount.setOnEditorActionListener(TextView.OnEditorActionListener{_,_,_ -> exchange()
            true})

        binding.txtAmount.setOnEditorActionListener { k, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !binding.txtAmount.text.endsWith(".")) {
                exchange()
                hideSoftKeyboard(binding.txtAmount)
                true
            } else {
                true
            }
        }

    }

    private fun hideSoftKeyboard(txtAmount: EditText): Boolean {
        val k = txtAmount.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return k.hideSoftInputFromWindow(txtAmount.windowToken, 0)
    }


    private fun currencyPointer(side: RadioGroup, checkedId: Int) {
        when (checkedId) {
            binding.radioFromDollar.id -> {
                updateIcon(binding.imgFromCoin, Currency.DOLLAR)
                checkEnabledCurrencies(dollar = false, euro = true, pound = true, currency = side)
                binding.radioFromDollar.isChecked=false
            }
            binding.radioFromEuro.id -> {
                updateIcon(binding.imgFromCoin, Currency.EURO)
                checkEnabledCurrencies(dollar = true, euro = false, pound = true, currency = side)
                binding.radioToEuro.isChecked=false
            }
            binding.radioFromPound.id -> {
                updateIcon(binding.imgFromCoin, Currency.POUND)
                checkEnabledCurrencies(dollar = true, euro = true, pound = false, currency = side)
                binding.radioToPound.isChecked=false
            }
            binding.radioToDollar.id -> {
                updateIcon(binding.imgFromCoin, Currency.DOLLAR)
                checkEnabledCurrencies(dollar = false, euro = true, pound = true, currency = side)
                binding.radioFromDollar.isChecked=false
            }
            binding.radioToEuro.id -> {
                updateIcon(binding.imgFromCoin, Currency.EURO)
                checkEnabledCurrencies(dollar = true, euro = false, pound = true, currency = side)
                binding.radioFromEuro.isChecked=false
            }
            binding.radioToPound.id -> {
                updateIcon(binding.imgFromCoin, Currency.POUND)
                checkEnabledCurrencies(dollar = true, euro = true, pound = false, currency = side)
                binding.radioFromPound.isChecked=false
            }
        }
    }

    private fun checkEnabledCurrencies(dollar: Boolean, euro: Boolean, pound: Boolean, currency: RadioGroup) {
        if (currency !== binding.rgdFromCurrency) {
            binding.radioFromDollar.isEnabled= dollar
            binding.radioFromEuro.isEnabled= euro
            binding.radioFromPound.isEnabled= pound
        } else {
            binding.radioToDollar.isEnabled= dollar
            binding.radioToEuro.isEnabled= euro
            binding.radioToPound.isEnabled= pound
        }
    }

    private fun updateIcon(imgFromCoin: ImageView, currency: Currency) {
        imgFromCoin.setImageResource(currency.drawableResId)
    }

    private fun exchange() {

        var amount = binding.txtAmount.text.toString().toDouble()
        var result = 0.00
        lateinit var currencyFrom: Currency
        lateinit var currencyTo: Currency

        when (binding.rgdFromCurrency.checkedRadioButtonId){
            binding.radioFromDollar.id -> {
                currencyFrom=Currency.DOLLAR
                when (binding.rgdToCurrency.checkedRadioButtonId) {
                    binding.radioToEuro.id -> {
                        result=Currency.DOLLAR.fromDollar(amount)
                        currencyTo=Currency.EURO
                    }
                    binding.radioToPound.id -> {
                        result=Currency.DOLLAR.fromDollar(amount)
                        currencyTo=Currency.POUND
                    }
                }
            }
            binding.radioFromEuro.id -> {
                currencyFrom=Currency.EURO
                when (binding.rgdToCurrency.checkedRadioButtonId) {
                    binding.radioToDollar.id -> {
                        currencyTo=Currency.DOLLAR
                        result=Currency.EURO.toDollar(amount)
                    }
                    binding.radioToPound.id -> {
                        currencyTo=Currency.POUND
                        result=Currency.EURO.toDollar(amount)
                        result=Currency.POUND.fromDollar(amount)
                    }
                }
            }
            binding.radioFromPound.id -> {
                currencyFrom=Currency.POUND
                when (binding.rgdToCurrency.checkedRadioButtonId) {
                    binding.radioToDollar.id -> {
                        currencyTo=Currency.DOLLAR
                        result=Currency.DOLLAR.toDollar(amount)
                    }
                    binding.radioToEuro.id -> {
                        currencyTo=Currency.POUND
                        result=Currency.POUND.toDollar(amount)
                        result=Currency.EURO.fromDollar(amount)
                    }
                }
            }
        }
        showExchange(amount, result, currencyFrom, currencyTo);
    }

    private fun showExchange(amount: Double, result: Double, currencyFrom: Currency, currencyTo: Currency) {
        Toast.makeText(this, getString(R.string.result, amount, currencyFrom, result, currencyTo), Toast.LENGTH_SHORT).show();
    }

}