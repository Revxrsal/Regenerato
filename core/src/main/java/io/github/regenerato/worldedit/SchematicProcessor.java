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
package io.github.regenerato.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.github.regenerato.Regenerato;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract class for processing schematics across different WorldEdit versions
 */
public abstract class SchematicProcessor {

    /**
     * The server's protocol. For example, if 1.11.2 it will be {@code 11}
     */
    private static final int PROTOCOL = Integer.parseInt(getVersion(Bukkit.getServer()).split("_")[1]);

    /**
     * Represents the schematic processor factory which creates instance. Should NOT be used to write or paste
     * schematics.
     */
    private static final SchematicProcessor FACTORY;

    /**
     * The name of the WorldEdit adapter
     */
    public static final AtomicReference<String> ADAPTER_NAME = new AtomicReference<>();

    /**
     * Represents the schematic file
     */
    protected File schematic;

    /**
     * Plugin instance
     */
    protected WorldEditPlugin plugin;

    /* Accessed to create instances */
    protected SchematicProcessor() {
    }

    /**
     * Creates a new schematic processor
     *
     * @param plugin        Plugin instance
     * @param schematicName Name of the schematic
     * @param directory     The directory that contains the schematic
     */
    protected SchematicProcessor(WorldEditPlugin plugin, String schematicName, File directory) {
        Preconditions.checkNotNull(plugin, "WorldEditPlugin cannot be null");
        Preconditions.checkNotNull(schematicName, "schematicName cannot be null");
        Preconditions.checkNotNull(directory, "directory cannot be null");
        this.plugin = plugin;
        schematic = new File(directory, schematicName + ".schem");
        try {
            schematic.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(Player player) throws EmptyClipboardException {
        com.sk89q.worldedit.entity.Player localPlayer = plugin.wrapPlayer(player);
        LocalSession localSession = plugin.getWorldEdit().getSessionManager().get(localPlayer);
        ClipboardHolder selection = localSession.getClipboard();
        write(selection);
    }

    /**
     * Writes the specified clipboard data to the schematic
     *
     * @param player Player to save clipboard for
     */
    public abstract void write(ClipboardHolder player) throws EmptyClipboardException;

    /**
     * Pastes the specified clipboard at the specified location
     *
     * @param location Location to paste in
     * @return A completable future for tracking the pasting progress of this schematic.
     * <p>
     * NOTE: This does not mean the process is asynchronous! Check the adapter name to know this.
     */
    public abstract CompletableFuture<EditSession> paste(Location location) throws NoSchematicException;

    /**
     * Creates a new instance of the processor
     *
     * @param plugin Plugin instance
     * @param name   Name of the schematic
     * @return The newly created schematic processor
     */
    protected abstract SchematicProcessor newInstance(WorldEditPlugin plugin, String name, File directory);

    /**
     * Creates a new Schematic processor for the appropriate version
     *
     * @param plugin    Plugin to create for
     * @param name      Name of the schematic
     * @param directory Directory that contains this schematic
     * @return The newly created schematic processor
     */
    public static SchematicProcessor newSchematicProcessor(WorldEditPlugin plugin, String name, File directory) {
        return FACTORY.newInstance(plugin, name, directory);
    }

    /**
     * Returns the file name without its
     * <a href="http://en.wikipedia.org/wiki/Filename_extension">file extension</a> or path. This is
     * similar to the {@code basename} unix command. The result does not include the '{@code .}'.
     *
     * @param file The name of the file to trim the extension from. This can be either a fully
     *             qualified file name (including a path) or just a file name.
     * @return The file name without its path or extension.
     * @since 14.0
     */
    public static String getBaseName(File file) {
        checkNotNull(file);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    static {
        SchematicProcessor factory;
        String classifier = PROTOCOL >= 13 ? "modern" : "legacy";
        try {
            if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") != null)
                factory = (SchematicProcessor) Class.forName("io.github.regenerato." + classifier + ".FAWESchematicProcessor").newInstance();
            else
                factory = (SchematicProcessor) Class.forName("io.github.regenerato." + classifier + ".WESchematicProcessor").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            factory = null;
            Regenerato.getRegenerato().getLogger().severe("Cannot initiate instance of SchematicProcessor.");
            e.printStackTrace();
        }
        FACTORY = factory;
        if (FACTORY != null) {
            ADAPTER_NAME.set(FACTORY.getClass().getSimpleName());
        } else {
            throw new IllegalStateException("Cannot create factory instance of SchematicProcessor. Check last error for details.");
        }
    }

    /**
     * Returns the server version
     *
     * @param server Server to retrieve from
     * @return The version, e.g v1_11_R1
     */
    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    @SuppressWarnings("RedundantTypeArguments")
    protected static RuntimeException sneakyThrow(Throwable t) {
        if (t == null) throw new NullPointerException("t");
        return SchematicProcessor.<RuntimeException>sneakyThrow0(t);
    }

    private static <T extends Throwable> T sneakyThrow0(Throwable t) throws T {
        throw (T) t;
    }
}
