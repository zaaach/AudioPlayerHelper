[![Platform](https://img.shields.io/badge/platform-android-green.svg)](https://developer.android.google.cn) [![Licence](https://img.shields.io/badge/Licence-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19) [![jitpack](https://jitpack.io/v/zaaach/AudioPlayerHelper.svg)](https://jitpack.io/#zaaach/AudioPlayerHelper)

# AudioPlayerHelper

> 基于`MediaPlayer`的音频播放器简单封装了一下，一行代码就能播！
>
> 还可以关联`SeekBar`，不用再处理拖动事件了，只需要在进度回调里更新就行:smile:

# Preview
![gif]()

# Install

**Step 1：** 项目根目录的build.gradle添加如下配置：

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2：** app添加依赖：

```groovy
dependencies {
	 implementation 'com.github.zaaach:AudioPlayerHelper:x.y'
}
```

记得把`x.y`替换为[![jitpack](https://jitpack.io/v/zaaach/AudioPlayerHelper.svg)](https://jitpack.io/#zaaach/AudioPlayerHelper)中的数字

# How to use

1、播放

```java
playerHelper = new AudioPlayerHelper(context)
	.attachSeekBar(seekBar)//关联SeekBar
    .setLooping(true)
    .setDebug(true)
	.setOnAudioPlayStateChangeListener(this)//播放器回调
    .setDataSource("http://....");
playerHelper.playOrPause();
```

2、播放器回调

```java
@Override
public void onPreparing(MediaPlayer player) {
    
}

@Override
public void onPrepared(MediaPlayer player, long duration) {
}

@Override
public void onPlaying(MediaPlayer player) {
}

@Override
public void onProgress(MediaPlayer player, @Nullable SeekBar seekBar, boolean isDragging, long position, long duration) {
}

@Override
public void onPlayPaused(MediaPlayer player) {
}

@Override
public void onPlayStop(MediaPlayer player) {
}

@Override
public void onPlayComplete(MediaPlayer player) {
}

@Override
public void onPlayError(String msg) {
}
```

3、生命周期

```java
@Override
protected void onPause() {
    super.onPause();
    playerHelper.pause();//暂停
}

@Override
protected void onResume() {
    super.onResume();
    playerHelper.restore();//恢复播放
}

@Override
protected void onDestroy() {
    super.onDestroy();
    playerHelper.destroy();//释放资源
}
```

:wink:Good luck！！！

# About me

掘金：[ https://juejin.im/user/56f3dfe8efa6310055ac719f ](https://juejin.im/user/56f3dfe8efa6310055ac719f)

简书：[ https://www.jianshu.com/u/913a8bb93d12 ](https://www.jianshu.com/u/913a8bb93d12)

淘宝店：[ LEON家居生活馆 （动漫摆件）]( https://shop238932691.taobao.com)

![LEON](https://raw.githubusercontent.com/zaaach/imgbed/master/arts/leon_shop_qrcode.png)

:wink:淘宝店求个关注:wink:

# License

```
Copyright (c) 2019 zaaach

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```