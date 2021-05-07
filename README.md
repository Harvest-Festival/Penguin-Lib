![](src/main/resources/assets/penguinlib/logo.png)

Penguin-Lib is the library mod for all of my mods. It's not too exciting!

If you have any questions, feel free to join the [Harvest Festival Discord](https://discord.gg/MRZAyze)

Adding Penguin-Lib to your buildscript
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts piscary files
    url //TODO
  }
}

dependencies {
  // Penguin-Lib
  deobfCompile "uk.joshiejack.piscary:Penguin-Lib:${minecraft_version}-${piscary_version}"
}
```

`${minecraft_version}` & `${penguinlib_version}` can be found //TODO, check the file name of the version you want.