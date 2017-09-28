package com.gionee.gnvoiceassist.util.kookong;

import com.gionee.gnvoiceassist.util.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twf on 2017/9/1.
 */

public class KookongCustomDataHelper {

    private static KookongUpdateCustomDataService updateCustomDataService;

    public static void bindDataRetriveService() {
        updateCustomDataService = new KookongUpdateCustomDataService();
        updateCustomDataService.execute();
    }

    public static Map<String, String[]> getKookongOfflineAsrSlotMap() {
        Map<String, String[]> kookongOfflineAsrSlotMap = new HashMap<>();
        if(updateCustomDataService != null) {
            String[] deviceList = updateCustomDataService.getDeviceLists();
            String[] customACStateList = updateCustomDataService.getCustomACStateLists();
            kookongOfflineAsrSlotMap.put(Constants.SLOT_DEVICELIST, deviceList);
            kookongOfflineAsrSlotMap.put(Constants.SLOT_CUSTOMACSTATELIST, customACStateList);
        }
        updateCustomDataService = null;
        return kookongOfflineAsrSlotMap;
    }
}
