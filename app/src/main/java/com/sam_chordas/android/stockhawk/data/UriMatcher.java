package com.sam_chordas.android.stockhawk.data;

import android.net.Uri;
import android.util.SparseArray;

public class UriMatcher {
    private android.content.UriMatcher mUriMatcher;
    private SparseArray<ProviderUriEnum> mEnumsMap = new SparseArray<>();

    public UriMatcher() {
        mUriMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);
        buildUriMatcher();
    }

    private void buildUriMatcher() {
        final String authority = Contract.CONTENT_AUTHORITY;

        ProviderUriEnum[] uris = ProviderUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            mUriMatcher.addURI(authority, uris[i].path, uris[i].code);
        }

        buildEnumsMap();
    }

    private void buildEnumsMap() {
        ProviderUriEnum[] uris = ProviderUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            mEnumsMap.put(uris[i].code, uris[i]);
        }
    }

    public ProviderUriEnum matchUri(Uri uri){
        final int code = mUriMatcher.match(uri);
        try {
            return matchCode(code);
        } catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    public ProviderUriEnum matchCode(int code){
        ProviderUriEnum scheduleUriEnum = mEnumsMap.get(code);
        if (scheduleUriEnum != null){
            return scheduleUriEnum;
        } else {
            throw new UnsupportedOperationException("Unknown uri with code " + code);
        }
    }
}