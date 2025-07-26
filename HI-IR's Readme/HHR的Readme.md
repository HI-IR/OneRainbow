# HHR的Readme

## APP简介

这是我与万夏一共同开发的一款仿网易云音乐风格的音乐软件 ——OneRainbow

## 概览

本次负责模块主要是以下六大模块

home模块：主界面的搭建

recommend模块：推荐页

top模块：热榜页

musicplayer模块：音乐模块

account模块：APP的登录相关

share模块：app的分享功能

## 详细介绍

### home模块

使用vp2+Fragment+BottomNavigation构建了app主页的雏形，因为我的页需要和侧边栏使用同一个ViewModel所以这部分也包含了用户页

主要构成：侧边栏，搜索框，以及播放音乐的playbar，和用户页

直接上图

![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/home.gif)


### recommend模块

home模块支持下的第一个Fragment，用于显示推荐歌单等

![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/recommend.gif)

主要内容就是一个通过Vp2制作的一个Banner和一些RV和Vp2。

同时在这部分中运用外部拦截法解决了一个由于SwipeRefreshLayout和轮播图的一个滑动冲突(当滑动轮播图时，容易触发SwipeRefreshLayout的刷新)



 ### top模块

就是一个Vp2中放了2个Fragment，用于展示一些热门排行榜，和歌手榜

![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/top.gif)

这里使用了NestedScrollView中嵌套RV的方式来解决了嵌套滑动的问题

(虽然在 NestedScrollView 中使用 RecyclerView 时，如果未明确指定 RecyclerView 的高度，会导致其所有 Item 被一次性加载出来。但由于当前场景下的数据量较小，因此这个问题暂时不会造成明显影响，若后续需要进一步优化，可考虑移除嵌套结构，仅使用单个 RecyclerView，并通过重写其 getItemViewType 方法区分不同类型的View，以此实现数据的按需加载，也就是我在期中考核中采用过的方案)



### musicplayer模块

好的来到了坐牢最久的模块

这个模块主要由2部分组成

1. 音乐播放服务
2. 音乐播放器页面

首先关于音乐播放服务，我这里采用的是谷歌的Exoplayer作为播放引擎，然后通过编写`MusicService`来实现最基础的维护音乐列表，加载音乐(因为接口问题，音乐接口的URL会变，所以为了保证歌单列表的歌都可以正常播放，所以每次播放音乐之前都需要重新访问一下拿到最新的URL，就是因为这个害我在这上面写了好久)

因为为了简化使用者的操作，于是又提供出了`MusicManager`用来管理MusicService。同时为了保证UI状态和MusicService的状态一致，这里提提供出了1个接口，通过设计保存多监听器的方式，保证主页playbar和播放器页面的播放状态一致

```kotlin
//用来监听变化的接口
interface PlaybackStateListener {
    fun onPlayStateChanged(isPlaying: Boolean)
    fun onPlayIndexChanged(index: Int)
    fun onPlayError(error: Boolean)//出现错误时回调
    fun onPlayerListChanged(playerList:List<Song>)//歌曲变化后的回调
}

    //多监听器
    private val listenerList = mutableListOf<PlaybackStateListener>()
```

同时后期增加本地登陆系统，需要完成最近播放的内容，于是这里又写了一个RecentPlayHelper，来帮助解决有关最近播放数据库操作的问题，方便解耦MusicService和MusicManager

关于播放器页面，属于是仿写网易云音乐的播放页面，在这个部分中也是尝试使用了值动画的方式来实现动画，效果就看下图吧

![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/musicplayer.gif)

 ### account模块

这个模块主要就是登录页，还有一些关于软件的Room数据库相关的搭建

![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/account.gif)

### share模块

这里其实是把我五一考核的时候写的分享功能直接拿过来

就是一个自定义的Dialog，然后通过Intent来调用系统的分享，可以分享到QQ，WX，浏览器，还有复制链接


![image](https://github.com/HI-IR/OneRainbow/blob/master/HI-IR's%20Readme/share.gif)

## 使用的技术

* 采用MVVM框架，在home模块采用ViewModel使侧边栏(Activity)的数据和用户页的数据共享
* 使用 rxjava 与 retrofit进行网络请求
* 使用Room数据库+协程的方法进行用户数据的管理
* 采用Exoplayer完成音乐播放服务
* TheRouter+多模块



## 不足

实话实说，不足的地方其实有很多

没有很好的整理代码，部分代码有点臃肿，有些代码写得确实很丑陋

对于一些知识的遗忘

and so on

## 个人感悟

一转眼，在红岩学习都过去了一年了，回望一年前的自己，我真切地感受到了这一年里的成长与变化 —— 无论是专业能力的提升、视野见识的拓展，还是面对难题时心态的成熟。遥看一年前的暑假，照着黑马敲下了第一个300行代码的一个命令行版本的通讯录，不过 300 行代码，当时却觉得已是 “庞大工程”，到如今随随便便一个小软件就是成千上万行的代码，也是真切的看见了自己心态的变化，感谢红岩这个我编程的启蒙老师。在安卓的学习时，也是深切的感受到了"路阻且长"，经过不小努力完成了一个小部分的学习之后发现后面还有更大的天地，但是经过这一年的学习也让我感受到了"行则将至"，只要踏实向前，终会抵达更远的地方

最后我想用这一句话来总结我的这一年：路虽远，行则将至；事虽难，做则必成。

