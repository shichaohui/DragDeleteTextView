# DragDeleteTextView
仿手机QQ消息数拖动删除效果，本效果的实现由[这里](http://www.eoeandroid.com/forum.php?mod=viewthread&tid=909319)的项目修改而来，原项目使用稍显麻烦。

![效果图](https://github.com/shichaohui/DragDeleteTextView/blob/master/app/renderings.gif)
### 用法：
复制[/app/src/main/java/com/sch/dragdelete/DragDeleteTextView.java](https://github.com/shichaohui/DragDeleteTextView/blob/master/app/src/main/java/com/sch/dragdelete/DragDeleteTextView.java)，和[/app/src/main/res/drawable/clean_anim.xml](https://github.com/shichaohui/DragDeleteTextView/blob/master/app/src/main/res/drawable/clean_anim.xml)，以及使用到的图片[/app/src/main/res/mipmap-xhdpi/](https://github.com/shichaohui/DragDeleteTextView/tree/master/app/src/main/res/mipmap-xhdpi)到自己项目中的对应位置。

DragDeleteTextView类的用法与TextView完全一致，获取DragDeleteTextView的实例之后，可以调用`setConnectedColor(int connectedColor)`方法设置拖动View与原位置的连接线的颜色，默认红色。
