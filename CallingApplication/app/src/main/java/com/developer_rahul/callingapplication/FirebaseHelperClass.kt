package com.developer_rahul.callingapplication
import com.google.firebase.database.*

class FirebaseHelper {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("contacts")

    fun addContact(contact: Contact, onComplete: (Boolean) -> Unit) {
        var id = databaseReference.push().key ?: return
        contact.id = id
        databaseReference.child(id).setValue(contact).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getAllContacts(onDataChange: (List<Contact>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contacts = snapshot.children.mapNotNull { it.getValue(Contact::class.java) }
                onDataChange(contacts)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun updateContact(contact: Contact, onComplete: (Boolean) -> Unit) {
        databaseReference.child(contact.id.toString()).setValue(contact).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }


    fun deleteContact(contactId: String, onComplete: (Boolean) -> Unit) {
        databaseReference.child(contactId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }
}
