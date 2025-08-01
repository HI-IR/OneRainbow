package com.onerainbow.module.seek

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.base.utils.UsernameUtils
import com.onerainbow.lib.database.entity.CollectEntity
import com.onerainbow.lib.database.entity.RecentPlayedEntity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.domain.Artist
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.domain.toArtistLite
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.adapter.SongListDetailAdapter
import com.onerainbow.module.seek.data.Al
import com.onerainbow.module.seek.data.Creator
import com.onerainbow.module.seek.data.GetPlaylistData
import com.onerainbow.module.seek.data.Playlists
import com.onerainbow.module.seek.data.SongGetPlay
import com.onerainbow.module.seek.databinding.ActivityPlaylistBinding
import com.onerainbow.module.seek.viewmodel.PlaylistViewModel
import com.therouter.TheRouter
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.PLAYLIST)
class PlaylistActivity : BaseActivity<ActivityPlaylistBinding>() {
    @Autowired(name = "playlists")
    lateinit var playlists: Playlists

    @Autowired(name = "type")
    lateinit var type: String

    private val playlistViewModel: PlaylistViewModel by lazy { PlaylistViewModel() }
    private lateinit var songListDetailAdapter: SongListDetailAdapter
    private var song: List<Song>? = null
    override fun onStart() {
        super.onStart()
        binding.playlistImg.apply {
            scaleX = 0.8f
            scaleY = 0.8f
            alpha = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(500)
                .start()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songListDetailAdapter = SongListDetailAdapter()
        binding.recycleviewPlaylistSong.adapter = songListDetailAdapter
        binding.recycleviewPlaylistSong.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        fun startPlaylistActivity(type: String) {
            TheRouter.build(RoutePath.PLAYLIST)
                .withString("type", type)
                .navigation()
        }
    }

    override fun getViewBinding(): ActivityPlaylistBinding =
        ActivityPlaylistBinding.inflate(layoutInflater)


    override fun observeData() {
        playlistViewModel.playListLiveData.observe(this) { result ->
            songListDetailAdapter.submitList(result.songs)

            song = result.songs.map { it.tosongs() }

        }
        playlistViewModel.apply {
            avatarData.observe(this@PlaylistActivity) { result ->
                Glide.with(binding.playlistAuthorImg.context)
                    .load(result)
                    .circleCrop()
                    .into(binding.playlistAuthorImg)
            }
            recentDataLiveData.observe(this@PlaylistActivity) { result ->
                binding.tvPlaylistAuthor.text = UsernameUtils.getUsername()
                binding.tvPlaylistTitle.text = "最近播放"
                binding.tvDescription.text = "寻找你的时光日记"
                if (result.isNullOrEmpty()) {
                    return@observe ToastUtils.makeText("没有最近播放啊")
                }
                Glide.with(binding.playlistImg.context)
                    .load(result.get(0).coverUrl)
                    .into(binding.playlistImg)
                song = result.map { it.toSongGetPlay() }.map { it.tosongs() }
                songListDetailAdapter.submitList(result.map { it.toSongGetPlay() })

            }
            collectDataLiveData.observe(this@PlaylistActivity) { result ->
                binding.tvPlaylistAuthor.text = UsernameUtils.getUsername()
                binding.tvDescription.text = "那些被时间温柔收录的声音"
                binding.tvPlaylistTitle.text = "我的收藏"
                if (result.isNullOrEmpty()) {
                    return@observe ToastUtils.makeText("这番天地等着你来探索")
                }
                Glide.with(binding.playlistImg.context)
                    .load(result[0].coverUrl)
                    .into(binding.playlistImg)
                song = result.map { it.toSongGetPlay() }.map { it.tosongs() }
                songListDetailAdapter.submitList(result.map { it.toSongGetPlay() })

            }
        }


    }

    override fun initEvent() {
        binding.playAllContainer.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
            if (song != null) {
                if (MusicManager.addToPlayerList(song!!)) {
                    ToastUtils.makeText("添加成功")
                } else {
                    ToastUtils.makeText("添加失败")
                }

            }

        }
        if (this::playlists.isInitialized && !this::type.isInitialized) {
            initPlaylist()
        } else initMyselfPlaylist(type)
        binding.tvBack.setOnClickListener {
            finish()
        }

    }

    private fun initPlaylist() {
        binding.tvPlaylistTitle.text = playlists.name
        binding.tvDescription.text = playlists.description
        binding.tvPlaylistAuthor.text = playlists.creator.nickname
        Glide.with(binding.playlistImg.context)
            .load(playlists.coverImgUrl)
            .transform(RoundedCorners(20))
            .into(binding.playlistImg)
        Glide.with(binding.playlistAuthorImg.context)
            .load(playlists.creator.avatarUrl)
            .circleCrop()
            .into(binding.playlistAuthorImg)
        playlistViewModel.getPlaylistData(playlists.id)
    }

    private fun initMyselfPlaylist(type: String) {
        playlistViewModel.getUserImg()
        when (type) {
            "collect" -> {
                initMyselfCollectPlaylist()
            }

            "recent" -> {
                initMyselfRecentPlaylist()
            }

        }


    }

    private fun initMyselfRecentPlaylist() {
        playlistViewModel.getRecentData()
    }

    private fun initMyselfCollectPlaylist() {
        playlistViewModel.getCollectData()
    }

    fun Artist.toArtist(): com.onerainbow.module.seek.data.Artist {
        return com.onerainbow.module.seek.data.Artist(
            id = this.id,
            name = this.name,
            img1v1Url = " "
        )
    }

    fun com.onerainbow.module.seek.data.Artist.toModelArtist(): Artist {
        return Artist(
            id = this.id,
            name = this.name
        )
    }

    fun CollectEntity.toSongGetPlay(): SongGetPlay {
        val artists: List<Artist> = Gson().fromJson(
            artistsJson,
            object : TypeToken<List<Artist>>() {}.type
        )
        return SongGetPlay(
            id = songId,
            name = songName,
            ar = artists.map { it.toArtist() },
            al = Al(
                id = -1L,         // 没有专辑ID和名称，默认填-1或空串
                name = "",
                picUrl = coverUrl // 用 CollectEntity 中的封面图作为 picUrl
            )
        )
    }

    fun RecentPlayedEntity.toSongGetPlay(): SongGetPlay {
        val artists: List<Artist> = Gson().fromJson(
            artistsJson,
            object : TypeToken<List<Artist>>() {}.type
        )
        return SongGetPlay(
            id = songId,
            name = songName,
            ar = artists.map { it.toArtist() },
            al = Al(
                id = -1L,        // 没有专辑ID信息，用默认值占位
                name = "",
                picUrl = coverUrl
            )
        )
    }

    fun SongGetPlay.tosongs(): Song {
        return Song(
            id = id,
            name = name,
            artists = ar.map { it.toModelArtist() }, // 直接用原来的 Artist 列表
            coverUrl = al.picUrl // 从 al 取封面
        )

    }


}
