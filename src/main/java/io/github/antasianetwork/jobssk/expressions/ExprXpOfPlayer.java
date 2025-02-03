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
import com.gamingmesh.jobs.container.JobProgression;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;


public class ExprXpOfPlayer extends SimpleExpression<Double> {

    static {
        Skript.registerExpression(ExprXpOfPlayer.class, Double.class, ExpressionType.PROPERTY,
                "([e]xp) (of|from) %job% of %player%"
        );
    }

    private Expression<Player> player;
    private Expression<Job> job;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        job = (Expression<Job>) expressions[0];
        player = (Expression<Player>) expressions[1];
        return true;
    }

    @Override
    protected @Nullable Double[] get(Event event) {
        return new Double[] { Jobs.getPlayerManager().getJobsPlayer(player.getSingle(event)).getJobProgression(this.job.getSingle(event)).getExperience() };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "xp for job " + this.job.toString(event, debug) + " for player " + this.player.toString(event, debug);
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET)
            return CollectionUtils.array(Double.class);
        return null;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Player player = this.player.getSingle(event);
        if (player == null) return;
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
        if (jobsPlayer == null) return;
        JobProgression jobProgression = jobsPlayer.getJobProgression(this.job.getSingle(event));
        if (jobProgression == null) return;

        switch (mode) {
            case ADD:
                if (delta != null && delta[0] instanceof Double xp)
                    jobProgression.setExperience(jobProgression.getExperience() + xp);
                break;
            case REMOVE:
                if (delta != null && delta[0] instanceof Double xp)
                    jobProgression.setExperience(jobProgression.getExperience() - xp);
                break;
            case SET:
                if (delta != null && delta[0] instanceof Double xp)
                    jobProgression.setExperience(xp);
                break;
            case DELETE:
            case RESET:
            case REMOVE_ALL:
                jobProgression.setExperience(0);
                break;
        }
    }
}
