package com.developer_rahul.callingapplication


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer_rahul.callingapplication.databinding.ItemContactBinding
class ContactAdapter(private val onClick: (Contact) -> Unit) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private val originalList = mutableListOf<Contact>()
    private val filteredList = mutableListOf<Contact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = filteredList[position]
        holder.bind(contact)
    }

    override fun getItemCount(): Int = filteredList.size

    fun submitList(contacts: List<Contact>) {
        originalList.clear()
        originalList.addAll(contacts)
        filteredList.clear()
        filteredList.addAll(contacts)
        notifyDataSetChanged()
    }

    fun filter(query: String?): List<Contact> {
        val searchText = query?.lowercase()?.trim()
        filteredList.clear()

        if (searchText.isNullOrEmpty()) {
            filteredList.addAll(originalList)
        } else {
            filteredList.addAll(originalList.filter {
                it.name.lowercase().contains(searchText) || it.mobileNo.contains(searchText)
            })
        }
        notifyDataSetChanged()
        return filteredList
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.tvName.text = contact.name
            binding.tvPhoneNumber.text = contact.mobileNo

            binding.root.setOnClickListener {
                onClick(contact)
            }
        }
    }
}
