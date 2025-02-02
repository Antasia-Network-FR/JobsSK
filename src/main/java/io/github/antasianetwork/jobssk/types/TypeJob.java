package io.github.antasianetwork.jobssk.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import io.github.antasianetwork.jobssk.JobsSK;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;

public class TypeJob {
    static {
        Classes.registerClass(new ClassInfo<>(Job.class, "job")
                .user("jobs?")
                .name("Job")
                .description("Represents Jobs from Jobs Reborn.")
                .examples("on player job join:", "\tbroadcast \"%player% joined the %name of job-event% !\"")
                .defaultExpression(new EventValueExpression<>(Job.class))
                .parser(new Parser<>() {
                    @Override
                    public String toString(Job job, int flags) {
                        return toVariableNameString(job);
                    }

                    @Override
                    public String toVariableNameString(Job job) {
                        return job.getName();
                    }

                    @Nullable
                    @Override
                    public Job parse(String s, ParseContext context) {
                        return Jobs.getJob(s);
                    }
                }).serializer(new Serializer<>() {
                    @Override
                    public Fields serialize(Job job) {
                        Fields fields = new Fields();
                        fields.putObject("id", job.getId());
                        return fields;
                    }

                    @Override
                    public void deserialize(Job job, Fields fields) throws StreamCorruptedException {
                        Jobs.getJob(fields.getAndRemoveObject("id", String.class));
                    }

                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }

                    @Override
                    protected boolean canBeInstantiated() {
                        return false;
                    }
                })
        );
    }
}
