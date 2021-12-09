package com.example.workmanagerimplementation.SyncUtils;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public class SyncSingleTon {
        private static SyncActivity INSTANCE = null;
        private SyncSingleTon() {};
        public static SyncActivity getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new SyncActivity();
            }
            return(INSTANCE);
        }
}
