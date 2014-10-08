package com.withub.model.workflow.event;

import org.springframework.context.ApplicationEvent;

public class DecisionCreateEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private DecisionEventArgs decisionEventArgs;

    public DecisionCreateEvent(Object source, DecisionEventArgs decisionEventArgs) {

        super(source);
        this.decisionEventArgs = decisionEventArgs;
    }

    public DecisionEventArgs getDecisionEventArgs() {

        return decisionEventArgs;
    }

    public void setDecisionEventArgs(DecisionEventArgs decisionEventArgs) {

        this.decisionEventArgs = decisionEventArgs;
    }
}
