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

class BotanickiAdapter(
    private val biljke: List<Biljka>, private val listener: BotanickiEvent,
    private val trefle : TrefleDAO, private val biljkaDAO: BiljkaDAO ) : RecyclerView.Adapter<BotanickiAdapter.BotanickiViewHolder>() {
    inner class BotanickiViewHolder (view: View): RecyclerView.ViewHolder(view),View.OnClickListener {
        val slikaItem: ImageView= view.findViewById(R.id.slikaItem)
        val nazivItem: TextView= view.findViewById(R.id.nazivItem)
        val porodicaItem: TextView=view.findViewById(R.id.porodicaItem)
        val klimatskiTipItem: TextView=view.findViewById(R.id.klimatskiTipItem)
        val zemljisniTipItem: TextView=view.findViewById(R.id.zemljisniTipItem)

        init{
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onBotanickiItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BotanickiViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.botanicki_layout, viewGroup, false)
        return BotanickiViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: BotanickiViewHolder, position: Int) {
        viewHolder.nazivItem.text = biljke[position].naziv
        viewHolder.porodicaItem.text= biljke[position].porodica

        if(biljke[position].klimatskiTipovi.isNotEmpty())
        viewHolder.klimatskiTipItem.text= biljke[position].klimatskiTipovi[0].opis
        else viewHolder.klimatskiTipItem.text=""

        if(biljke[position].zemljisniTipovi.isNotEmpty())
        viewHolder.zemljisniTipItem.text= biljke[position].zemljisniTipovi[0].naziv
        else viewHolder.zemljisniTipItem.text=""

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
                }
                    else {
                        TrefleDAO().getImage(biljke[position])
                    }

            }
            if (bitmap != null) {
                viewHolder.slikaItem.setImageBitmap(bitmap)
            }
        }
    }
    override fun getItemCount() = biljke.size

    interface BotanickiEvent{
        fun onBotanickiItemClick(position: Int)
    }

    fun crop(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val startX = (originalWidth - cropWidth) / 2
        val startY = (originalHeight - cropHeight) / 2
        return Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight)
    }
}