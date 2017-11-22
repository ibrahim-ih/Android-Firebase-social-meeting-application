package com.ngenious.ibrahim.liny.model;

/**
 * Created by ibrahim on 24/05/17.
 */

public class History {
    String invitationId;

    public History() {
    }
    public History(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public History setInvitationId(String invitationId) {
        this.invitationId = invitationId;
        return this;
    }
}
