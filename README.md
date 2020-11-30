[![Platform](https://img.shields.io/badge/platform-android-green.svg)](https://developer.android.google.cn) [![Licence](https://img.shields.io/badge/Licence-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19) [![jitpack](https://jitpack.io/v/zaaach/AudioPlayerHelper.svg)](https://jitpack.io/#zaaach/AudioPlayerHelper)

# AudioPlayerHelper

> 音频播放器简单封装了一下，一行代码就能播！

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

```groovy
new AudioPlayerHelper(context)
    .setDebug(true)
    .playOrPause();
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