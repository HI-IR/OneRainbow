package com.onerainbow.lib.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * description ： 用于给RecentPlayed中的歌手数据做类型转换
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/22 19:26
 */
class Converter {
    private val gson = Gson()

    @TypeConverter
    fun fromArtistList(list: List<ArtistLite>) = gson.toJson(list)

    @TypeConverter
    fun toArtistLists(json: String) : List<ArtistLite>{
        val type = object : TypeToken<List<ArtistLite>>() {}.type
        return gson.fromJson(json,type)
    }
}