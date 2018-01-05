package com.gionee.voiceassist.coreservice.datamodel;

/**
 * 打开网页场景的payload
 */

public class WebBrowserDirectiveEntity extends DirectiveEntity {

    private String url;

    public WebBrowserDirectiveEntity() {
        setType(Type.WEBBROWSER);
    }

    /**
     * 取得网页URL
     * @return 网页URL
     */
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
