package com.onerainbow.module.musicplayer.model

/**
 * description ： TODO:类的作用
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/18 20:29
 */
data class SongURL(
    val code: Int,
    val `data`: List<Data>
)

data class Data(
    val accompany: Any,
    val auEff: Any,
    val br: Int,
    val canExtend: Boolean,
    val channelLayout: Any,
    val closedGain: Int,
    val closedPeak: Double,
    val code: Int,
    val effectTypes: Any,
    val encodeType: String,
    val expi: Int,
    val fee: Int,
    val flag: Int,
    val freeTimeTrialPrivilege: FreeTimeTrialPrivilege,
    val freeTrialInfo: Any,
    val freeTrialPrivilege: FreeTrialPrivilege,
    val gain: Double,
    val id: Long,//id
    val level: String,
    val levelConfuse: Any,
    val md5: String,
    val message: Any,
    val musicId: String,
    val payed: Int,
    val peak: Int,
    val podcastCtrp: Any,
    val rightSource: Int,
    val size: Int,
    val sr: Int,
    val time: Int,
    val type: String,
    val uf: Any,
    val url: String,//url
    val urlSource: Int
)

data class FreeTimeTrialPrivilege(
    val remainTime: Int,
    val resConsumable: Boolean,
    val type: Int,
    val userConsumable: Boolean
)

data class FreeTrialPrivilege(
    val cannotListenReason: Any,
    val freeLimitTagType: Any,
    val listenType: Any,
    val playReason: Any,
    val resConsumable: Boolean,
    val userConsumable: Boolean
)
