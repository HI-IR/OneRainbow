package com.onerainbow.module.recommend.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.CopyUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.databinding.ActivityWebBinding
import com.onerainbow.module.share.CustomShare
import com.onerainbow.module.share.utils.ShareUtils
import com.therouter.router.Autowired
import com.therouter.router.Route
//TODO 更改路由
@Route(path = RoutePath.WEB)
class WebActivity : BaseActivity<ActivityWebBinding>() {

    @Autowired(name = "url")
    lateinit var mUrl: String

    override fun getViewBinding(): ActivityWebBinding = ActivityWebBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViewModel() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initEvent() {
        binding.webWebview.apply {
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(mUrl)
        }

        binding.apply {
            btnBack.setOnClickListener{
                finish()
            }
            btnShare.setOnClickListener {
                //初始化分享弹窗
                initShare()
            }
        }

    }

    private fun initShare() {
        val customShare = CustomShare(this)
        customShare.setOnQqClickListener{
            ShareUtils.shareToQQ(this,mUrl)
            customShare.dismiss()
        }
        customShare.setOnWxClickListener{
            ShareUtils.shareToWX(this,mUrl)
            customShare.dismiss()
        }
        customShare.setOnBrowseClickListener{
            ShareUtils.shareToBrowser(this,mUrl)
            customShare.dismiss()
        }
        customShare.setOnLinkClickListener(){
            CopyUtils.copy(this ,mUrl)
            Toast.makeText(this, "已复制链接", Toast.LENGTH_SHORT).show()
            customShare.dismiss()
        }

        //显示弹窗
        customShare.show()
    }
}