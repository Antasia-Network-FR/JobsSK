package io.github.antasianetwork.jobssk.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import com.gamingmesh.jobs.api.JobsLeaveEvent;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.lang.converter.Converter;

public class EvtJobLeave extends SkriptEvent {

    static {
        Skript.registerEvent("Player leave job", EvtJobLeave.class, JobsJoinEvent.class, "player leave [(a|the)] job [[(with name|named) ]%-string%]");
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

    private Literal<Job> job;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        job = args[0] == null ? null : (Literal<Job>) args[0].getConvertedExpression(Job.class);
        return true;
    }

    @Override
    public boolean check(Event event) {
        if (job != null)
            return job.check(event, jobC -> jobC.equals(((JobsLeaveEvent) event).getJob()));
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Player leave job " + job.toString(event, debug);
    }
}
