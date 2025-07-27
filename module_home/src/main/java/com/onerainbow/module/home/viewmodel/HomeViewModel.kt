package com.onerainbow.module.home.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onerainbow.lib.base.BaseApplication
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.OneRainbowDatabase
import com.onerainbow.lib.database.entity.CollectEntity
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.module.home.model.HomeModel
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.musicplayer.service.PlaybackStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * description ： 主页框架的ViewModel
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/19 15:36
 */
class HomeViewModel: ViewModel(){

    private val model by lazy {
        HomeModel()
    }

    //最近播放的一首歌的数据
    private val _lastPlay = MutableLiveData<RecentPlayedEntity?>()
    val lastPlay :LiveData<RecentPlayedEntity?> = _lastPlay

    //最近的收藏歌曲数据
    private val _collect = MutableLiveData<CollectEntity?>()
    val collect : LiveData<CollectEntity?> = _collect

    //最近的收藏歌曲数据
    private val _collectCount = MutableLiveData<Long>()
    val collectCount : LiveData<Long> = _collectCount

    //用户歌单数据加载失败的报错
    private val _errorAccount = MutableLiveData<String>()
    val errorAccount : LiveData<String> = _errorAccount

    //头像数据
    private val _avatarData = MutableLiveData<ByteArray?>()
    val avatarData: LiveData<ByteArray?>  = _avatarData

    //用户名数据
    private val _usernameData = MutableLiveData<String>()
    val usernameData: LiveData<String>  = _usernameData


    //头像数据报错
    private val _errorAvatar = MutableLiveData<String>()
    val errorAvatar : LiveData<String> = _errorAvatar


    //播放状态-error，停止所有控制，交给播放器控制
    private val _error = MutableLiveData<Boolean>(false)
    val error: LiveData<Boolean> = _error

    //播放状态-标志
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    //当前播放的页数
    private val _currentIndex : MutableLiveData<Int> = MutableLiveData(-1)
    val currentIndex : LiveData<Int> = _currentIndex

    // 播放列表
    private val _playlist = MutableLiveData<List<Song>>(emptyList())
    val playlist: LiveData<List<Song>> = _playlist

    private val userDao by lazy {
        OneRainbowDatabase.getDatabase().userDao()
    }

    private val collectDao by lazy {
        OneRainbowDatabase.getDatabase().collectDao()
    }

    private val recentPlayDao by lazy {
        OneRainbowDatabase.getDatabase().recentPlayDao()
    }

    private val playbackListener = object : PlaybackStateListener {
        override fun onPlayStateChanged(isPlaying: Boolean) {
            _isPlaying.postValue(isPlaying)
        }

        override fun onPlayIndexChanged(index: Int) {
            _currentIndex.postValue(index)
        }

        override fun onPlayError(error: Boolean) {
            _error.postValue(error)
        }

        override fun onPlayerListChanged(playerList: List<Song>) {
            _playlist.postValue(playerList)
        }
    }

    //初始化同步数据
    init {
        //注册监听器
        MusicManager.addPlaybackStateListener(playbackListener)

        // 尝试从 Manager 拉取初始状态
        _playlist.value = MusicManager.getPlaylist()
        _currentIndex.value = MusicManager.getCurrentIndex()
        _isPlaying.value = MusicManager.isPlaying()
    }


    //切换播放状态
    fun togglePlayPause(){
        if (error.value!!){
            return
        }
        MusicManager.togglePlayPause()
    }

    fun playAt(index: Int){
        MusicManager.playAt(index)
    }


    //移除监听器，避免内存泄露，状态污染
    override fun onCleared() {
        MusicManager.removePlaybackStateListener(playbackListener)//取消监听器
        super.onCleared()
    }

    fun loadUserAvatar() {
        //本地没有保存用户名，则没登陆，不加载头像
        val username =UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        _usernameData.postValue(username!!)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val base64String = userDao.getAvatarResByUsername(username)
                if (base64String.isNullOrBlank()) return@launch

                val pureBase64 = base64String.substringAfter("base64,", base64String)
                val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
                _avatarData.postValue(decodedBytes)
            } catch (e: Exception) {
                _errorAvatar.postValue(e.message?:"头像加载失败")
            }
        }
    }

    //加载用户数据 如歌单等
    fun loadAccount(){
        //本地没有保存用户名，则没登陆，不加载头像
        val username =UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        viewModelScope.launch{
            //防止一个异常导致所有被取消
            supervisorScope {
                //获取我喜欢的歌曲
                launch {
                    try {
                        val collectSong = collectDao.getLastCollection(username)
                        val collectCount = collectDao.getCollectCount(username)

                        collectSong?.let { _collect.postValue(it) }
                        _collectCount.postValue(collectCount)

                    }catch (e:Exception){
                        _errorAccount.postValue(e.message?:"歌单数据加载失败")
                    }
                }
                //获取最近播放
                launch {
                    try {
                        val lastPlay = recentPlayDao.getLastPlay(username)
                        lastPlay?.let { _lastPlay.postValue(it) }
                    }catch (e:Exception){
                        _errorAccount.postValue(e.message?:"歌单数据加载失败")
                    }
                }
            }
        }
    }


    /**
     * 登出
     */
    fun logout(){
        //本地没有保存用户名，则没登陆，不能登出
        val username = UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        UsernameUtils.clearUsername()
        //清空数据
        //传递一个空字符串，代表已经登出
        _usernameData.postValue("")
        _lastPlay.postValue(null)
        _collectCount.postValue(0)
        _collect.postValue(null)
        _avatarData.postValue(null)
    }

    //压缩图片并转化为base64
    private fun convertToBase64(uri: Uri): String? {
        val maxSize = 1080
        return try {
            val contentResolver = BaseApplication.context.contentResolver

            // 先获取图片尺寸
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }

            // 计算缩放比例
            var scale = 1
            val width = options.outWidth
            val height = options.outHeight
            if (width > maxSize || height > maxSize) {
                scale = Math.max(width / maxSize, height / maxSize)
            }

            // 加载缩放后的Bitmap
            val decodeOptions = BitmapFactory.Options().apply { inSampleSize = scale }
            val bitmap = contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, decodeOptions)
            } ?: return null

            // 压缩成JPEG，质量85%
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)

            // 转Base64，无换行
            Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    fun saveAvatar(uri: Uri) {
        val username =UsernameUtils.getUsername()
        if (username.isNullOrBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            val base64 = convertToBase64(uri)
            if (base64 != null) {
                model.updateAvatarResByUsername(username,base64)
            }
            withContext(Dispatchers.Main){
                loadUserAvatar() // 存入数据库后立即刷新头像
            }
        }

    }

}