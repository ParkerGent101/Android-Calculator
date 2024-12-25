package com.example.calculator1

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentInput = ""  // Holds the current input from the user
    private var lastNumber = ""    // Holds the last number entered before an operator
    private var operator = ""      // Holds the current operator (+, -, x, /)
    private var resultDisplayed = false // Flag to check if the result is currently displayed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()  // Set up button listeners for all calculator buttons
    }

    private fun setupListeners() {
        // Set up listeners for number buttons (0-9)
        val numberButtons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6, binding.button7,
            binding.button8, binding.button9
        )
        for (button in numberButtons) {
            button.setOnClickListener { onNumberClick(it) }
        }

        // Set up listeners for operator buttons (+, -, x, /)
        binding.buttonPlus.setOnClickListener { onOperatorClick("+") }
        binding.buttonMinus.setOnClickListener { onOperatorClick("-") }
        binding.buttonMultiply.setOnClickListener { onOperatorClick("x") }
        binding.buttonDivide.setOnClickListener { onOperatorClick("/") }

        // Set up listeners for functionality buttons (equals, dot, clear, backspace, toggle sign)
        binding.buttonEquals.setOnClickListener { calculateResult() }
        binding.buttonDot.setOnClickListener { onDecimalClick() }
        binding.buttonClear.setOnClickListener { clearAll() }
        binding.buttonBackspace.setOnClickListener { onBackspaceClick() }
        binding.buttonToggleSign.setOnClickListener { toggleSign() }
    }

    // Called when a number button is clicked
    private fun onNumberClick(view: View) {
        if (view is Button) {
            if (resultDisplayed) {
                currentInput = ""  // Clear current input if result was displayed
                resultDisplayed = false
            }
            currentInput += view.text  // Append the number to current input
            binding.resultTextView.text = currentInput  // Update the display
        }
    }

    // Called when an operator button is clicked
    private fun onOperatorClick(op: String) {
        if (currentInput.isNotEmpty()) {
            lastNumber = currentInput  // Save the current input as the last number
            operator = op  // Save the chosen operator
            currentInput = ""  // Clear the current input to enter the next number
        }
    }

    // Called when the decimal button is clicked
    private fun onDecimalClick() {
        if (!currentInput.contains(".")) {  // Check if the current input already contains a decimal point
            currentInput += "."  // Append a decimal point
            binding.resultTextView.text = currentInput  // Update the display
        }
    }

    // Called when the backspace button is clicked
    private fun onBackspaceClick() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)  // Remove the last character
            binding.resultTextView.text = currentInput  // Update the display
        }
    }

    // Clears all inputs and results
    private fun clearAll() {
        currentInput = ""
        lastNumber = ""
        operator = ""
        binding.resultTextView.text = ""  // Clear the display
    }

    // Toggles the sign of the current input (positive/negative)
    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)  // Remove the negative sign
            } else {
                "-$currentInput"  // Add the negative sign
            }
            binding.resultTextView.text = currentInput  // Update the display
        }
    }

    // Calculates the result based on the last number, current input, and operator
    private fun calculateResult() {
        if (lastNumber.isNotEmpty() && currentInput.isNotEmpty()) {
            val num1 = lastNumber.toFloatOrNull()  // Convert last number to Float
            val num2 = currentInput.toFloatOrNull()  // Convert current input to Float

            if (num1 != null && num2 != null) {
                val result = when (operator) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "x" -> num1 * num2
                    "/" -> if (num2 != 0f) num1 / num2 else Float.NaN
                    else -> Float.NaN
                }

                // Format result based on its magnitude
                val formattedResult = when {
                    result.isNaN() -> "Error"  // Display error if result is NaN (e.g., division by zero)
                    Math.abs(result) >= 1e6 || Math.abs(result) < 1e-3 -> String.format("%.2e", result)  // Scientific notation
                    else -> String.format("%.2f", result)  // Fixed decimal places
                }

                binding.resultTextView.text = formattedResult  // Display the result
                resultDisplayed = true  // Set flag to true indicating result is displayed
            }
        }
    }
}
