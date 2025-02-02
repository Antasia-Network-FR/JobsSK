package io.github.antasianetwork.jobssk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.gamingmesh.jobs.Jobs;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class JobsSK extends JavaPlugin {
    private static JobsSK instance;

    @Override
    public void onEnable() {
        getLogger().info("Starting JobsSK...");
        instance = this;

        Plugin skript = getServer().getPluginManager().getPlugin("Skript");
        if (skript == null || !skript.isEnabled()) {
            getLogger().info("Skript isn't installed or loaded, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().enablePlugin(Jobs.getInstance());
        if (Jobs.getInstance() == null || !Jobs.getInstance().isEnabled()) {
            getLogger().info("Jobs isn't installed or loaded, disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        SkriptAddon addon = Skript.registerAddon(this);
        try {
            getLogger().info("Loading skript syntaxes...");
            addon.loadClasses("io.github.antasianetwork.jobssk");
        } catch (IOException e) {
            getLogger().severe("An error occured when loading syntaxes :\n" + e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static JobsSK getInstance() {
        return instance;
    }
}
