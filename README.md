[![](https://jitpack.io/v/ReflxctionDev/Regenerato.svg)](https://jitpack.io/#ReflxctionDev/Regenerato)
# Regenerato  
A simple library to simplify arena regeneration for minigame plugins.  

# Prerequisites
* WorldEdit or FastAsyncWorldEdit

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
	    <version>1.1-SNAPSHOT</version>
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
    compile 'com.github.ReflxctionDev:Regenerato:1.1-SNAPSHOT'
```

# Download
Download latest version [here](https://github.com/ReflxctionDev/Regenerato/releases/tag/1.1-SNAPSHOT).

# Example usage
## Accessing Regenerato instance
```java

    /**
     * Regenerato instance
     */
    private Regenerato regenerato;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {
        regenerato = (Regenerato) Bukkit.getPluginManager().getPlugin("Regenerato");
    }

    /**
     * Returns the Regenerato instance
     *
     * @return The Regenerato instance
     */
    public Regenerato getRegenerato() {
        return regenerato;
    }
```

## Save a schematic
```java
private TestPlugin plugin;  
  
public SaveCommand(TestPlugin plugin) {  
  this.plugin = plugin;  
}  
  
@Override  
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {  
  if (!(sender instanceof Player)) {  
    sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");  
    return true;  
  }  
  try {  
     Player player = (Player) sender;  
     SchematicProcessor processor = SchematicProcessor.newSchematicProcessor(plugin.getRegenerato().getWorldEdit(), sender.getName(), plugin.getDataFolder());  // Create a schematic file with the player name
     processor.write(player); // Write into the schematic file
    sender.sendMessage(ChatColor.GREEN + "Schematic saved successfully.");  
  } catch (EmptyClipboardException e) {  
    // Player has no clipboard
    sender.sendMessage(ChatColor.RED + "No WorldEdit clipboard found.");  
  }  
  return true;  
}
```

##  Pasting a schematic
```java
private TestPlugin plugin;  
  
public PasteCommand(TestPlugin plugin) {  
  this.plugin = plugin;  
}  
  
@Override  
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {  
  if (!(sender instanceof Player)) {  
    sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");  
    return true;  
  }  
  try {  
    Player player = (Player) sender;  
    SchematicProcessor processor = SchematicProcessor.newSchematicProcessor(plugin.getRegenerato().getWorldEdit(), sender.getName(), plugin.getDataFolder());  // Load the schematic that has the player name
    processor.paste(player.getLocation());  // Paste the schematic wherever the player is standing
    sender.sendMessage(ChatColor.GREEN + "Schematic has been pasted.");  
  } catch (NoSchematicException e) {  
    sender.sendMessage(ChatColor.RED + "No such schematic exists!"); // No schematic with the specified name exists
  }  
  return true;  
}
```
