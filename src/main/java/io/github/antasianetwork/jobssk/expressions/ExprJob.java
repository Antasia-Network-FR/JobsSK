package io.github.antasianetwork.jobssk.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Job from ID/Name")
@Description("Returns the Job from its ID or Name.")
@Examples({"job named \"miner\"", "the job \"farmer\""})
@Since("1.0.0")
public class ExprJob extends SimpleExpression<Job> {

    static {
        Skript.registerExpression(ExprJob.class, Job.class, ExpressionType.SIMPLE, "[the] job [(named|with name)] %string%");
    }

    private Expression<String> jobName;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        jobName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    @Nullable
    protected Job[] get(Event e) {
        String jobName = this.jobName.getSingle(e);
        if (jobName == null)
            return null;
        Job job = Jobs.getJob(jobName);
        if (job == null)
            return null;

        return new Job[] {job};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<Job> getReturnType() {
        return Job.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the job with the name " + jobName.toString(e, debug);
    }
}