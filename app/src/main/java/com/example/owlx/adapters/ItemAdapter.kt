package com.example.owlx.adapters

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.owlx.R
import com.example.owlx.firebaseUtil.getUserFromLoggedUser
import com.example.owlx.firebaseUtil.getUserObjectFromUserId
import com.example.owlx.models.product.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ItemAdapter(private val context: Context, private val items: ArrayList<Product>,
                  private var onItemClicked: (product: Product) -> Unit):
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
        Picasso.get().load(item.imageUri).rotate(90.0F).into(holder.ivProductImage)

        getDistance(item) { distance ->
            val distanceText = if (distance < 100.0) {
                String.format("%.2f mt(s)", distance)
            } else {
                String.format("%.2f km(s)", distance / 1000)
            }

            holder.tvProductDistance.text = distanceText
        }

        holder.cardView.setOnClickListener {
            onItemClicked(item)
        }
    }

    private fun getDistance(product: Product, callback: (distance: Float) -> Unit) {
        val db = Firebase.firestore

        getUserObjectFromUserId(db, product.userId!!) { ownerUser ->
            getUserFromLoggedUser(db) { loggedUser, _ ->
                val buyerLocation = Location("buyer")
                buyerLocation.latitude = loggedUser.coordinates?.latitude!!
                buyerLocation.longitude = loggedUser.coordinates?.longitude!!

                val sellerLocation = Location("seller")
                sellerLocation.latitude = ownerUser.coordinates?.latitude!!
                sellerLocation.longitude = ownerUser.coordinates?.longitude!!

                val distance = buyerLocation.distanceTo(sellerLocation)

                callback(distance)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}