package com.onerainbow.module.seek

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.domain.Artist
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.adapter.SongListDetailAdapter
import com.onerainbow.module.seek.data.Playlists
import com.onerainbow.module.seek.databinding.ActivityPlaylistBinding
import com.onerainbow.module.seek.viewmodel.PlaylistViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.PLAYLIST)
class PlaylistActivity : BaseActivity<ActivityPlaylistBinding>() {
    @Autowired(name = "playlists")
    lateinit var playlists: Playlists

    private val playlistViewModel: PlaylistViewModel by lazy { PlaylistViewModel() }
    private lateinit var songListDetailAdapter: SongListDetailAdapter
    override fun getViewBinding(): ActivityPlaylistBinding =
        ActivityPlaylistBinding.inflate(layoutInflater)


    override fun observeData() {
        playlistViewModel.playListLiveData.observe(this) { result ->
            songListDetailAdapter.submitList(result.songs)
            fun com.onerainbow.module.seek.data.Artist.toModelArtist(): Artist {
                return Artist(
                    id = this.id,
                    name = this.name
                )
            }

            val songs: List<Song> = result.songs.map { songGetPlay ->
                Song(
                    id = songGetPlay.id,
                    name = songGetPlay.name,
                    artists = songGetPlay.ar.map { it.toModelArtist() }, // 直接用原来的 Artist 列表
                    coverUrl = songGetPlay.al.picUrl // 从 al 取封面
                )

            }
            binding.playAllContainer.setOnClickListener {
                if (songs != null) {
                    if (MusicManager.addToPlayerList(songs)) {
                        ToastUtils.makeText("添加成功")
                    } else {
                        ToastUtils.makeText("添加失败")
                    }

                }

            }
        }
    }

    override fun initEvent() {
        songListDetailAdapter = SongListDetailAdapter()
        binding.recycleviewPlaylistSong.adapter = songListDetailAdapter
        binding.recycleviewPlaylistSong.layoutManager = LinearLayoutManager(this)
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

}
