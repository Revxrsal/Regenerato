/*
 * * Copyright 2019 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.regenerato.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import io.github.regenerato.Regenerato;
import io.github.regenerato.worldedit.SchematicProcessor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand implements CommandExecutor {

    private final Regenerato plugin;

    public SaveCommand(Regenerato plugin) {
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
            SchematicProcessor processor = SchematicProcessor.newSchematicProcessor(plugin.getWorldEdit(), sender.getName(), plugin.getDataFolder());  // Create a schematic file with the player name
            processor.write(player); // Write into the schematic file
            sender.sendMessage(ChatColor.GREEN + "Schematic saved successfully.");
        } catch (EmptyClipboardException e) {
            // Player has no clipboard
            sender.sendMessage(ChatColor.RED + "No WorldEdit clipboard found.");
        }
        return true;
    }
}
