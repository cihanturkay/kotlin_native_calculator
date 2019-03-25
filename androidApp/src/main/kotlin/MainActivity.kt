/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package sample.calculator.android

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import sample.calculator.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var calculatorViewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        calculatorViewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)

        calculatorViewModel.expression.observe(this, Observer { binding.displayFunction.text = it.toString() })
        calculatorViewModel.result.observe(this, Observer { binding.displayResult.text = it })

    }

    fun onButtonClick(view: View) {
        if (view is TextView) {
            calculatorViewModel.appendExpression(view.text as String)
        }
    }

    fun onBackSpace(view: View) {
        calculatorViewModel.removeLast()
    }

    fun onEqualsClicked(view: View) {
        calculatorViewModel.subTotal()
    }

}