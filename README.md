# `ANDROID`
## THIẾT KẾ CONTROLS
[![anroid](https://jobs.hybrid-technologies.vn/wp-content/uploads/2020/06/andy-sm.png)](https://www.facebook.com/groups/803166693912534)
Trong bài viết này. mình sẽ thảo luận về một số bản thiết kế giao diện các control được đánh giá là ưu nhìn. Mục tiêu là cung cấp `cách thiết kế` và các `Template` để phục vụ cho việc thiết kế.
### Shape

> công việc A
> công việc B
> công việc C

- Shape là một thuộc tính được định nghĩa trong Android Resource File.
- Shape có 4 dạng:
  - Rectangle: hình chữ nhật
  - Oval: hình bầu dục, giống quả trứng hoặc hình ellipse.
  - Line: là dạng đưởng kẻ.
  - Ring: vành tròn.
- Trong các shape có các thuộc tính như sau:
  - corners: độ bo tròn cho các góc của hình khối. 
  - gradient: cho phép bạn chỉ định tối đa `3` điểm màu làm mốc.
  - padding: canh lề biên.
  - size: độ lớn hình khối.
  - solid: giúp tô màu **"đặc"** cho hình khối.
##### Ring

Ở thẻ này bạn phải thêm một vài thuộc tính khác.
- Dùng độ lớn để định nghĩa đường kình vòng tròn bên trong.
``` sh
android:innerRadius 
```
- Dùng tỉ lệ để định nghĩa đường kính vòng tron bên trong.
``` sh
android:innerRadiusRatio 
```
- Dùng độ lớn để định nghĩa khoảng cách giữa hai dường tròn.
``` sh
android:thickness
```
- Dùng tỉ lệ để định nghĩa khoảng cách giữa hai đường tròn.
``` sh
android:thicknessRatio
```
- True hoặc False cho dạng drawable là LevelListDrawable.
``` sh
android:useLevel 
```
##### Gradient

- Ba điểm màu làm mốc.
``` sh
android:startColor
android:centerColor
android:endColor
```
- Chỉ định kiểu gradient:
  - linear: tuyến tính.
  - radial: vòng ra-đa.
  - sweep: kiểu "dẻ" quạt.
``` sh
android:type
```
- Chỉ định mộtsố nguyên để định nghĩa gốc xoay của dãy màu với `type = linear`.
```sh
android:angle
```
Định nghĩa bán kính của hình tròn ra-đa.
```sh
android:gradientRadius
```
##### Padding và size
Chỉ định độ lớn của bên và độ lớn của hình khối.
##### Solid
Giúp tô màu đặc cho hình khối, khác với gradient.
```sh
android:color
```
##### Stroke
- Chỉ định độ rộng đường biên của hình khối.
```sh
android:width
```
- Chỉ định màu cho đường biên.
```sh
android:color
```
- Định nghĩa đường biên dức nét
  - Khoảng cách giữa hai nét đứt
  - Độ dài của mỗi nét đứt.
```sh
android:dashGap
```
```sh
android:dashWidth
```
##### Một vài ví dụ của Shape XML
- Background bo tròn button
 ```sh
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
 
    <solid android:color="@color/colorAccent" />
 
    <corners android:radius="5dp" />
 
    <stroke
        android:width="2dp"
        android:color="@color/colorPrimary" />
</shape>
```
[![vd1](https://i2.wp.com/yellowcodebooks.com/wp-content/uploads/2017/07/screen-shot-2017-07-18-at-05-36-31.png?resize=300%2C378&ssl=1)]()
- Hình tròn mang phong cách Skype

```sh
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
 
    <gradient
        android:angle="135"
        android:endColor="#FFFF00"
        android:startColor="#FFB000"
        android:type="linear" />
 
    <size
        android:width="40dp"
        android:height="40dp" />
 
</shape>
```
[![circleButton](https://i2.wp.com/yellowcodebooks.com/wp-content/uploads/2017/07/screen-shot-2017-07-18-at-05-49-15.png?resize=300%2C301&ssl=1)]()

- Vành tròn
```sh
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="ring"
    android:innerRadiusRatio="6"
    android:thicknessRatio="4"
    android:useLevel="false">
 
    <gradient
        android:endColor="@color/colorAccent"
        android:startColor="@color/colorPrimary"
        android:gradientRadius="15dp"
        android:centerX="0.5"
        android:centerY="0.5"
        android:type="radial" />
 
    <size
        android:width="40dp"
        android:height="40dp" />
 
</shape>
```
[![lineCircle](https://i0.wp.com/yellowcodebooks.com/wp-content/uploads/2017/07/screen-shot-2017-07-18-at-05-59-52.png?resize=300%2C364&ssl=1)]()
