OverScrollListView ([Play Store Demo][1])
===========

An Android List View implementation with overscroll support.

* [Library][3]
* [Sample][4]

Setup
-----
* In Eclipse, just import the library as an Android library project. Project > Clean to generate the binaries 
you need, like R.java, etc.


API support
------------------------------------------------
OverScrollListView works fine on Android 2.3+

How to Integrate this Library into Your Projects
------------------------------------------------
Write in your layout.xml file something like this:

```xml
   <ru.suvitruf.overscrolllistview.OverscrollListView
        android:id="@+id/activity_oslv_list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff"
        android:cacheColorHint="@android:color/transparent"
        android:choiceMode="singleChoice"
        android:divider="@android:color/transparent"
        android:dividerHeight="0dp"
        android:fadingEdgeLength="0dp"
        android:overScrollMode="always"
        android:requiresFadingEdge="vertical"
        oslv:maxOverScrollDistance="100dp"
        oslv:animationTime="800"
         />
```
* `animationTime` - duration of collapse animation time.
* `maxOverScrollDistance` - max distance of overscroll.
* `slowEffect` - you can set it `true` for enable slow effect, it seems like you pulling List View.
* `slowCoefficient` - coefficient of slow effect. Deffault `SLOW_COEFFICIENT = 0.8F`.

Set params programmatically
------------------------------------------------
You also can set params for List View in code:
* `setCollapseAnimationDuration(int duration)`
* `setSlowEffect(boolean slowEffect)`
* `setSlowCoefficient(int coef)`

Developed By
------------
* Andrey Apanasik ([suvitruf.ru][2])


License
-------

    Copyright 2014 Andrey Apanasik
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 
[1]: https://play.google.com/store/apps/details?id=ru.suvitruf.overscrolllistview
[2]: http://suvitruf.ru/
[3]: https://github.com/Suvitruf/Android-sdk-examples/tree/master/OverScrollListView/OverScrollListView
[4]: https://github.com/Suvitruf/Android-sdk-examples/tree/master/OverScrollListView/OverscrollListViewSample
