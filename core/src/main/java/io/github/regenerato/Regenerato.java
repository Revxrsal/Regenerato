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
package io.github.regenerato;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import io.github.regenerato.commands.PasteCommand;
import io.github.regenerato.commands.SaveCommand;
import io.github.regenerato.worldedit.SchematicProcessor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Main class
 */
public class Regenerato extends JavaPlugin {

    private WorldEditPlugin worldEdit;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Plugin we = Bukkit.getPluginManager().getPlugin("WorldEdit");
        Plugin fawe = Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit");
        if (we == null && fawe == null) {
            getLogger().severe("WorldEdit / FastAsyncWorldEdit not found. Disabling Regenerato");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Using " + SchematicProcessor.ADAPTER_NAME.get() + " for processing schematics");
        getDataFolder().mkdirs();

        if (getConfig().getBoolean("developer-mode")) {
            getCommand("pasteschem").setExecutor(new PasteCommand(this));
            getCommand("saveschem").setExecutor(new SaveCommand(this));
        }

        worldEdit = (WorldEditPlugin) (we != null ? we : checkNotNull(fawe));
    }

    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        getLogger().info("Regenerato has been successfully disabled!");
    }

    public WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }
}