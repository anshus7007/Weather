package com.anshu.weather.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.anshu.weather.R
import com.anshu.weather.others.Mailer
import com.anshu.weather.ui.fragments.SettingsFragment
import com.google.android.material.snackbar.Snackbar


class CustomDialogFragment:DialogFragment() {
    lateinit var cancel: Button
    lateinit var submit: Button
    lateinit var emailOfUser: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val rootView = inflater.inflate(R.layout.custom_dialog_fragment, container, false)
        cancel=rootView.findViewById(R.id.Cancel_Dialog)
        submit=rootView.findViewById(R.id.Submit_Dialog)
        emailOfUser=rootView.findViewById(R.id.emailOfUser)

        cancel.setOnClickListener {
            dismiss()
        }
        submit.setOnClickListener {
            val selectedId = rootView.findViewById<RadioGroup>(R.id.radioGroup).checkedRadioButtonId
            var radio = rootView.findViewById<RadioButton>(selectedId)
            if (radio != null) {
                var msg = radio.text.toString()
                var recipient = emailOfUser.text.toString().trim()
                if(recipient.isNotEmpty()) {
                    val email: String = recipient
                    val subject: String = "Feedback of Weather App"
                    val message: String = msg.toString().trim()
                    val sm = Mailer(activity!!, email, subject, message)
                    sm.execute()
                }
                else
                {
                    Toast.makeText(activity,"Please type your email",Toast.LENGTH_LONG).show()
                }
            } else {
                    Snackbar
                        .make(rootView , "Nothing selected", Snackbar.LENGTH_LONG).show()


            }
        }
        return rootView
    }
}