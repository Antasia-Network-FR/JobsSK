package io.github.antasianetwork.jobssk.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.gamingmesh.jobs.container.Job;
import org.jetbrains.annotations.Nullable;

public class ExprJobId extends SimplePropertyExpression<Job, Integer> {

    static {
        register(ExprJobId.class, Integer.class, "id", "job");
    }

    @Nullable
    @Override
    public Integer convert(Job from) {
        return from.getId();
    }

    @Override
    protected String getPropertyName() {
        return "id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode changeMode) {
        return null;
    }
}
