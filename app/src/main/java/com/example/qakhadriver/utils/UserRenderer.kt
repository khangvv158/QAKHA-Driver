package com.example.qakhadriver.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.qakhadriver.data.model.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator

class UserRenderer(
    private val context: Context,
    map: GoogleMap?,
    clusterManager: ClusterManager<User>?
) :
    DefaultClusterRenderer<User>(context, map, clusterManager) {

    private val iconGenerator: IconGenerator by lazy {
        IconGenerator(context)
    }
    private var imageView = ImageView(context)

    init {
        imageView.apply {
            layoutParams = ViewGroup.LayoutParams(markerWidth, markerHeight)
            setPadding(5)
        }
        iconGenerator.setContentView(imageView)
    }

    override fun onBeforeClusterItemRendered(item: User, markerOptions: MarkerOptions) {
        val icon = iconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterItemRendered(clusterItem: User, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        Glide.with(context).asBitmap().load(clusterItem.image)
            .into(object : CustomTarget<Bitmap>(90, 90) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    imageView.setImageBitmap(resource)
                    val icon = iconGenerator.makeIcon()
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    override fun shouldRenderAsCluster(cluster: Cluster<User>): Boolean {
        return false
    }

    companion object {
        private const val markerWidth = 100
        private const val markerHeight = 100
    }
}
