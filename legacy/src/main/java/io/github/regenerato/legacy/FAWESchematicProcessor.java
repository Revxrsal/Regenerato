package io.github.regenerato.legacy;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.github.regenerato.worldedit.NoSchematicException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class FAWESchematicProcessor extends WESchematicProcessor {

    public FAWESchematicProcessor() {
    }

    public FAWESchematicProcessor(WorldEditPlugin plugin, String name, File directory) {
        super(plugin, name, directory);
    }

    @Override
    public void write(ClipboardHolder clipboard) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                super.write(clipboard);
            } catch (EmptyClipboardException e) {
                throw sneakyThrow(e);
            }
        });
    }

    @Override
    public CompletableFuture<EditSession> paste(Location location) {
        CompletableFuture<EditSession> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                super.paste(location).thenApply(future::complete);
            } catch (NoSchematicException e) {
                future.obtrudeException(e);
                throw sneakyThrow(e);
            }
        });
        return future;
    }


}
