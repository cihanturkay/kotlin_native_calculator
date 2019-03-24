package sample.calculator.android

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import sample.calculator.arithmeticparser.PartialParser
import sample.calculator.arithmeticparser.parseAndCompute

class CalculatorViewModel : ViewModel() {

    private val expression = MutableLiveData<StringBuilder>()
    val result: LiveData<PartialParser.Result<Double, String>> = Transformations.map(expression) { value ->
        parseAndCompute(value.toString())
    }


    fun appendExpression(value: String) {
        expression.value?.let { currentBuilder ->
            currentBuilder.append(value)
            expression.postValue(currentBuilder)
        } ?: run {
            expression.postValue(StringBuilder(value))
        }
    }

    fun removeLast() {
        expression.value?.let { currentBuilder ->
            if (currentBuilder.isNotEmpty()) {
                currentBuilder.deleteCharAt(currentBuilder.length - 1)
                expression.postValue(currentBuilder)
            }
        }
    }

    fun getExpression(): String {
        return expression.value.toString()
    }

    fun subTotal() {
        val result :Double? = parseAndCompute(expression.value.toString()).expression
        result?.let {
            expression.postValue(StringBuilder(result.toString()))
        }

    }

}