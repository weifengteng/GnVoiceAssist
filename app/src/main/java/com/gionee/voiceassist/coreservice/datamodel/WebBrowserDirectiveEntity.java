package com.gionee.voiceassist.coreservice.datamodel;

/**
 * Created by liyingheng on 1/5/18.
 */

public class WebBrowserDirectiveEntity extends DirectiveEntity {

    private String url;

    public WebBrowserDirectiveEntity() {
        setType(Type.WEBBROWSER);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WebBrowserDirectiveEntity{" +
                "url='" + url + '\'' +
                '}';
    }
}
