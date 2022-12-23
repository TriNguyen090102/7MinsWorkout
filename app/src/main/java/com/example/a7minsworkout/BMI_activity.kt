package com.example.a7minsworkout

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minsworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMI_activity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var curVisibleView : String = METRIC_UNITS_VIEW

    private var binding : ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = "BMI"
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }
        makeVisibleMetricUnitsView()
        binding?.btnCalculateUnits?.setOnClickListener {
            if(validateMetricUnits() && curVisibleView == METRIC_UNITS_VIEW)
            {
                val heightValue: Float = binding?.etHeight?.text.toString().toFloat() / 100

                // The weight value is converted to a float value
                val weightValue: Float = binding?.etWeight?.text.toString().toFloat()

                // BMI value is calculated in METRIC UNITS using the height and weight value.
                val bmi = weightValue / (heightValue * heightValue)

                displayBMIResult(bmi)
            }
            else if(validateUsUnits() && curVisibleView == US_UNITS_VIEW)
            {
                var heightUsValue : Float
                var weighUstValue: Float
                var bmi :Float
                if(binding?.etUsMetricUnitHeightFeet?.text!!.isEmpty())
                {
                    heightUsValue = binding?.etUsMetricUnitHeightInch?.text.toString().toFloat()
                    weighUstValue = binding?.etUsMetricUnitWeight?.text.toString().toFloat()
                    val inchToMeter = heightUsValue*0.0254f
                    bmi = weighUstValue/(inchToMeter*inchToMeter)
                    displayBMIResult(bmi)

                } else if(binding?.etUsMetricUnitHeightInch?.text!!.isEmpty())
                {
                    heightUsValue = binding?.etUsMetricUnitHeightFeet?.text.toString().toFloat()
                    weighUstValue = binding?.etUsMetricUnitWeight?.text.toString().toFloat()
                    val feetToMeter : Float = heightUsValue*0.3048f
                    bmi = weighUstValue/(feetToMeter*feetToMeter)
                    displayBMIResult(bmi)
                }
            }
            else {
                Toast.makeText(this@BMI_activity, "Please enter valid values.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rbMetricUnits) makeVisibleMetricUnitsView()
            else makeVisibleUSUnitsView()
        }
    }



    private fun makeVisibleMetricUnitsView() {
        curVisibleView = METRIC_UNITS_VIEW
        binding?.tilHeight?.visibility = View.VISIBLE
        binding?.tilWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.INVISIBLE

        binding?.etUsMetricUnitWeight?.text?.clear()
        binding?.etUsMetricUnitHeightInch?.text?.clear()
        binding?.etUsMetricUnitHeightFeet?.text?.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUSUnitsView() {
        curVisibleView = US_UNITS_VIEW
        binding?.tilHeight?.visibility = View.INVISIBLE
        binding?.tilWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE

        binding?.etHeight?.text?.clear()
        binding?.etWeight?.text?.clear()

        binding?.llDiplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding?.llDiplayBMIResult?.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBMIValue?.text = bmiValue // Value is set to TextView
        binding?.tvBMIType?.text = bmiLabel // Label is set to TextView
        binding?.tvBMIDescription?.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean
    {
        var isValid = true
        if(binding?.etHeight?.text!!.isEmpty()) isValid = false
        if(binding?.etWeight?.text!!.isEmpty()) isValid = false
        return isValid
    }
    private fun validateUsUnits(): Boolean {
        var isValid = true
        if(binding?.etUsMetricUnitWeight?.text!!.isEmpty()) isValid = false
        if(binding?.etUsMetricUnitHeightFeet?.text!!.isEmpty() && binding?.etUsMetricUnitHeightInch?.text!!.isEmpty()) isValid = false
        if(!binding?.etUsMetricUnitHeightFeet?.text!!.isEmpty() && !binding?.etUsMetricUnitHeightInch?.text!!.isEmpty()) isValid = false
        return isValid
    }
}