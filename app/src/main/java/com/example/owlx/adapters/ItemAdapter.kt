package com.example.owlx.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.owlx.R
import com.example.owlx.models.product.Product
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ItemAdapter(val context: Context, val items: ArrayList<Product>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView
        val ivProductImage: ImageView
        val tvProductName: TextView
        val tvProductPrice: TextView
        val tvProductDistance: TextView

        init {
            cardView = view.findViewById(R.id.product_card_view)
            ivProductImage = view.findViewById(R.id.iv_product_image)
            tvProductName = view.findViewById(R.id.tv_product_name)
            tvProductDistance = view.findViewById(R.id.tv_user_distance)
            tvProductPrice = view.findViewById(R.id.tv_product_price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_custom_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvProductName.text = item.name
        holder.tvProductPrice.text = item.price.toString()
        Picasso.get().load(item.imageUri).into(holder.ivProductImage)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}