package com.singularitycoder.instanote.imageselection.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.singularitycoder.instanote.databinding.ListItemImageBinding
import javax.inject.Inject

class ImageSelectionAdapter @Inject constructor() : RecyclerView.Adapter<ImageSelectionAdapter.ImageViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
    private val imageListDiffer = AsyncListDiffer(this, diffUtil)
    private var onItemClickListener: ((imageUrl: String) -> Unit)? = null
    var imageUrlList: List<String>
        get() = imageListDiffer.currentList
        set(value) = imageListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        holder.setData(imageUrl)
    }

    override fun getItemCount(): Int = imageUrlList.size

    override fun getItemViewType(position: Int): Int = position

    fun setOnImageClickListener(listener: (imageUrl: String) -> Unit) {
        onItemClickListener = listener
    }

    inner class ImageViewHolder(private val itemBinding: ListItemImageBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun setData(imageUrl: String) {
            itemBinding.ivImage.load(imageUrl)
            itemBinding.root.setOnClickListener {
                onItemClickListener?.let { listener: (imageUrl: String) -> Unit -> listener.invoke(imageUrl) }
            }
        }
    }
}