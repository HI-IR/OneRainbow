package com.onerainbow.module.seek

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.base.BaseActivity
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.adapter.SongListDetailAdapter
import com.onerainbow.module.seek.adapter.SongerAdapter
import com.onerainbow.module.seek.data.Playlists
import com.onerainbow.module.seek.databinding.ActivityPlaylistBinding
import com.onerainbow.module.seek.viewmodel.PlaylistViewModel
import com.therouter.router.Autowired
import com.therouter.router.Route

@Route(path = RoutePath.PLAYLIST)
class PlaylistActivity : BaseActivity<ActivityPlaylistBinding>() {
    @Autowired(name = "playlists")
    lateinit var playlists: Playlists

    @Autowired(name = "id")
    var id: Long? = null
    private val playlistViewModel: PlaylistViewModel by lazy { PlaylistViewModel() }
    private lateinit var songListDetailAdapter: SongListDetailAdapter
    private lateinit var songerAdapter: SongerAdapter
    override fun getViewBinding(): ActivityPlaylistBinding =
        ActivityPlaylistBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initViewModel() {
        if (this::playlists.isInitialized) {
            playlistViewModel.getPlaylistData(playlists.id)
        }
        if (id != null && !this::playlists.isInitialized) {
            playlistViewModel.getSongerData(id!!)
            playlistViewModel.getSongsData(id!!)

        }
    }

    override fun initEvent() {
        if (this::playlists.isInitialized) {
            songListDetailAdapter = SongListDetailAdapter()
            binding.recycleviewPlaylistSong.adapter = songListDetailAdapter
            binding.recycleviewPlaylistSong.layoutManager = LinearLayoutManager(this)
            binding.tvPlaylistTitle.text = playlists.name
            Glide.with(binding.playlistImg.context)
                .load(playlists.coverImgUrl)
                .transform(RoundedCorners(20))
                .into(binding.playlistImg)
            Glide.with(binding.playlistAuthorImg.context)
                .load(playlists.creator.avatarUrl)
                .circleCrop()
                .into(binding.playlistAuthorImg)
            binding.tvDescription.text = playlists.description
            binding.tvPlaylistAuthor.text = playlists.creator.nickname
            playlistViewModel.playListLiveData.observe(this) { result ->
                songListDetailAdapter.submitList(result.songs)
                fun com.onerainbow.module.seek.data.Artist.toModelArtist(): com.onerainbow.module.musicplayer.model.Artist {
                    return com.onerainbow.module.musicplayer.model.Artist(
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
                        if (MusicManager.getPlaylist().isEmpty()) {
                            MusicManager.play(songs)
                        } else {
                            MusicManager.addSongs(songs)
                        }
                    }

                }

            }
        }
        if (id != null && !this::playlists.isInitialized) {
            songerAdapter = SongerAdapter()
            binding.recycleviewPlaylistSong.adapter = songerAdapter
            binding.recycleviewPlaylistSong.layoutManager = LinearLayoutManager(this)
            playlistViewModel.songerDataLiveData.observe(this) { result ->
                binding.tvPlaylistTitle.text = result.artist.name
                binding.tvPlaylistAuthor.text = result.artist.name
                binding.tvDescription.text = result.artist.briefDesc
                Glide.with(binding.playlistImg.context)
                    .load(result.artist.img1v1Url)
                    .transform(RoundedCorners(20))
                    .into(binding.playlistImg)
                Glide.with(binding.playlistAuthorImg.context)
                    .load(result.artist.img1v1Url)
                    .circleCrop()
                    .into(binding.playlistAuthorImg)

            }
            playlistViewModel.songsDataLiveData.observe(this) { result ->
                songerAdapter.submitList(result.songs)
                fun com.onerainbow.module.seek.data.Artist.toModelArtist(): com.onerainbow.module.musicplayer.model.Artist {
                    return com.onerainbow.module.musicplayer.model.Artist(
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
                        if (MusicManager.getPlaylist().isEmpty()) {
                            MusicManager.play(songs)
                        } else {
                            MusicManager.addSongs(songs)
                        }
                    }

                }
            }
        }
    }

}