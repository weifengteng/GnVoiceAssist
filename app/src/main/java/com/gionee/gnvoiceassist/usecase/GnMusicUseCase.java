package com.gionee.gnvoiceassist.usecase;

import com.gionee.gnvoiceassist.message.model.DirectiveResponseEntity;
import com.gionee.gnvoiceassist.util.constants.UsecaseConstants.UsecaseAlias;

/**
 * Created by liyingheng on 11/9/17.
 */

public class GnMusicUseCase extends UseCase {
    @Override
    public void handleMessage(DirectiveResponseEntity message) {

    }

    @Override
    public String getUseCaseName() {
        return UsecaseAlias.GN_MUSIC;
    }
}
