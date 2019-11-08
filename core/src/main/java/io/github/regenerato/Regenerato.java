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
import org.bukkit.plugin.java.JavaPlugin;

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
        getLogger().info("Regenerato has been successfully enabled!");
        getDataFolder().mkdirs();
        worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
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