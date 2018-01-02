package com.gionee.voiceassist.coreservice.sdk.module.localaudioplayer.message;

/**
 * Created by liyingheng on 10/16/17.
 */

import com.baidu.duer.dcs.framework.message.Payload;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientContextPayload
        extends Payload {
    private Source source;
    private String activity;
    private String status;
    private String playerName;

    public ClientContextPayload(@JsonProperty("source") Source source, @JsonProperty("activity") String activity, @JsonProperty("status") String status, @JsonProperty("playerName") String playerName)
    {
        this.source = source;
        this.activity = activity;
        this.status = status;
        this.playerName = playerName;
    }

    public Source getSource()
    {
        return this.source;
    }

    public void setSource(Source source)
    {
        this.source = source;
    }

    public String getActivity()
    {
        return this.activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public String getStatus()
    {
        return this.status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPlayerName()
    {
        return this.playerName;
    }

    public void setPlayerName(String playerName)
    {
        this.playerName = playerName;
    }
}
