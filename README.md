[![](https://jitpack.io/v/ReflxctionDev/Regenerato.svg)](https://jitpack.io/#ReflxctionDev/Regenerato)
# Regenerato  
A simple library to simplify arena regeneration for minigame plugins.  
  
# Add to your project
 0. Add `Regenerato` as a soft dependency or a hard dependency to your plugin through your `plugin.yml`.
 ```yml
depend:  
  - Regenerato
 ```
### Maven:

 1. Add this to your `repositories` tag:
```xml
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
```

 2. Add this to your `dependencies` tag:
```xml
	<dependency>
	  <groupId>com.github.ReflxctionDev</groupId>
	    <artifactId>Regenerato</artifactId>
	    <version>1.0-SNAPSHOT</version>
	    <scope>provided</scope>
	</dependency>
```
### Gradle

 1. Add this to your `repositories` closure:
 ```gradle
 	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
2. Add this to your `dependencies` closure:
```gradle
    compile 'com.github.ReflxctionDev:Regenerato:1.0-SNAPSHOT'
```

# Download
Download latest version [here](https://github.com/ReflxctionDev/Regenerato/releases/tag/1.0-SNAPSHOT).