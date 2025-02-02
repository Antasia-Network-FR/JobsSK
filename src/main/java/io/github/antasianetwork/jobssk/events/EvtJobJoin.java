package io.github.antasianetwork.jobssk.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.util.NullableChecker;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import com.gamingmesh.jobs.container.Job;
import io.github.antasianetwork.jobssk.JobsSK;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converter;

public class EvtJobJoin extends SkriptEvent {

    static {
        Skript.registerEvent("Player join job", EvtJobJoin.class, JobsJoinEvent.class, "player join job [%-job%]");
        EventValues.registerEventValue(JobsJoinEvent.class, Job.class, new Converter<>() {
            @Nullable
            @Override
            public Job convert(JobsJoinEvent from) {
                return from.getJob();
            }
        });
        EventValues.registerEventValue(JobsJoinEvent.class, Player.class, new Converter<>() {
            @Nullable
            @Override
            public Player convert(JobsJoinEvent from) {
                return from.getPlayer().getPlayer();
            }
        });
    }

    Literal<Job> job;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        job = (Literal<Job>) args[0];
        JobsSK.getInstance().getLogger().info("Init JobJoin.java");
        return true;
    }

    @Override
    public boolean check(Event event) {
        JobsSK.getInstance().getLogger().info("Check JobJoin.java");
        if (job != null) {
            return job.check(event, (NullableChecker<Job>) o -> o == null || o.getId() == job.getAll()[0].getId());
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Job " + job.toString(event, debug);
    }
}
