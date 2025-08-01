## WXY的Redme

#### app简介

这是一个仿写网易云音乐的app，在一定程度上模仿了网易云的风格，值得注意的是这个app的主色调为`淡蓝色`和`淡绿色`，主要体现**简约，清新**，这与网易云红色的主色调区分开。

为了更佳的显示效果，此app大量实现了沉浸式状态栏，以及吸顶效果

该app由我和hhr共同完成

下面是我负责的app内容的展示

#### 我负责的模块以及功能

我主要负责seek(本来是search,由于模块冲突所以改名),mv,playlist,songer,app(这里的app模块会自动跳转home模块，所以这里的app模块指开屏页)模块，前期也负责account和user模块，由于接口问题，导致之前写的account，user模块两次报废，就交给hhr写了。

#### app模块(开屏页)

一个图片加上点缩放动画
![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753521725845.gif)

#### Seek(search)模块

主要使用rv,vp2,MVVM架构以及sharepreferences，实现搜索界面

![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753519364762.gif)

这里使用sharepreferences保存本地搜索，使用vp2实现排行榜功能每一个vp2页都是rv，这里把每一页和每一个排名分别看成一个item，使用两个adapter继承于listadapter，再拿给viewpager2使用，历史搜索以及排行榜上的内容均有点击事件，均可跳转搜索

![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/Screenshot_2025-07-26-16-38-39-10_84258bf26a1e533.jpg)

来到搜索结果界面，可以看到这里有5个结果，每个搜索结果均是一个rv，点击歌词，单曲里面的歌曲，可以添加到播放列表并且点击的歌曲会变红方便显示

![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753520127629.gif)

### playlist模块

其实就是歌单显示，贯穿所有歌单的点击事件，传入Playlist类型值，便可跳转

![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753520453511.gif)

这里也有点击歌曲变红的相关逻辑，并且额外添加了播放全部逻辑

### Songer模块

实现内容和上面playlist相似，不在赘述

![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753520609247.gif)

### Mv模块

Mv模块有两个部分组成搜索的Mv,主界面的Mv，都是rv，这里的Mv通过ExoPlayer视频库组成，并且自定义了组件，实现了全屏模式，以及BottomSheet实现评论自下向上弹出，实现更好的观感体验，以及实现了分享功能。



![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/Screenshot_2025-07-26-15-47-24-63_84258bf26a1e533.jpg)



![image](https://github.com/HI-IR/OneRainbow/blob/master/redme_wxy/mmexport1753521105834.gif)

#### 其他：

之前在account模块分别实现验证码，密码，二维码登录，user模块实现数据转接，由于接口相继失效，于是费案。

##### 细节点展示：

为提供app的显示性，灵动性，增添了少许缩放动画，歌单图片，播放图标，搜索按钮，mv图标加载等等。

#### 体现的技术

* 使用了MVVM架构，使用ViewModel缓存数据
* 大量使用rxjava与retrofit进行网络请求，并且使用rxjava的操作符简化代码并提升了效率
* fragment懒加载利用ViewModelProvider创建ViewModel(因为ViewModelProVider的缓存机制重复创建会返回之前第一次创建时的viewmodel实现共用viewmodel)
* 使用exoplayer库实现视频播放，并且自定义组件
* 使用rv的适配器创建viewpager2(vp2是rv的子类所以可以用父类的adapter)

### 个人感悟

来红岩网校有大半年了，在这里学到了不少真东西，也认识了不少同学。学长学姐的热情让我感受到移动部门的温暖。其实我的学习道路多余转折。从第一节课更不上进度而选择放弃的懊悔，以及在11月中旬选择重新加入的决心。仍然记得那一晚面对面对对象作业的绝望，以及初学安卓无法适应的我，期末考试仍然在赶红岩网校的进度，面对寒假考核的无力，以及某一晚的豁然开朗，期中考核5天任务的艰巨。我多次怀疑过我的能力，赶不上的进度，知识的短浅，稀碎的java/kotlin基础。但最后我仍然支撑到了现在。这大半年过的并不轻松，我并不想其他人一样，学习的道路上仍充满缺口，并没有熟练掌握之前学过的东西，直到现在我仍然觉得我的基础相当薄弱，代码并不属性，平时写代码还是不太规范，面对一些问题时Al依赖度较大，解决问题的能力稍弱，这都是我后面需要加强的点。总的来说，来红岩网校我近几年最正确的的选择之一，正确引导了我的学习道路，让我看到了未来的希望。以后我会更加注重我的基础，多加练习下代码。防止这次啥都忘了了的情况出现。