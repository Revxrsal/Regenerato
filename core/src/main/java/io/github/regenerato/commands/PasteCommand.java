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

import io.github.regenerato.Regenerato;
import io.github.regenerato.worldedit.NoSchematicException;
import io.github.regenerato.worldedit.SchematicProcessor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PasteCommand implements CommandExecutor {

    private Regenerato plugin;

    public PasteCommand(Regenerato plugin) {
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
            SchematicProcessor processor = SchematicProcessor.newSchematicProcessor(plugin.getWorldEdit(), sender.getName(), plugin.getDataFolder());  // Load the schematic that has the player name
            processor.paste(player.getLocation());  // Paste the schematic wherever the player is standing
            sender.sendMessage(ChatColor.GREEN + "Schematic has been pasted.");
        } catch (NoSchematicException e) {
            sender.sendMessage(ChatColor.RED + "No such schematic exists!"); // No schematic with the specified name exists
        }
        return true;
    }
}
