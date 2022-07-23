# Legend
--------
![logo][2]
--------
## Projects are out of date, please move to:
> [Whale Hook](https://github.com/asLody/whale)



## What is Legend?

Legend is a **Hook framework** for **Android Development**, as it allows you to Hook Java methods **without** ROOT. Legend supports both **Dalvik and Art** environment!

<https://github.com/asLody/legend>

### 1. The Advantages
- Program more **efficiently**
- Dynamic debugging
- **HotFix** and doesn't need to reboot the app
- Fast **dump** Dex File in the shell
- Software security penetration
- Does exciting things...


### 2. How to use Legend
Example 1: **Annotation** type Hook
```java
@Hook("android.widget.Toast::show")
public static void Toast_show(Toast thiz) {
  thiz.setText("XXXXXXXXX");
  //Call the origin method
  HookManager.getDefault().callSuper(thiz);
}
```
Example 2: **Interception** of startActivity
```java
@Hook("android.app.Activity::startActivity@android.content.Intent")
public static void Activity_startActivity(Activity thiz, Intent intent) {
  if (!ALLOW_LAUNCH_ACTIVITY) {
    Toast.makeText(thiz, "I am sorry to turn your Activity down :)", Toast.LENGTH_SHORT).show();
  } else {
    HookManager.getDefault().callSuper(thiz, intent);
  }
}
```
#### Notice:
- Write the following code down in where you want your hooks **enabled**.

```java
HookManager.getDefault().applyHooks(YourClass.class);
```
- You can also hook a method **without annotation**.

```java
HookManager.getDefault().hookMethod(originMethod, hookMethod);
```

### 3. Software Compatibility
- [x] Dalvik & Android 4.2
- [x] Dalvik & Android 4.3
- [x] Art & Android 5.0
- [x] Art & Android 5.0.1
- [x] Art & Android 5.1
- [x] Art & Android 6.0
- [x] Art & Android 6.0.1
- [ ] aliyunOS VM

### 4. Showcase
<https://github.com/dodola/RocooFix>

### 5. Help improve Legend
```java
if (Country.China == your.country) {
  QQGroup.join(530497973);
} else {
  webView.loadUrl("https://github.com/asLody/legend/issues");
}
```
### 6. Author

> [Lody][3]

[2]: https://raw.githubusercontent.com/asLody/legend/master/art/legend_logo.png
[3]: https://github.com/asLody
