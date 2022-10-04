package id.syaafiqi.photo_gallery_app.features.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.syaafiqi.photo_gallery_app.databinding.ItemLoadingBinding
import id.syaafiqi.photo_gallery_app.databinding.ItemPhotoBinding
import id.syaafiqi.photo_gallery_app.extensions.show

class HomeAdapter(
    private var photosList: ArrayList<PhotosModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> LoadMoreViewHolder(
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> PhotosViewHolder(
                ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    inner class PhotosViewHolder(private val view: ItemPhotoBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(data: PhotosModel) {
            with(view) {
                Glide.with(root.context).load(data.imageLink)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(view.ivPhotoThumbnail)
                tvPhotoDescription.text = data.imageDescription
                tvUserName.text = data.userName
            }

        }
    }

    inner class LoadMoreViewHolder(private val view: ItemLoadingBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind() {
            with(view) {
                loadingStateRv.show()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        position == photosList.size && isLoading -> 0
        else -> 1
    }

    override fun getItemCount(): Int = when (isLoading) {
        true -> photosList.size + 1
        else -> photosList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadMoreViewHolder -> holder.bind()
            is PhotosViewHolder -> holder.bind(photosList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(addedItems: ArrayList<PhotosModel>, isLastPage: Boolean = false) {
        isLoading = !isLastPage
        photosList.addAll(addedItems)
        notifyDataSetChanged()
    }
}