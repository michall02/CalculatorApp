package com.example.calculatorapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import android.view.ViewGroup
import android.widget.EditText



private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {


    // Variables to hold the operands and type of calculation
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //data buttons
        val listener = View.OnClickListener { view ->
            val but = view as Button
            newNumber.append(but.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        //operations buttons
        val opListener = View.OnClickListener { view ->
            val op = (view as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
        buttonSqr.setOnClickListener(opListener)
        buttonRoot.setOnClickListener(opListener)

        // negative button
        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if(value.isEmpty()){
                newNumber.setText("-")
            }else{
                //value will be multiply by (-1) if there any value,
                //if newNumber equals "-" then we catch NumberFormatException and return ""
                try{
                    var doubleValue = value.toDouble()
                    doubleValue*=-1
                    newNumber.setText(doubleValue.toString())
                }catch (e:NumberFormatException){
                    newNumber.setText("")
                }
            }
        }

        //delete button
        buttonDel.setOnClickListener{
            val value = newNumber.text.toString()
            val res = result.text.toString()

            if(value.isNotEmpty()){
                newNumber.setText("")
            }else if (value.isEmpty() && res.isNotEmpty()){
                newNumber.setText("")
                result.setText("")
                operand1 = null
                pendingOperation = ""
                operation.text = pendingOperation
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (operand1 != null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
        outState.putString(STATE_PENDING_OPERATION, pendingOperation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }

        pendingOperation = savedInstanceState.getString(STATE_PENDING_OPERATION)
        operation.text = pendingOperation
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            if(pendingOperation == "sqr"){
                operand1 = Math.pow(value,2.0)
            }else if (pendingOperation == "root"){
                operand1 = Math.sqrt(value)
            }else{
                operand1 = value
            }
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN   // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
                "sqr" -> operand1 = Math.pow(value,2.0)
                "root" -> operand1 = Math.sqrt(value)
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }
}


