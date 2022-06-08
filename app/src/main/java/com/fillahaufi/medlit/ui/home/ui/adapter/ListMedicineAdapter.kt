package com.fillahaufi.medlit.ui.camera

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fillahaufi.medlit.R
import com.fillahaufi.medlit.data.local.Medicine

class ListMedicineAdapter(private val listMedicine: ArrayList<Medicine>) : RecyclerView.Adapter<ListMedicineAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_medicine, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, photo) = listMedicine[position]
//        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        Glide
            .with(holder.itemView.context)
            .load(photo)
            .into(holder.imgPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listMedicine[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = listMedicine.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.medicine_img)
        var tvName: TextView = itemView.findViewById(R.id.medicine_name)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Medicine)
    }

}