package com.example.caqao

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.caqao.databinding.GridViewItemBinding
import com.example.caqao.network.CacaoDetection

class CacaoGridAdapter (val clickListener: CacaoDetectionListener) : ListAdapter<CacaoDetection,
        CacaoGridAdapter.CacaoDetectionViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacaoGridAdapter.CacaoDetectionViewHolder {
        return CacaoDetectionViewHolder(
            GridViewItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CacaoGridAdapter.CacaoDetectionViewHolder, position: Int) {
        val cacaoDetection = getItem(position)
        holder.bind(getItem(position)!!, clickListener)
    }

    class CacaoDetectionViewHolder(private var binding:
                              GridViewItemBinding
    ):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cacaoDetection: CacaoDetection, clickListener: CacaoDetectionListener) {
            binding.cacaoDetection = cacaoDetection
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<CacaoDetection>() {

        override fun areItemsTheSame(oldItem: CacaoDetection, newItem: CacaoDetection): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CacaoDetection, newItem: CacaoDetection): Boolean {
            return oldItem.imgSrcUrl == newItem.imgSrcUrl
        }

    }

}

class CacaoDetectionListener(val clickListener: (cacaoDetectionId: Int) -> Unit) {
    fun onClick(cacaoDetection: CacaoDetection) = clickListener(cacaoDetection.id)
}
