<img style="float: left" width=64px src="https://harvestfestivalwiki.com/images/f/fd/Penguin-Lib.png">
<br>

 [![Discord](https://img.shields.io/discord/227497118498029569?style=plastic&colorB=7289DA&logo=discord&logoColor=white)](http://discord.gg/0vVjLvWg5kyQwnHG) &nbsp; ![GitHub](https://img.shields.io/github/license/joshiejack/Penguin-Lib?color=%23990000&style=plastic) &nbsp; ![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.joshiejack.uk%2Fjob%2FPenguin-Lib%2F&style=plastic) &nbsp; ![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.joshiejack.uk%2Fuk%2Fjoshiejack%2Fpenguinlib%2FPenguin-Lib%2Fmaven-metadata.xml&style=plastic) &nbsp; [![Curseforge](http://cf.way2muchnoise.eu/full_penguin-lib_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/penguin-lib)


<br><br><br>
Penguin-Lib is the library mod for all of my mods. It's not too exciting!

Adding Penguin-Lib to your buildscript
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    url 'https://maven.joshiejack.uk/'
  }
}

dependencies {
    compile fg.deobf("uk.joshiejack.penguinlib:Penguin-Lib:${minecraft_version}-${penguinlib_version}")
}
```

`${$penguinlib_version}` can be found [here](https://maven.joshiejack.uk/uk/joshiejack/penguinlib/Penguin-Lib/)