package com.lana.cc.device.user.ui.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.lana.cc.device.user.BuildConfig
import com.lana.cc.device.user.R

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.drawable.bg_404)
        .centerCrop()
        .into(imageView)
}

@BindingAdapter("imageUrlWithAddIcon")
fun loadImageWithAddIcon(imageView: ImageView, url: String?) {
    Glide.with(imageView.context).load(url)
        .placeholder(R.drawable.icon_add_pic)
        .centerCrop()
        .into(imageView)
}

@SuppressLint("NewApi")
@BindingAdapter("gender")
fun getGenderDrawable(imageView: ImageView, gender: String?) {
    when (gender) {
        "F" -> {
            Glide.with(imageView.context).load(R.drawable.icon_genderfemale)
                .placeholder(R.drawable.icon_gendermale)
                .dontAnimate()
                .into(imageView)
            imageView.imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        imageView.context,
                        R.color.colorFemale
                    )
                )
        }
        else -> {
            Glide.with(imageView.context).load(R.drawable.icon_gendermale)
                .placeholder(R.drawable.icon_gendermale)
                .dontAnimate()
                .into(imageView)

            imageView.imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        imageView.context,
                        R.color.colorMale
                    )
                )
        }
    }


}

fun getImageFromServer(relativePath: String) = "${BuildConfig.BASE_URL}/image/$relativePath"





