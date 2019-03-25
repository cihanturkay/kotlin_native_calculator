package sample.calculator.android

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import sample.calculator.arithmeticparser.parseAndCompute
import java.text.DecimalFormat
import java.util.concurrent.Executors

class CalculatorViewModel : ViewModel() {

    //TODO Create executors in a dagger module to use it them globally in the app
    private val calculatorExecutor = Executors.newSingleThreadExecutor()
    private val decimalFormatter = DecimalFormat.getNumberInstance()

    val expression = MutableLiveData<StringBuilder>()
    val result: LiveData<String> = Transformations.switchMap(expression) { value ->
        calculate(value.toString())
    }

    private fun calculate(expression: String): LiveData<String> {
        val result = MutableLiveData<String>()
        calculatorExecutor.execute {
            Runnable {
                if (expression.isBlank()) {
                    result.postValue("")
                } else {
                    val partialResult = parseAndCompute(expression)
                    partialResult.expression?.let {
                        result.postValue(decimalFormatter.format(it))
                    }
                }
            }.run()
        }
        return result
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

    fun subTotal() {
        val result: Double? = parseAndCompute(expression.value.toString()).expression
        result?.let {
            expression.postValue(StringBuilder(result.toString()))
        }

    }

}