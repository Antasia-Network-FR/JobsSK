package io.github.antasianetwork.jobssk.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;


public class ExprJobsOfPlayer extends SimpleExpression<Job> {

    static {
        Skript.registerExpression(ExprJobsOfPlayer.class, Job.class, ExpressionType.PROPERTY,
                "[all] job[s] (of|from) %player%"
        );
    }

    private Expression<Player> player;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) expressions[0];
        return true;
    }

    @Override
    protected @Nullable Job[] get(Event event) {
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(this.player.getSingle(event));
        return Jobs.getJobs().stream().filter(jobsPlayer::isInJob).toList().toArray(new Job[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Job> getReturnType() {
        return Job.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "all jobs of " + this.player.toString(event, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Job.class);
        }
        return null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Player player = this.player.getSingle(event);
        if (player == null) return;
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jobsPlayer == null) return;

        switch (mode) {
            case ADD:
                if (delta != null && delta[0] instanceof Job job)
                    jobsPlayer.joinJob(job);
                break;
            case REMOVE:
                if (delta != null && delta[0] instanceof Job job)
                    jobsPlayer.leaveJob(job);
                break;
            case SET:
                if (delta != null)
                {
                    jobsPlayer.leaveAllJobs();
                    for (Object obj : delta) {
                        if (obj instanceof Job job)
                            jobsPlayer.joinJob(job);
                    }
                }
                break;
            case DELETE:
            case RESET:
            case REMOVE_ALL:
                jobsPlayer.leaveAllJobs();
                break;
        }
    }
}
