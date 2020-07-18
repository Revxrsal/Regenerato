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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WESchematicProcessor extends SchematicProcessor {

    public WESchematicProcessor() {
    }

    /**
     * Creates a new schematic processor
     *
     * @param plugin Plugin instance
     * @param name   Name of the schematic
     */
    protected WESchematicProcessor(WorldEditPlugin plugin, String name, File directory) {
        super(plugin, name, directory);
    }

    @Override
    public void write(ClipboardHolder selection) throws EmptyClipboardException {
        try (Closer closer = Closer.create()) {
            FileOutputStream fos = closer.register(new FileOutputStream(schematic));
            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(ClipboardFormat.SCHEMATIC.getWriter(bos));
            writer.write(selection.getClipboard(), selection.getWorldData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<EditSession> paste(Location location) throws NoSchematicException {
        CompletableFuture<EditSession> future = new CompletableFuture<>();
        EditSession editSession = null;
        try {
            editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) new BukkitWorld(location.getWorld()), -1);
            editSession.enableQueue();
            SchematicFormat schematic = SchematicFormat.getFormat(this.schematic);
            CuboidClipboard cuboidClipboard = schematic.load(this.schematic);

            cuboidClipboard.paste(editSession, BukkitUtil.toVector(location), false);
            editSession.flushQueue();
            future.complete(editSession);
        } catch (DataException | MaxChangedBlocksException | IOException e) {
            e.printStackTrace();
            future.obtrudeException(e);
        } catch (NullPointerException e) {
            throw new NoSchematicException(SchematicProcessor.getBaseName(schematic));
        }

        return future;
    }

    @Override
    public SchematicProcessor newInstance(WorldEditPlugin plugin, String name, File directory) {
        return new WESchematicProcessor(plugin, name, directory);
    }
}
