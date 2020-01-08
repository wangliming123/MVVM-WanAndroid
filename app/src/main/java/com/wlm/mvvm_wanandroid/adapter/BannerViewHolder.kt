package com.wlm.mvvm_wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wlm.mvvm_wanandroid.R
import com.wlm.mvvm_wanandroid.common.BannerData
import com.wlm.mvvm_wanandroid.startKtxActivity
import com.wlm.mvvm_wanandroid.ui.activity.BrowserActivity
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader

class BannerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.layout_banner, parent, false)) {

    private val banner = itemView.findViewById<Banner>(R.id.banner)

    private var bannerDatas: List<BannerData>? = null
    private val bannerImages = mutableListOf<String>()
    private val bannerTitles = mutableListOf<String>()
    private val bannerUrls = mutableListOf<String>()

    fun bindBanner(banners: List<BannerData>?) {
        bannerDatas = banners
        bannerImages.clear()
        bannerTitles.clear()
        bannerUrls.clear()
        banners?.forEach {
            it.run {
                bannerImages.add(imagePath)
                bannerTitles.add(title)
                bannerUrls.add(url)
            }
        }
        banner.run {
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setImageLoader(GlideImageLoader())
            setImages(bannerImages)
            setBannerTitles(bannerTitles)
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            setDelayTime(3000)
            setOnBannerListener{ position ->
                context.startKtxActivity<BrowserActivity>(value = BrowserActivity.KEY_URL to bannerUrls[position])
            }
            start()
        }
    }

    class GlideImageLoader : ImageLoader(){
        override fun displayImage(context: Context, path: Any?, imageView: ImageView) {
            Glide.with(context).load(path).into(imageView)
        }

    }
}


