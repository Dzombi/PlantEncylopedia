package com.example.spirala

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicinskiAdapter(
    private val biljke: List<Biljka>, 
    private val listener: MedicinskiEvent, private val trefle : TrefleDAO, private val biljkaDAO : BiljkaDAO
) : RecyclerView.Adapter<MedicinskiAdapter.MedicinskiViewHolder>() {
    inner class MedicinskiViewHolder (view: View): RecyclerView.ViewHolder(view),View.OnClickListener {
        val slikaItem: ImageView= view.findViewById(R.id.slikaItem)
        val nazivItem: TextView= view.findViewById(R.id.nazivItem)
        val upozorenjeItem: TextView=view.findViewById(R.id.upozorenjeItem)
        val korist1Item: TextView=view.findViewById(R.id.korist1Item)
        val korist2Item: TextView=view.findViewById(R.id.korist2Item)
        val korist3Item: TextView=view.findViewById(R.id.korist3Item)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onMedicinskiItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MedicinskiViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.medicinski_layout, viewGroup, false)
        return MedicinskiViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: MedicinskiViewHolder, position: Int) {
        viewHolder.nazivItem.text = biljke[position].naziv
        if(biljke[position].medicinskoUpozorenje.isNotEmpty())
        viewHolder.upozorenjeItem.text=biljke[position].medicinskoUpozorenje
        else viewHolder.upozorenjeItem.text=""

        val broj: Int = biljke[position].medicinskeKoristi.size

        if(broj>=1)
        viewHolder.korist1Item.text= biljke[position].medicinskeKoristi[0].opis
        else viewHolder.korist1Item.text=""

        if(broj>=2)
        viewHolder.korist2Item.text= biljke[position].medicinskeKoristi[1].opis
        else viewHolder.korist2Item.text=""

        if(broj>=3)
        viewHolder.korist3Item.text= biljke[position].medicinskeKoristi[2].opis
        else viewHolder.korist3Item.text=""

        /*val naziv: String = biljke[position].naziv
        val context: Context = viewHolder.slikaItem.context
        var id: Int = context.resources.getIdentifier(naziv, "drawable", context.packageName)
        if (id==0) id=context.resources.getIdentifier("default1", "drawable", context.packageName)
        viewHolder.slikaItem.setImageResource(id)*/
        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = withContext(Dispatchers.IO) {
                val biljkaId = biljkaDAO.getAllBiljkas().find { it.naziv == biljke[position].naziv }?.id
                if (biljkaId != null) {
                    val biljkaBitmap = biljkaDAO.getBitmapById(biljkaId)
                    if (biljkaBitmap != null) {
                        biljkaBitmap.bitmap
                    } else {
                        val fetchedBitmap = trefle.getImage(biljke[position])
                        fetchedBitmap?.let {
                            val cropWidth = 100
                            val cropHeight = 100
                            val croppedBitmap = crop(it, cropWidth, cropHeight)
                            biljkaDAO.addImage(idBiljke = biljkaId, croppedBitmap)
                            croppedBitmap
                        }
                    }
                } else {
                    null
                }
            }
            if (bitmap != null) {
                viewHolder.slikaItem.setImageBitmap(bitmap)
            }
        }

    }
    override fun getItemCount() = biljke.size

    interface MedicinskiEvent {
        fun onMedicinskiItemClick(position: Int)
    }

    fun crop(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val startX = (originalWidth - cropWidth) / 2
        val startY = (originalHeight - cropHeight) / 2
        return Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight)
    }


}