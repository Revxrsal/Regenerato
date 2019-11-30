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
package io.github.regenerato.legacy;

import com.google.common.io.Files;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import io.github.regenerato.worldedit.NoSchematicException;
import io.github.regenerato.worldedit.SchematicProcessor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchematicProcessorImpl extends SchematicProcessor {

    public SchematicProcessorImpl() {
    }

    /**
     * Creates a new schematic processor
     *
     * @param plugin Plugin instance
     * @param name   Name of the schematic
     */
    private SchematicProcessorImpl(WorldEditPlugin plugin, String name, File directory) {
        super(plugin, name, directory);
    }

    @Override
    public void write(Player player) throws EmptyClipboardException {
        try (Closer closer = Closer.create()) {
            com.sk89q.worldedit.entity.Player localPlayer = plugin.wrapPlayer(player);
            LocalSession localSession = plugin.getWorldEdit().getSessionManager().get(localPlayer);
            ClipboardHolder selection = localSession.getClipboard();
            FileOutputStream fos = closer.register(new FileOutputStream(schematic));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(selection.getClipboard(), selection.getWorldData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EditSession paste(Location location) throws NoSchematicException {
        EditSession editSession = null;
        try {
            editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) new BukkitWorld(location.getWorld()), -1);
            editSession.enableQueue();
            SchematicFormat schematic = SchematicFormat.getFormat(this.schematic);
            CuboidClipboard cuboidClipboard = schematic.load(this.schematic);

            cuboidClipboard.paste(editSession, BukkitUtil.toVector(location), false);
            editSession.flushQueue();
        } catch (DataException | MaxChangedBlocksException | IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw new NoSchematicException(Files.getNameWithoutExtension(schematic.getName()));
        }
        return editSession;
    }

    @Override
    public SchematicProcessor newInstance(WorldEditPlugin plugin, String name, File directory) {
        return new SchematicProcessorImpl(plugin, name, directory);
    }
}
