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

class KuharskiAdapter(
    private val biljke: List<Biljka>,private val listener: KuharskiEvent,
    private val trefle : TrefleDAO, private val biljkaDAO: BiljkaDAO) : RecyclerView.Adapter<KuharskiAdapter.KuharskiViewHolder>() {
   inner class KuharskiViewHolder (view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
        val slikaItem: ImageView= view.findViewById(R.id.slikaItem)
        val nazivItem: TextView= view.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView=view.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView=view.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView=view.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView=view.findViewById(R.id.jelo3Item)

        init{
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onKuharskiItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): KuharskiViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.kuharski_layout, viewGroup, false)
        return KuharskiViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: KuharskiViewHolder, position: Int) {
        viewHolder.nazivItem.text = biljke[position].naziv
        viewHolder.profilOkusaItem.text= biljke[position].profilOkusa?.opis

        val broj: Int=biljke[position].jela.size

        if(broj>=1)
        viewHolder.jelo1Item.text= biljke[position].jela[0]
        else viewHolder.jelo1Item.text=""

        if(broj>=2)
        viewHolder.jelo2Item.text= biljke[position].jela[1]
        else viewHolder.jelo2Item.text=""

        if(broj>=3)
        viewHolder.jelo3Item.text= biljke[position].jela[2]
        else viewHolder.jelo3Item.text=""

        /*val naziv: String = biljke[position].naziv
        val context: Context = viewHolder.slikaItem.context
        var id: Int = context.resources.getIdentifier(naziv, "drawable", context.packageName)
        if (id==0) id=context.resources.getIdentifier("default1", "drawable", context.packageName)
        viewHolder.slikaItem.setImageResource(id)*/CoroutineScope(Dispatchers.Main).launch {
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

    interface KuharskiEvent {
        fun onKuharskiItemClick(position: Int)
    }

    fun crop(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val startX = (originalWidth - cropWidth) / 2
        val startY = (originalHeight - cropHeight) / 2
        return Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight)
    }
}