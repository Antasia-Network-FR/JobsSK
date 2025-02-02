package io.github.antasianetwork.jobssk.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.gamingmesh.jobs.container.Job;
import org.jetbrains.annotations.Nullable;

public class ExprJobDisplayname extends SimplePropertyExpression<Job, String> {

    static {
        register(ExprJobDisplayname.class, String.class, "display[name]", "job");
    }

    @Nullable
    @Override
    public String convert(Job from) {
        return from.getDisplayName();
    }

    @Override
    protected String getPropertyName() {
        return "displayname";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode changeMode) {
        return null;
    }
}
