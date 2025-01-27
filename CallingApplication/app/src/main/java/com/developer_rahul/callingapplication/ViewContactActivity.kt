package com.developer_rahul.callingapplication
import android.Manifest
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import com.developer_rahul.callingapplication.databinding.ActivityViewContactBinding

class ViewContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewContactBinding
    private val firebaseHelper = FirebaseHelper()
    private lateinit var contact: Contact

    private val REQUEST_CALL_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contact = intent.getParcelableExtra("contact")!!

        displayContactDetails()
        setupListeners()
    }

    private fun displayContactDetails() {
        binding.etName.setText(contact.name)
        binding.etMobileNo.setText(contact.mobileNo)
        binding.etEmail.setText(contact.email)
        binding.etNickname.setText(contact.nickname)
    }

    private fun setupListeners() {
        binding.btnUpdate.setOnClickListener {
            val updatedContact = contact.copy(
                name = binding.etName.text.toString(),
                mobileNo = binding.etMobileNo.text.toString(),
                email = binding.etEmail.text.toString(),
                nickname = binding.etNickname.text.toString()
            )
            firebaseHelper.updateContact(updatedContact) { success ->
                if (success) {
                    Toast.makeText(this, "Contact updated!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            firebaseHelper.deleteContact(contact.id) { success ->
                if (success) {
                    Toast.makeText(this, "Contact deleted!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to delete contact", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnCall.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PERMISSION
                )
            } else {

                makeCall(contact.mobileNo)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(contact.mobileNo)
            } else {

                Toast.makeText(this, "Permission denied to make calls", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}
